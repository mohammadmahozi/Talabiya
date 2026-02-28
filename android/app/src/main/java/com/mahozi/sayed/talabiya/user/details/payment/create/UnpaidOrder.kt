package com.mahozi.sayed.talabiya.user.details.payment.create

import com.mahozi.sayed.talabiya.core.Money
import java.time.Instant

data class UnpaidOrder(
  val orderId: Long,
  val createdAt: Instant,
  val restaurant: String,
  val fullOrderTotal: Money,
  val userOrderTotal: Money,
  val selected: Boolean,
)
