package com.mahozi.sayed.talabiya.order.list

import java.time.Instant

data class Order(
  val id: Long,
  val restaurant: String,
  val createdAt: Instant,
)
