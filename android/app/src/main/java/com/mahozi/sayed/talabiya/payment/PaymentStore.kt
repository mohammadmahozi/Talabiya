package com.mahozi.sayed.talabiya.payment

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.core.cents
import com.mahozi.sayed.talabiya.core.sumOf
import com.mahozi.sayed.talabiya.user.details.payment.create.UnpaidOrder
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import payment.PaymentQueries
import java.time.Instant

class PaymentStore @Inject constructor(
  private val paymentQueries: PaymentQueries,
  private val dispatcher: CoroutineDispatcher,
) {

  fun getUserPayments(userId: Long): Flow<List<Payment>> {
    return paymentQueries
      .userPayments(
        userId = userId,
        mapper = { id, amount, createdAt, status, direction ->
          Payment(
            id = id,
            amount = amount.cents,
            createdAt = createdAt,
            status = PaymentStatus.from(status),
            direction = PaymentDirection.from(direction)
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
        val totalPaid = orders.sumOf { it.fullOrderTotal }
        val totalOwed = orders.sumOf { it.userOrderTotal }

        val direction = if (totalPaid > totalOwed) {
          PaymentDirection.Credit
        } else {
          PaymentDirection.Debit
        }

        paymentQueries.insertPayment(
          userId,
          amount = amount.toCents(),
          createdAt = Instant.now(),
          status = PaymentStatus.Completed.name,
          direction = direction.key
        )
        val paymentId = paymentQueries.lastInsertRowId().executeAsOne()

        paymentQueries.insertInvoice(
          paymentId = paymentId,
          totalOwed = totalOwed.toCents(),
          totalPaid = totalPaid.toCents()
        )
        val invoiceId = paymentQueries.lastInsertRowId().executeAsOne()

        orders.forEach { order ->
          paymentQueries.insertInvoiceItem(
            invoiceId = invoiceId,
            orderId = order.orderId,
            amountPaid = order.fullOrderTotal.toCents(),
            amountOwed = order.userOrderTotal.toCents()
          )
        }
      }
    }
  }

  fun getUnpaidOrders(userId: Long): Flow<List<UnpaidOrder>> {
    return paymentQueries.unpaidOrderEntity(
      userId = userId,
      mapper = { orderId, createdAt, restaurant, totalPaid, totalOwed ->
        UnpaidOrder(
          orderId = orderId,
          createdAt = createdAt,
          restaurant = restaurant,
          fullOrderTotal = totalPaid.cents,
          userOrderTotal = totalOwed.cents,
          selected = true
        )
      }
    ).asFlow()
    .mapToList(dispatcher)
  }
}