package com.mahozi.sayed.talabiya.payment

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.core.cents
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.user.details.payment.create.UnpaidOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import payment.PaymentQueries
import java.time.Instant
import dev.zacsweers.metro.Inject

class PaymentStore @Inject constructor(
  private val paymentQueries: PaymentQueries,
  private val dispatcher: CoroutineDispatcher,
) {

  fun getUserPayments(userId: Long): Flow<List<Payment>> {
    return paymentQueries
      .selectUserPayments(
        userId = userId,
        mapper = { id, amount, createdAt, status ->
          Payment(
            id = id,
            amount = amount.cents,
            createdAt = createdAt,
            status = PaymentStatus.valueOf(status),
            userId = userId
          )
        }
      ).asFlow()
      .mapToList(dispatcher)
  }

  suspend fun createPayment(
    userId: Long,
    amount: Money,
    orders: List<UnpaidOrder>
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
        orders.forEach { order ->
          if (order.userOrderTotal > 0.money) {
            paymentQueries.insertUserOrderPayment(
              paymentId = paymentId,
              orderId = order.orderId
            )
          }
          if (order.fullOrderTotal > 0.money) {
            paymentQueries.insertOrderReimbursement(
              paymentId = paymentId,
              orderId = order.orderId
            )
          }
        }
      }
    }
  }

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
          userId = userId,
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

  suspend fun getUnpaidOrders(userId: Long): List<UnpaidOrder> {
    return withContext(dispatcher) {
      val orders = paymentQueries.unpaidOrdersEntity(
        userId = userId,
        mapper = { orderId, createdAt, restaurant, fullOrderTotal, userOrderTotal ->
          UnpaidOrder(
            orderId = orderId,
            createdAt = createdAt,
            restaurant = restaurant,
            fullOrderTotal = (fullOrderTotal ?: 0L).cents,
            userOrderTotal = (userOrderTotal ?: 0L).cents,
            selected = true
          )
        }
      ).executeAsList()
      orders
    }
  }
}