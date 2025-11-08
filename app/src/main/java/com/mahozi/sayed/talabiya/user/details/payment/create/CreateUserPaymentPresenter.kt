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
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import java.time.Instant

class CreateUserPaymentPresenter @AssistedInject constructor(
  @Assisted private val screen: CreateUserPaymentScreen,
  private val paymentStore: PaymentStore,
) : Presenter<CreateUserPaymentEvent, CreateUserPaymentState> {

  @Composable
  override fun start(events: Flow<CreateUserPaymentEvent>): CreateUserPaymentState {

    var orders by remember { mutableStateOf(listOf<UnpaidOrder>()) }
    var summary by remember {
      mutableStateOf(
        PaymentSummary(
          from = Instant.now(),
          to = Instant.now(),
          ordersPlaced = 0,
          ordersCovered = 0,
        )
      )
    }
    var totals by remember {
      mutableStateOf(
        PaymentTotals(
          coveredOrdersTotal = 0.money,
          placedOrdersTotal = 0.money,
          finalTotal = 0.money,
        )
      )
    }

    LaunchedEffect(Unit) {
      orders = paymentStore.getUnpaidOrders(screen.userId)
      val selectedOrders = orders.selected
      val placedOrders = selectedOrders.filter { it.userOrderTotal > 0.money }
      val placedOrdersTotal = placedOrders.sumOf { it.userOrderTotal }

      val coveredOrders = selectedOrders.filter { it.fullOrderTotal > 0.money }
      val coveredOrdersTotal = coveredOrders.sumOf { it.fullOrderTotal }

      if (selectedOrders.isNotEmpty()) {
        summary = PaymentSummary(
          from = selectedOrders.first().createdAt,
          to = selectedOrders.last().createdAt,
          ordersPlaced = placedOrders.size,
          ordersCovered = coveredOrders.size,
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
      numberOfSelectedOrders = orders.selected.size,
      allOrdersSelected = orders.all { it.selected },
      summary = summary,
      totals = totals,
      showPay = orders.any { it.selected }
    )
  }

  private val List<UnpaidOrder>.selected get() = filter { it.selected }

  @AssistedFactory
  interface Factory {
    fun create(screen: CreateUserPaymentScreen): CreateUserPaymentPresenter
  }
}