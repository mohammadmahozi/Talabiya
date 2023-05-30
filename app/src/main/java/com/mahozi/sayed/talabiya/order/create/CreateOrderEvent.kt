package com.mahozi.sayed.talabiya.order.create

import restaurant.RestaurantEntity
import java.time.LocalDate
import java.time.LocalTime

sealed interface CreateOrderEvent {
  data class RestaurantSelected(val restaurant: RestaurantEntity): CreateOrderEvent
  data class DateSelected(val date: LocalDate): CreateOrderEvent
  data class TimeSelected(val time: LocalTime): CreateOrderEvent
}