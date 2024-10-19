package com.mahozi.sayed.talabiya.order.details.edit

import com.mahozi.sayed.talabiya.core.Money

data class PricedOrderItem(
  val id: Long,
  val name: String,
  val price: Money,
)
