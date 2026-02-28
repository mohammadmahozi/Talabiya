package com.mahozi.sayed.talabiya.user.details.payment.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.sumOf
import com.mahozi.sayed.talabiya.payment.PaymentStore
import com.mahozi.sayed.talabiya.user.details.order.list.SelectUnpaidOrderState
import com.mahozi.sayed.talabiya.user.details.order.list.SelectUnpaidOrdersEvent
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant

class CreateUserPaymentPresenter @AssistedInject constructor(
  @Assisted private val userId: Long,
  private val paymentStore: PaymentStore,
  private val clock: Clock,
) : Presenter<CreateUserPaymentEvent, CreateUserPaymentState> {

  @Composable
  override fun start(events: Flow<CreateUserPaymentEvent>): CreateUserPaymentState {
    var showSelectOrders by remember { mutableStateOf(false) }
    val ordersState by orders()

    val summary = remember(ordersState) { calculateSummary(ordersState.selectedOrders) }
    val totals by remember { derivedStateOf { calculateTotals(ordersState.selectedOrders) } }

    CollectEvents(events) { event ->
      when (event) {
        CreateUserPaymentEvent.SelectOrders -> showSelectOrders = true
        is CreateUserPaymentEvent.UnpaidOrder -> {
          when (event.event) {
            SelectUnpaidOrdersEvent.Dismiss -> showSelectOrders = false
            is SelectUnpaidOrdersEvent.SelectOrder -> {
              ordersState.onToggleOrder(event.event.order.orderId)
            }
          }
        }
        CreateUserPaymentEvent.Pay -> {
          val selectedOrders = ordersState.selectedOrders
          launch {
            paymentStore.createPayment(
              userId = userId,
              orders = selectedOrders,
              amount = totals.finalTotal,
            )
          }
        }
      }
    }

    return CreateUserPaymentState(
      user = "",
      numberOfSelectedOrders = ordersState.selectedOrders.size,
      allOrdersSelected = ordersState.orders.all { it.selected },
      summary = summary,
      totals = totals,
      showPay = ordersState.selectedOrders.isNotEmpty(),
      selectUnpaidOrderState = when (showSelectOrders) {
        true -> SelectUnpaidOrderState(ordersState.orders)
        false -> null
      },
    )
  }

  @Composable
  private fun orders(): State<OrdersState> {
    val orders by paymentStore.getUnpaidOrders(userId).collectAsState(listOf())
    var selectedOrdersIds by remember { mutableStateOf(setOf<Long>()) }

    LaunchedEffect(orders) {
      if (selectedOrdersIds.isEmpty()) {
        selectedOrdersIds = orders.map { it.orderId }.toSet()
      }
    }

    return remember {
      derivedStateOf {
        val uiOrders = orders.map { order ->
          order.copy(selected = selectedOrdersIds.contains(order.orderId))
        }
        OrdersState(
          orders = uiOrders,
          selectedOrderIds = selectedOrdersIds,
          selectedOrders = uiOrders.filter { it.selected },
          onToggleOrder = { id ->
            selectedOrdersIds = if (id in selectedOrdersIds) {
              selectedOrdersIds - id
            } else {
              selectedOrdersIds + id
            }
          },
        )
      }
    }
  }

  private fun calculateSummary(selectedOrders: List<UnpaidOrder>) = PaymentSummary(
    from = selectedOrders.firstOrNull()?.createdAt ?: Instant.now(clock),
    to = selectedOrders.lastOrNull()?.createdAt ?: Instant.now(clock),
    ordersPlaced = selectedOrders.count { it.userOrderTotal > 0.money },
    ordersCovered = selectedOrders.count { it.fullOrderTotal > 0.money },
  )

  private fun calculateTotals(selectedOrders: List<UnpaidOrder>) = PaymentTotals(
    coveredOrdersTotal = selectedOrders.sumOf { it.fullOrderTotal },
    placedOrdersTotal = selectedOrders.sumOf { it.userOrderTotal },
    finalTotal = selectedOrders.sumOf { it.fullOrderTotal - it.userOrderTotal },
  )

  @AssistedFactory
  interface Factory {
    fun create(userId: Long): CreateUserPaymentPresenter
  }
}

private data class OrdersState(
  val orders: List<UnpaidOrder>,
  val selectedOrders: List<UnpaidOrder>,
  val selectedOrderIds: Set<Long>,
  val onToggleOrder: (Long) -> Unit,
)