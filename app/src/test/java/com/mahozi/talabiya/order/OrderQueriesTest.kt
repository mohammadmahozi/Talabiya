package com.mahozi.talabiya.order

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mahozi.sayed.talabiya.core.data.TypeAdapters
import com.mahozi.talabiya.TalabiyaDatabase
import order.OrderEntity
import order.OrderItemPriceEntity
import org.junit.Before
import org.junit.Test
import payment.PaymentEntity
import restaurant.MenuItemPriceEntity
import java.time.Instant
import java.util.Properties

class OrderQueriesTest {

  private lateinit var database: TalabiyaDatabase
  private val orderQueries get() = database.orderQueries

  @Before fun setUp() {
    val driver = JdbcSqliteDriver(
      url = JdbcSqliteDriver.IN_MEMORY,
      properties = Properties().apply { put("foreign_keys", "true") }
    )
    TalabiyaDatabase.Schema.create(driver)
    database = TalabiyaDatabase(
      driver,
      MenuItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
      OrderEntity.Adapter(TypeAdapters.instantAdapter),
      OrderItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
      PaymentEntity.Adapter(TypeAdapters.instantAdapter),
    )
  }

  @Test fun selectOrderById() {
    val time = Instant.now()
    database.restaurantQueries.insert("Restaurant")
    database.menuItemQueries.insert(
      restaurantId = 1,
      name = "Name",
      category = "Pastry"
    )
    database.menuItemQueries.insertPrice(
      menuItemId = 1,
      datetime = time,
      price = 100
    )
    orderQueries.insert(
      restaurantId = 1,
      createdAt = time
    )
    orderQueries.insertOrderItem(
      customerId = 1,
      quantity = 1,
      orderItemPriceId = 1,
    )
    val order = orderQueries.selectById(1).executeAsOne()
    println(order)
  }

  @Test fun selectAllSuborders() {
    val time = Instant.now()
    database.restaurantQueries.insert("Restaurant")
    database.menuItemQueries.insert(
      restaurantId = 1,
      name = "Item 1",
      category = "Pastry"
    )
    database.menuItemQueries.insertPrice(
      menuItemId = 1,
      datetime = time,
      price = 100
    )
    database.menuItemQueries.lastInsertRowId()

    database.menuItemQueries.insert(
      restaurantId = 1,
      name = "Item 2",
      category = "Pastry"
    )
    database.menuItemQueries.insertPrice(
      menuItemId = 2,
      datetime = time,
      price = 200
    )

    orderQueries.insert(
      restaurantId = 1,
      createdAt = time
    )

    database.userQueries.insert("Customer 1")
    orderQueries.insertOrderItem(1, 1, 1)
    orderQueries.insertOrderItem(1, 5, 2)

    database.userQueries.insert("Customer 2")
    orderQueries.insertOrderItem(2, 10, 1)

    orderQueries.selectAllOrderItems(1).executeAsList().forEach(::println)
  }
}