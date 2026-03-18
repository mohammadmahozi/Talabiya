package com.mahozi.talabiya.order

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mahozi.talabiya.TalabiyaDatabase
import com.mahozi.talabiya.database.createDatabase
import org.junit.Before
import org.junit.Test
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
    database = createDatabase()
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
      note = "",
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
    orderQueries.insertOrderItem(customerId = 1, quantity = 1, orderItemPriceId = 1, note = "")
    orderQueries.insertOrderItem(customerId = 1, quantity = 5, orderItemPriceId = 2, note = "")

    database.userQueries.insert("Customer 2")
    orderQueries.insertOrderItem(customerId = 2, quantity = 10, orderItemPriceId = 1, note = "")

    orderQueries.selectAllOrderItems(1).executeAsList().forEach(::println)
  }
}