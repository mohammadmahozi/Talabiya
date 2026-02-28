package com.mahozi.sayed.talabiya.order.details.suborder

import com.mahozi.sayed.talabiya.core.Money

data class OrderItem(
  val id: Long,
  val menuItemId: Long,
  val name: String,
  val quantity: Int,
  val total: Money
)
