package com.mahozi.talabiya

import com.mahozi.talabiya.database.createDatabase
import org.junit.Before
import org.junit.Test
import java.time.Instant

class RestaurantQueriesTest {

  private lateinit var database: TalabiyaDatabase
  private val restaurantQueries get() = database.restaurantQueries
  private val menuItemQueries get() = database.menuItemQueries

  @Before fun before() {
    database = createDatabase()
  }

  @Test fun menuItemsHaveTheMostRecentPrice() {
    restaurantQueries.insert("Restaurant")
    val restaurant = restaurantQueries.selectAll().executeAsOne()
    menuItemQueries.insert(restaurant.id, "item", "Cateory")

    val newestDateTime = Instant.now()
    val newestDateTimePrice = 1000L

    val olderDateTime = Instant.now().minusSeconds(1)
    val olderDateTimePrice = 900L
//
//    menuItemQueries.insertPrice(1, newestDateTime.toEpochMilli(), newestDateTimePrice)
//    menuItemQueries.insertPrice(1, olderDateTime.toEpochMilli(), olderDateTimePrice)
//
//    val menuItem = menuItemQueries.selectAll().executeAsOne()
//    assertThat(menuItem.price, equalTo(newestDateTimePrice))
  }
}