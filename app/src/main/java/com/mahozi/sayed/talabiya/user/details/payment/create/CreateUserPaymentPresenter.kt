package com.mahozi.sayed.talabiya.user.details.payment.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.sumOf
import com.mahozi.sayed.talabiya.payment.PaymentStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class CreateUserPaymentPresenter @AssistedInject constructor(
  @Assisted private val screen: CreateUserPaymentScreen,
  private val paymentStore: PaymentStore,
): Presenter<CreateUserPaymentEvent, CreateUserPaymentState> {

  @Composable
  override fun start(events: Flow<CreateUserPaymentEvent>): CreateUserPaymentState {

    var orders by remember { mutableStateOf(listOf<UnpaidOrder>()) }
    var summary by remember { mutableStateOf(null as PaymentSummary?) }
    var totals by remember { mutableStateOf(null as PaymentTotals?) }

    LaunchedEffect(Unit) {
      orders = paymentStore.getUnpaidOrders(screen.userId)
      val placedOrders = orders.filter { it.userOrderTotal > 0.money  }
      val placedOrdersTotal = placedOrders.sumOf { it.userOrderTotal }

      val coveredOrders = orders.filter { it.fullOrderTotal > 0.money }
      val coveredOrdersTotal = coveredOrders.sumOf { it.fullOrderTotal }

      if (orders.isNotEmpty()) {
        summary = PaymentSummary(
          from = orders.first().createdAt,
          to = orders.last().createdAt,
          ordersPlaced = placedOrders.size,
          placedOrdersTotal = placedOrdersTotal,
          coveredOrders = coveredOrders.size,
          coveredOrdersTotal = coveredOrdersTotal,
        )
        totals = PaymentTotals(
          coveredOrdersTotal = coveredOrdersTotal,
          placedOrdersTotal = placedOrdersTotal,
          finalTotal = coveredOrdersTotal - placedOrdersTotal,
        )
      }
    }

    return CreateUserPaymentState(
      user = "",
      summary = summary,
      totals = totals,
    )
  }
}