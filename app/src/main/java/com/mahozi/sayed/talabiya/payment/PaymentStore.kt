package com.mahozi.sayed.talabiya.payment

import com.mahozi.sayed.talabiya.core.Money
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import payment.PaymentQueries
import java.time.Instant
import javax.inject.Inject

class PaymentStore @Inject constructor(
  private val paymentQueries: PaymentQueries,
  private val dispatcher: CoroutineDispatcher,
) {

  suspend fun createUserOrderPayment(
    orderId: Long,
    userId: Long,
    amount: Money,
  ) {
    withContext(dispatcher) {
      paymentQueries.transaction {
        paymentQueries.insertPayment(
          userId,
          amount = amount.toLong(),
          createdAt = Instant.now(),
          status = PaymentStatus.Completed.name
        )
        val paymentId = paymentQueries.lastInsertRowId().executeAsOne()
        paymentQueries.insertUserOrderPayment(
          paymentId = paymentId,
          orderId = orderId
        )
      }
    }
  }

  suspend fun createReimbursementPayment(
    orderId: Long,
    userId: Long,
    amount: Money,
  ) {
    withContext(dispatcher) {
      paymentQueries.transaction {
        paymentQueries.insertPayment(
          userId,
          amount = amount.toLong(),
          createdAt = Instant.now(),
          status = PaymentStatus.Completed.name
        )
        val paymentId = paymentQueries.lastInsertRowId().executeAsOne()
        paymentQueries.insertOrderReimbursement(
          paymentId = paymentId,
          orderId = orderId
        )
      }
    }
  }
}