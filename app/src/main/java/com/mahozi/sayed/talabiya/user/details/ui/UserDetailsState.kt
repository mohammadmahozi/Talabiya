package com.mahozi.sayed.talabiya.user.details.ui

import com.mahozi.sayed.talabiya.payment.Payment
import com.mahozi.sayed.talabiya.user.details.payment.create.CreateUserPaymentEvent
import com.mahozi.sayed.talabiya.user.details.payment.create.CreateUserPaymentState
import com.mahozi.sayed.talabiya.userorder.UserOrder

data class UserDetailsState(
  val userName: String,
  val tab: UserDetailsTab,
  val createUserPaymentState: CreateUserPaymentState,
  val payments: List<Payment>,
  val orders: List<UserOrder>
)

enum class UserDetailsTab {
  CreatePayment,
  Payments,
  Orders
}

sealed interface UserDetailsEvent {
  data class SelectTab(val tab: UserDetailsTab): UserDetailsEvent
  data class CreatePaymentEvent(val event: CreateUserPaymentEvent): UserDetailsEvent
}


