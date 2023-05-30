package com.mahozi.sayed.talabiya.order.create

import restaurant.RestaurantEntity
import java.time.LocalDate
import java.time.LocalTime

data class CreateOrderState(
  val selectedRestaurant: RestaurantEntity?,
  val restaurants: List<RestaurantEntity>,
  val date: LocalDate,
  val time: LocalTime
)
