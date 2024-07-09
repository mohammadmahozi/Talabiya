package com.mahozi.sayed.talabiya.order.details.suborder

import com.mahozi.sayed.talabiya.core.Money

//TODO rename to userOrder
data class Suborder(
  val id: Long,
  val userId: Long,
  val user: String,
  val items: List<OrderItem>,
  val total: Money,
  val expanded: Boolean,
)
