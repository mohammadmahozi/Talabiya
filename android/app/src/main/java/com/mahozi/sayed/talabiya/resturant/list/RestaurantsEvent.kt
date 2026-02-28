package com.mahozi.sayed.talabiya.resturant.list

import restaurant.RestaurantEntity

sealed interface RestaurantsEvent {
  object CreateRestaurantClicked: RestaurantsEvent
  data class RestaurantClicked(val restaurantEntity: RestaurantEntity): RestaurantsEvent
  data class DeleteRestaurantClicked(val restaurant: RestaurantEntity): RestaurantsEvent
}