package com.mahozi.sayed.talabiya.order.store

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.order.details.info.OrderInfo
import com.mahozi.sayed.talabiya.order.details.suborder.OrderItem
import com.mahozi.sayed.talabiya.order.details.suborder.Suborder
import com.mahozi.sayed.talabiya.order.list.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import order.OrderQueries
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class OrderStore @Inject constructor(
  private val orderDao: OrderDao,
  private val orderQueries: OrderQueries,
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
      mapper = { _, createdAt, restaurant, payer, total, note ->
        OrderInfo(
          id,
          createdAt,
          restaurant,
          payer,
          (total ?: 0.0).money,
          note
        )
      }).asFlow()
      .mapToOne(dispatcher)
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
              false
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
    quantity: Long,
    menuItemPriceId: Long
  ) {
    withContext(dispatcher) {
      orderQueries.insertOrderItem(
        orderId, customerId, quantity, menuItemPriceId
      )
    }
  }

  fun insertSubOrder(subOrderEntity: SubOrderEntity) {
    orderDao.insertSubOrder(subOrderEntity)
  }

  fun insertOrderItem(orderItemEntity: OrderItemEntity): Long {
    return orderDao.insertOrderItem(orderItemEntity)
  }

  fun selectOrderItemsWithOrderIdAndPerson(
    orderId: Int,
    personName: String
  ): LiveData<List<OrderItemEntity>> {
    return orderDao.selectOrderItemsWithOrderIdAndPerson(orderId, personName)
  }

  fun selectSubOrdersAndOrderItems(orderId: Int): LiveData<List<SubOrderAndOrderItems>> {
    return orderDao.selectSubOrdersAndOrderItems(orderId)
  }

  fun updateSuborder(subOrderEntity: SubOrderEntity) {
    orderDao.updateSuborder(subOrderEntity)
  }

  fun deleteSuborder(subOrderEntity: SubOrderEntity) {
    orderDao.deleteSuborder(subOrderEntity)
  }

  fun deleteOrderItem(orderItemEntity: OrderItemEntity) {
    orderDao.deleteOrderItem(orderItemEntity)
  }

  fun deleteOrder(orderEntity: OrderEntity) {
    orderDao.deleteOrder(orderEntity)
  }

  fun updateOrder(orderEntity: OrderEntity) {
    orderDao.updateOrder(orderEntity)
  }

  fun selectFullOrder(orderId: Int): List<FullOrderEntity> {
    val query = SimpleSQLiteQuery(
      """
    SELECT OrderItemEntity.menu_item_name, SUM(quantity) as quantity, note, category FROM OrderItemEntity 
    LEFT outer JOIN MenuItemEntity ON OrderItemEntity.menu_item_name = MenuItemEntity.item_name 
    AND MenuItemEntity.restaurant_name = OrderItemEntity.restaurant_name
    WHERE OrderItemEntity.order_id =  GROUP BY OrderItemEntity.menu_item_name, note ORDER BY category, menu_item_name ASC
    """.trimIndent(), arrayOf<Any>(orderId)
    )
    return orderDao.selectFullOrder(query)
  }

  fun selectPersonInfo(personName: String): List<OrderAndPersonSuborder> {
    return orderDao.selectUnpaidPersonInfo(personName)
  }

  fun updateOrderItem(orderItemEntity: OrderItemEntity) {
    orderDao.updateOrderItem(orderItemEntity)
  }

  fun updateSuborderStatus(date: String, status: Int, id: Long) {
    orderDao.updateSuborderStatus(date, status, id)
  }

  fun updateOrderStatus(date: String, status: Int, id: Long) {
    orderDao.updateOrderStatus(date, status, id)
  }

  fun selectAllPersonInfo(personName: String): List<OrderAndPersonSuborder> {
    return orderDao.selectAllPersonInfo(personName)
  }

  fun selectSubordersAndOrderItemsWithOrderIdAndName(
    menuItemName: String,
    orderId: Int
  ): List<SuborderAndorderItem> {
    return orderDao.selectSubordersAndOrderItemsWithOrderIdAndName(menuItemName, orderId)
  }

  suspend fun getRestaurantId(orderId: Long): Long {
    return withContext(dispatcher) {
      orderQueries.selectRestaurantId(orderId).executeAsOne()
    }
  }
}