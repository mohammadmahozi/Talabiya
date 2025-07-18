package com.mahozi.sayed.talabiya.user.details.payment.create

data class CreatePaymentState(
  val user: String,
)

sealed interface CreatePaymentEvent {
  object SelectOrdersClicked : CreatePaymentEvent
  object PayClicked : CreatePaymentEvent
}
