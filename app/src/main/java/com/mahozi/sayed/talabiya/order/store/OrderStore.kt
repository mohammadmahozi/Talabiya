package com.mahozi.sayed.talabiya.order.store

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.order.details.edit.PricedOrderItem
import com.mahozi.sayed.talabiya.order.details.full.FullOrderItem
import com.mahozi.sayed.talabiya.order.details.info.OrderInfo
import com.mahozi.sayed.talabiya.order.details.suborder.OrderItem
import com.mahozi.sayed.talabiya.order.details.suborder.Suborder
import com.mahozi.sayed.talabiya.order.list.Order
import com.mahozi.sayed.talabiya.userorder.UserOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import order.OrderQueries
import restaurant.MenuItemQueries
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Optional
import dev.zacsweers.metro.Inject
import kotlin.jvm.optionals.getOrNull

class OrderStore @Inject constructor(
  private val orderQueries: OrderQueries,
  private val menuQueries: MenuItemQueries,
  private val dispatcher: CoroutineDispatcher,
) {

  val orders: Flow<List<Order>> = orderQueries
    .selectAll(mapper = { id, createdAt, name ->
      Order(id, name, createdAt)
    })
    .asFlow()
    .mapToList(dispatcher)

  fun getOrderDetails(id: Long): Flow<OrderInfo> {
    return orderQueries.selectById(
      id = id,
      mapper = { id, createdAt, restaurant, payer, total, attachment, note ->
        OrderInfo(
          id = id,
          createdAt = createdAt,
          restaurant = restaurant,
          payer = payer,
          total = (total ?: 0.0).money,
          invoice = attachment,
          note = note
        )
      }).asFlow()
      .mapToOne(dispatcher)
  }

  fun getUserOrders(userId: Long): Flow<List<UserOrder>> {
    return orderQueries.selectUserOrders(
      userId = userId,
      mapper = { orderId, userId, restaurant, createdAt ->
        UserOrder(
          orderId = orderId,
          userId = userId,
          user = "",
          restaurant = restaurant,
          createdAt = createdAt,
          total = 0.money,
        )
      }
    ).asFlow()
    .mapToList(dispatcher)
  }
  fun getSuborders(id: Long): Flow<List<Suborder>> {
    return orderQueries.selectAllOrderItems(id)
      .asFlow()
      .map { selectAllOrderItemsQuery ->
        selectAllOrderItemsQuery.executeAsList()
          .groupBy { item -> item.customerId }
          .map { (id, orderItems) ->
            val suborderTotal = orderItems.sumOf { it.total }.money

            Suborder(
              id,
              orderItems.first().customerId,
              orderItems.first().name,
              orderItems.map { OrderItem(it.id, it.quantity.toInt(), it.name, it.total.money) },
              suborderTotal,
            )
        }
      }
  }

  fun getUserOrderItems(
    orderId: Long,
    userId: Long,
  ): Flow<List<OrderItem>> {
    return orderQueries.selectUserOrderItems(orderId, userId)
      .asFlow()
      .map { query ->
        query.executeAsList().map { item ->
          OrderItem(
            item.id,
            item.quantity.toInt(),
            item.name,
            item.total.money
          )
        }
      }
  }

  fun getPricedOrderItems(orderId: Long): Flow<List<PricedOrderItem>> {
    return orderQueries.selectOrderItemsPrices(orderId)
      .asFlow()
      .map { query ->
        query.executeAsList().map { item ->
          PricedOrderItem(
            orderItemId = item.id,
            menuItemId = item.menuItemId,
            name = item.name,
            price = item.price.money
          )
        }
      }
  }

  fun getFullOrderItems(orderId: Long): Flow<List<FullOrderItem>> {
    return orderQueries.selectFullOrderItem(orderId)
      .asFlow()
      .map { query ->
        query.executeAsList().map { item ->
          FullOrderItem(
            id = item.id,
            name = item.name,
            quantity = item.quantity!!.toInt()
          )
        }
      }
  }

  suspend fun createOrder(
    restaurantId: Long,
    date: LocalDate,
    time: LocalTime,
  ) {
    val instant = LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant()
    withContext(dispatcher) {
      orderQueries.insert(
        restaurantId = restaurantId,
        createdAt = instant
      )
    }
  }

  suspend fun insertOrderItem(
    orderId: Long,
    customerId: Long,
    menuItemId: Long,
    quantity: Long,
    price: Money,
  ) {
    withContext(dispatcher) {
      orderQueries.transaction {
        orderQueries.insertOrderItemPrice(orderId, menuItemId, price.toLong(), Instant.now())

        val priceId = orderQueries.selectOrderItemPriceId(orderId, menuItemId).executeAsOne()
        orderQueries.insertOrderItem(
          customerId,
          quantity,
          priceId
        )
      }
    }
  }

  suspend fun updateOrderItemPrice(
    orderId: Long,
    itemId: Long,
    price: Money,
    setAsDefaultPrice: Boolean,
  ) {
    withContext(dispatcher) {
      orderQueries.updateOrderItemsPrice(
        price = price.toLong(),
        orderId = orderId,
        menuItemId = itemId
      )

      if (setAsDefaultPrice) {
        menuQueries.insertPrice(
          menuItemId = itemId,
          datetime = Instant.now(),
          price = price.toLong()
        )
      }
    }
  }

  suspend fun getRestaurantId(orderId: Long): Long {
    return withContext(dispatcher) {
      orderQueries.selectRestaurantId(orderId).executeAsOne()
    }
  }

  suspend fun updateOrder(
    orderId: Long,
    date: LocalDate? = null,
    time: LocalTime? = null,
    payerId: Optional<Long>? = null,
    invoice: Optional<String>? = null,
    note: String? = null,
  ) {
    withContext(dispatcher) {
      if (date != null) {
        var instant = orderQueries.selectCreationTime(orderId).executeAsOne()
        instant = instant
          .atZone(ZoneId.systemDefault())
          .with(date)
          .toInstant()

        orderQueries.updateCreationTime(id = orderId, createdAt = instant)
      }
      if (time != null) {
        var instant = orderQueries.selectCreationTime(orderId).executeAsOne()
        instant = instant
          .atZone(ZoneId.systemDefault())
          .with(time)
          .toInstant()

        orderQueries.updateCreationTime(id = orderId, createdAt = instant)
      }

      if (payerId != null) {
        orderQueries.updatePayer(id = orderId, payerId = payerId.getOrNull())
      }

      if (payerId != null) {
        orderQueries.updatePayer(id = orderId, payerId = payerId.getOrNull())
      }

      if (invoice != null) {
        orderQueries.updateAttachment(id = orderId, attachment = invoice.getOrNull())
      }
      if (note != null) {
        orderQueries.updateNote(id = orderId, note = note)
      }
    }
  }
}