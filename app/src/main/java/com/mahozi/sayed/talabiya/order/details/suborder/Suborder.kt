package com.mahozi.sayed.talabiya.order.details.suborder

import com.mahozi.sayed.talabiya.core.Money

data class Suborder(
  val customer: String,
  val items: List<OrderItem>,
  val total: Money,
  val expanded: Boolean,
)
