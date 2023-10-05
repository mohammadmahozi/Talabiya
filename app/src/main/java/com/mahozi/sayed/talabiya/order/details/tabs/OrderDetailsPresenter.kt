package com.mahozi.sayed.talabiya.order.details.tabs

import androidx.compose.runtime.*
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.store.OrderStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class OrderDetailsPresenter @AssistedInject constructor(
  @Assisted private val orderId: Long,
  private val orderStore: OrderStore,
) : Presenter<OrderDetailsEvent, OrderDetailsState> {

  @Composable
  override fun start(events: Flow<OrderDetailsEvent>): OrderDetailsState {
    val orderState by remember { orderStore.getOrderDetails(orderId) }.collectAsState(initial = null)

    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(events) {
      events.collect {
        when(it) {
          OrderDetailsEvent.OrderInfoEvent.AddInvoiceClicked -> TODO()
          OrderDetailsEvent.OrderInfoEvent.DateClicked -> showDatePicker = true
          OrderDetailsEvent.OrderInfoEvent.DateDialogDismissed -> showDatePicker = false
          is OrderDetailsEvent.OrderInfoEvent.DateSelected -> {}
          OrderDetailsEvent.OrderInfoEvent.InvoiceClicked -> TODO()
          is OrderDetailsEvent.OrderInfoEvent.NoteChanged -> TODO()
          OrderDetailsEvent.OrderInfoEvent.PayerClicked -> TODO()
          OrderDetailsEvent.OrderInfoEvent.StatusClicked -> TODO()
          OrderDetailsEvent.OrderInfoEvent.TimeClicked -> TODO()

        }
      }
    }

    val order = orderState
    return when (order) {
      null -> OrderDetailsState(null)
      else -> OrderDetailsState(
        OrderInfoState(
          order.createdAt,
          order.total,
          order.payer,
          OrderStatus.COMPLETE,
          order.note,
          showDatePicker
        )
      )
    }
  }

  @AssistedFactory
  interface OrderDetailsPresenterAssistedFactory {
    fun create(orderId: Long): OrderDetailsPresenter
  }
}