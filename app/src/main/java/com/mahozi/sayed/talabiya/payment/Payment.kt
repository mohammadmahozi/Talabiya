package com.mahozi.sayed.talabiya.payment

import com.mahozi.sayed.talabiya.core.Money
import java.time.Instant

data class Payment(
  val id: Long,
  val amount: Money,
  val createdAt: Instant,
  val status: PaymentStatus,
  val userId: Long,
)
