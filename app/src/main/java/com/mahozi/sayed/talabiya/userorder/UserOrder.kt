package com.mahozi.sayed.talabiya.userorder

import com.mahozi.sayed.talabiya.core.Money
import java.time.Instant

data class UserOrder(
  val orderId: Long,
  val userId: Long,
  val user: String,
  val restaurant: String,
  val createdAt: Instant,
  val total: Money,
)
