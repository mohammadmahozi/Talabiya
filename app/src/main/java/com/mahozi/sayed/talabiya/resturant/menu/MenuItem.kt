package com.mahozi.sayed.talabiya.resturant.menu

import com.mahozi.sayed.talabiya.core.Money

data class MenuItem(
  val id: Long,
  val name: String,
  val category: String,
  val price: Money,
)
