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
  userQueries.insert("User")
  restaurantQueries.insert("Restaurant")
  menuItemQueries.insert(restaurantId = 1, name = "Item", category = "Pastry")
  menuItemQueries.insertPrice(1, Instant.now(clock), 10.money.toCents())
  orderQueries.insert(
    restaurantId = 1,
    createdAt = ZonedDateTime.of(
      /* date = */ date,
      /* time = */ time,
      /* zone = */ ZoneId.systemDefault()
    ).toInstant()
      .truncatedTo(ChronoUnit.SECONDS)
  )
  orderQueries.updateAttachment(id = 1, attachment = invoice)
  orderQueries.updateNote(id = 1, note = note)
  orderQueries.insertOrderItemPrice(
    orderId = 1,
    menuItemId = 1,
    price = 10.money.toCents(),
    datetime = Instant.now(clock)
  )
  orderQueries.insertOrderItem(customerId = 1, quantity = 5, orderItemPriceId = 1)
}