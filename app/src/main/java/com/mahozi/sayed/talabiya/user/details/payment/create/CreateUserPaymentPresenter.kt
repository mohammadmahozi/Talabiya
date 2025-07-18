package com.mahozi.sayed.talabiya.user.details.payment.create

import com.mahozi.sayed.talabiya.payment.PaymentStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class CreateUserPaymentPresenter @AssistedInject constructor(
  @Assisted private val screen: CreateUserPaymentScreen,
  private val paymentStore: PaymentStore,
) {

}