package com.mahozi.talabiya.order

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mahozi.sayed.talabiya.core.data.TypeAdapters
import com.mahozi.talabiya.Database
import order.OrderEntity
import org.junit.Before
import org.junit.Test
import restaurant.MenuItemPriceEntity
import java.time.Instant

class OrderQueriesTest {

  private lateinit var database: Database
  private val orderQueries get() = database.orderQueries

  @Before fun setUp() {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
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
    database.menuItemQueries.insert(1, "Name", "Category")
    database.menuItemQueries.insertPrice(1, time, 100)
    orderQueries.insert(1, time)
    orderQueries.insertOrderItem(1, 1, 1, 1)
    val order = orderQueries.selectById(1).executeAsOne()
    println(order)
  }

}