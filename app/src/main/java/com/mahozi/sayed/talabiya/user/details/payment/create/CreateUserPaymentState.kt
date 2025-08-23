package com.mahozi.sayed.talabiya.user.details.payment.create

import com.mahozi.sayed.talabiya.core.Money
import java.time.Instant

data class CreateUserPaymentState(
  val user: String,
  val summary: PaymentSummary?,
  val totals: PaymentTotals?,
)

data class PaymentSummary(
  val from: Instant,
  val to: Instant,
  val ordersPlaced: Int,
  val placedOrdersTotal: Money,
  val coveredOrders: Int,
  val coveredOrdersTotal: Money,
)

data class PaymentTotals(
  val coveredOrdersTotal: Money,
  val placedOrdersTotal: Money,
  val finalTotal: Money,
)

sealed interface CreateUserPaymentEvent {
  object SelectOrdersClicked : CreateUserPaymentEvent
  object PayClicked : CreateUserPaymentEvent
}
