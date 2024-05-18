package com.mahozi.sayed.talabiya.resturant.option


data class FoodOption(
  val id: Long,
  val name: String,
  val categories: List<Category>,
) {
  data class Category(
    val name: String,
    val selected: Boolean
  )
}
