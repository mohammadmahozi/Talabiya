package com.mahozi.talabiya

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mahozi.sayed.talabiya.core.data.TypeAdapters
import order.OrderEntity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Before
import org.junit.Test
import java.time.Instant

class RestaurantQueriesTest {


  private lateinit var database: Database
  private val restaurantQueries get() = database.restaurantQueries
  private val menuItemQueries get() = database.menuItemQueries

  @Before fun before() {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    Database.Schema.create(driver)
    database = Database(driver, OrderEntity.Adapter(TypeAdapters.instantAdapter))
  }

  @Test fun menuItemsHaveTheMostRecentPrice() {
    restaurantQueries.insert("Restaurant")
    val restaurant = restaurantQueries.selectAll().executeAsOne()
    menuItemQueries.insert(restaurant.id, "item")

    val newestDateTime = Instant.now()
    val newestDateTimePrice = 1000L

    val olderDateTime = Instant.now().minusSeconds(1)
    val olderDateTimePrice = 900L

    menuItemQueries.insertPrice(1, newestDateTime.toEpochMilli(), newestDateTimePrice)
    menuItemQueries.insertPrice(1, olderDateTime.toEpochMilli(), olderDateTimePrice)

    val menuItem = menuItemQueries.selectAll().executeAsOne()
    assertThat(menuItem.price, equalTo(newestDateTimePrice))
  }
}