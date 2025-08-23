package com.mahozi.talabiya.database.payment

import com.mahozi.talabiya.Database
import com.mahozi.talabiya.database.createDatabase
import org.junit.Before
import org.junit.Test
import java.time.Instant


class PaymentQueriesTest {

  private lateinit var database: Database
  private val paymentQueries get() = database.paymentQueries
  private val orderQueries get() = database.orderQueries
  private val restaurantQueries get() = database.restaurantQueries
  private val menuItemQueries get() = database.menuItemQueries
  private val userQueries get() = database.userQueries

  @Before
  fun setUp() {
    database = createDatabase()
  }

  @Test
  fun selectUnpaidOrders() {
    restaurantQueries.insert("Restaurant")
    val restaurantId = restaurantQueries.lastInsertRowId().executeAsOne()

    menuItemQueries.insert(restaurantId = restaurantId, name = "Item 1", category = "Pastry")
    val menuItemId1 = menuItemQueries.lastInsertRowId().executeAsOne()
    menuItemQueries.insertPrice(menuItemId = menuItemId1, Instant.now(), 1000)

    menuItemQueries.insert(restaurantId = restaurantId, name = "Item 2", category = "Pastry")
    val menuItemId2 = menuItemQueries.lastInsertRowId().executeAsOne()
    menuItemQueries.insertPrice(menuItemId = menuItemId2, Instant.now(), 1500)

    userQueries.insert("User1")
    val user1Id = userQueries.lastInsertRowId().executeAsOne()

    userQueries.insert("User2")
    val user2Id = userQueries.lastInsertRowId().executeAsOne()

    orderQueries.insert(restaurantId = restaurantId, createdAt = Instant.now())
    val firstOrderId = orderQueries.lastInsertRowId().executeAsOne()

    orderQueries.insertOrderItemPrice(
      orderId = firstOrderId,
      price = 1000,
      menuItemId = menuItemId1,
      datetime = Instant.now()
    )
    val firstOrderItem1 = orderQueries.lastInsertRowId().executeAsOne()
    orderQueries.insertOrderItemPrice(
      orderId = firstOrderId,
      price = 1500,
      menuItemId = menuItemId2,
      datetime = Instant.now()
    )
    val firstOrderItem2 = orderQueries.lastInsertRowId().executeAsOne()

    orderQueries.insertOrderItem(customerId = user1Id, quantity = 1, orderItemPriceId = firstOrderItem1)
    orderQueries.insertOrderItem(customerId = user1Id, quantity = 2, orderItemPriceId = firstOrderItem2)
    orderQueries.insertOrderItem(customerId = user2Id, quantity = 1, orderItemPriceId = firstOrderItem1)


    orderQueries.insert(restaurantId = restaurantId, createdAt = Instant.now())
    val secondOrderId = orderQueries.lastInsertRowId().executeAsOne()
    orderQueries.insertOrderItemPrice(
      orderId = secondOrderId,
      price = 1000,
      menuItemId = menuItemId1,
      datetime = Instant.now()
    )
    val secondOrderItem1 = orderQueries.lastInsertRowId().executeAsOne()
    orderQueries.insertOrderItem(customerId = user1Id, quantity = 1, orderItemPriceId = secondOrderItem1)
    orderQueries.insertOrderItem(customerId = user2Id, quantity = 2, orderItemPriceId = secondOrderItem1)

    orderQueries.insert(restaurantId = restaurantId, createdAt = Instant.now())
    val thirdOrderId = orderQueries.lastInsertRowId().executeAsOne()
    orderQueries.insertOrderItemPrice(
      orderId = thirdOrderId,
      price = 1000,
      menuItemId = menuItemId1,
      datetime = Instant.now()
    )
    val thirdOrderItem1 = orderQueries.lastInsertRowId().executeAsOne()
    orderQueries.insertOrderItem(customerId = user1Id, quantity = 1, orderItemPriceId = thirdOrderItem1)
    orderQueries.insertOrderItem(customerId = user2Id, quantity = 1, orderItemPriceId = thirdOrderItem1)
    paymentQueries.insertPayment(
      user1Id,
      amount = 1000,
      createdAt = Instant.now(),
      status = "Completed"
    )

    val paymentId = paymentQueries.lastInsertRowId().executeAsOne()
    paymentQueries.insertOrderReimbursement(
      paymentId = paymentId,
      orderId = thirdOrderId
    )
    val unpaidOrders = paymentQueries.unpaidOrdersEntity(user1Id).executeAsList()
    unpaidOrders.forEach { println(it) }
  }
}