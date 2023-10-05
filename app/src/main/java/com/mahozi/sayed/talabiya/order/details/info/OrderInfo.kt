package com.mahozi.sayed.talabiya.order.details.info

import com.mahozi.sayed.talabiya.core.Money
import java.time.Instant

data class OrderInfo(
  val id: Long,
  val createdAt: Instant,
  val restaurant: String,
  val payer: String?,
  val total: Money,
  val note: String
)
