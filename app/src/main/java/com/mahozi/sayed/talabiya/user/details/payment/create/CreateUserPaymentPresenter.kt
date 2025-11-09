package com.mahozi.sayed.talabiya.user.details.payment.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.sumOf
import com.mahozi.sayed.talabiya.payment.PaymentStore
import com.mahozi.sayed.talabiya.user.details.order.list.SelectUnpaidOrdersEvent
import com.mahozi.sayed.talabiya.user.details.order.list.SelectUnpaidOrderState
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
    LaunchedEffect(Unit) {
      orders = paymentStore.getUnpaidOrders(screen.userId)
    }

    var summary by remember(orders) {
      val selectedOrders = orders.selected
      val placedOrders = selectedOrders.filter { it.userOrderTotal > 0.money }
      val coveredOrders = selectedOrders.filter { it.fullOrderTotal > 0.money }
      mutableStateOf(
        PaymentSummary(
          from = selectedOrders.firstOrNull()?.createdAt ?: Instant.now(),
          to = selectedOrders.lastOrNull()?.createdAt ?: Instant.now(),
          ordersPlaced = placedOrders.size,
          ordersCovered = coveredOrders.size,
        )
      )
    }
    var totals by remember(orders) {
      val selectedOrders = orders.selected
      val placedOrdersTotal = selectedOrders.sumOf { it.userOrderTotal }
      val coveredOrdersTotal = selectedOrders.sumOf { it.fullOrderTotal }
      mutableStateOf(
        PaymentTotals(
          coveredOrdersTotal = coveredOrdersTotal,
          placedOrdersTotal = placedOrdersTotal,
          finalTotal = coveredOrdersTotal - placedOrdersTotal,
        )
      )
    }
    var showSelectOrders by remember { mutableStateOf(false) }

    CollectEvents(events) { event ->
      when (event) {
        CreateUserPaymentEvent.SelectOrders -> showSelectOrders = true
        CreateUserPaymentEvent.Pay -> {}
        is CreateUserPaymentEvent.UnpaidOrder -> {
          when (event.event) {
            SelectUnpaidOrdersEvent.Dismiss -> showSelectOrders = false
            is SelectUnpaidOrdersEvent.SelectOrder -> {
              orders = orders.map {
                if (it.orderId == event.event.order.orderId) {
                  it.copy(selected = !it.selected)
                } else {
                  it
                }
              }
            }
          }
        }
      }
    }
    return CreateUserPaymentState(
      user = "",
      numberOfSelectedOrders = orders.selected.size,
      allOrdersSelected = orders.all { it.selected },
      summary = summary,
      totals = totals,
      showPay = orders.any { it.selected },
      selectUnpaidOrderState = when (showSelectOrders) {
        true -> SelectUnpaidOrderState(orders)
        false -> null
      },
    )
  }

  private val List<UnpaidOrder>.selected get() = filter { it.selected }

  @AssistedFactory
  interface Factory {
    fun create(screen: CreateUserPaymentScreen): CreateUserPaymentPresenter
  }
}