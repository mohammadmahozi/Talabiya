package com.mahozi.sayed.talabiya.user.details.payment.create

import com.mahozi.sayed.talabiya.core.Money
import java.time.Instant

data class CreateUserPaymentState(
  val user: String,
  val numberOfSelectedOrders: Int,
  val allOrdersSelected: Boolean,
  val summary: PaymentSummary,
  val totals: PaymentTotals,
  val showPay: Boolean,
)

data class PaymentSummary(
  val from: Instant,
  val to: Instant,
  val ordersPlaced: Int,
  val ordersCovered: Int,
)

data class PaymentTotals(
  val placedOrdersTotal: Money,
  val coveredOrdersTotal: Money,
  val finalTotal: Money,
)

sealed interface CreateUserPaymentEvent {
  object SelectOrders : CreateUserPaymentEvent
  object Pay : CreateUserPaymentEvent
}
