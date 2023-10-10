package com.mahozi.talabiya.order

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mahozi.sayed.talabiya.core.data.TypeAdapters
import com.mahozi.talabiya.Database
import order.OrderEntity
import org.junit.Before
import org.junit.Test
import restaurant.MenuItemPriceEntity
import java.time.Instant
import java.util.Properties

class OrderQueriesTest {

  private lateinit var database: Database
  private val orderQueries get() = database.orderQueries

  @Before fun setUp() {
    val driver = JdbcSqliteDriver(
      url = JdbcSqliteDriver.IN_MEMORY,
      properties = Properties().apply { put("foreign_keys", "true") }
    )
    Database.Schema.create(driver)
    database = Database(
      driver,
      MenuItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
      OrderEntity.Adapter(TypeAdapters.instantAdapter)
    )
  }

  @Test fun selectOrderById() {
    val time = Instant.now()
    database.restaurantQueries.insert("Restaurant")
    database.menuItemQueries.insert(1, "Name", "Pastry")
    database.menuItemQueries.insertPrice(1, time, 100)
    orderQueries.insert(1, time)
    orderQueries.insertOrderItem(1, 1, 1, 1)
    val order = orderQueries.selectById(1).executeAsOne()
    println(order)
  }

  @Test fun selectAllSuborders() {
    val time = Instant.now()
    database.restaurantQueries.insert("Restaurant")
    database.menuItemQueries.insert(1, "Item 1", "Pastry")
    database.menuItemQueries.insertPrice(1, time, 100)

    database.menuItemQueries.insert(1, "Item 2", "Pastry")
    database.menuItemQueries.insertPrice(2, time, 200)

    orderQueries.insert(1, time)

    database.customerQueries.insert("Customer 1")
    orderQueries.insertOrderItem(1, 1, 1, 1)
    orderQueries.insertOrderItem(1, 1, 5, 2)

    database.customerQueries.insert("Customer 2")
    orderQueries.insertOrderItem(1, 2, 10, 1)

    orderQueries.selectAllSuborders(1).executeAsList().forEach(::println)
  }
}