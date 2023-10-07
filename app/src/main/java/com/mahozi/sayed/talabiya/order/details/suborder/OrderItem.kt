package com.mahozi.sayed.talabiya.order.details.suborder

import com.mahozi.sayed.talabiya.core.Money

data class OrderItem(
  val id: Long,
  val quantity: Int,
  val name: String,
  val total: Money
)
