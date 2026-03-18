package com.mahozi.talabiya.database

import com.mahozi.sayed.talabiya.core.money
import com.mahozi.talabiya.TalabiyaDatabase
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class TestOrder(
  val date: LocalDate,
  val time: LocalTime,
)

fun TalabiyaDatabase.seedOrder(
  clock: Clock = com.mahozi.talabiya.clock,
  date: LocalDate = LocalDate.now(clock),
  time: LocalTime = LocalTime.now(clock),
  invoice: String? = null,
  note: String = ""
) {
  fun Any.id() = orderQueries.lastInsertRowId().executeAsOne()

  val user = userQueries.insert("User").id()
  val restaurant = restaurantQueries.insert("Restaurant").id()
  val menuItem = menuItemQueries.insert(restaurantId = restaurant, name = "Item", category = "Pastry").id()
  menuItemQueries.insertPrice(
    menuItemId = menuItem,
    datetime = Instant.now(clock),
    price = 10.money.toCents()
  )
  val order = orderQueries.insert(
    restaurantId = restaurant,
    createdAt = ZonedDateTime.of(
      /* date = */ date,
      /* time = */ time,
      /* zone = */ ZoneId.systemDefault()
    ).toInstant()
      .truncatedTo(ChronoUnit.SECONDS)
  ).id()

  orderQueries.updateAttachment(id = order, attachment = invoice)
  orderQueries.updateNote(id = order, note = note)
  val orderItem = orderQueries.insertOrderItemPrice(
    orderId = order,
    menuItemId = menuItem,
    price = 10.money.toCents(),
    datetime = Instant.now(clock)
  ).id()
  orderQueries.insertOrderItem(customerId = user, quantity = 5, orderItemPriceId = orderItem, note = "")
}