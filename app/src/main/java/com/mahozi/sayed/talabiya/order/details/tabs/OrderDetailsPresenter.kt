package com.mahozi.sayed.talabiya.order.details.tabs

import androidx.compose.runtime.*
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.user.data.UserStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class OrderDetailsPresenter @AssistedInject constructor(
  @Assisted private val orderId: Long,
  private val orderStore: OrderStore,
  private val userStore: UserStore,
) : Presenter<OrderDetailsEvent, OrderDetailsState> {

  @Composable
  override fun start(events: Flow<OrderDetailsEvent>): OrderDetailsState {
    val orderState by remember { orderStore.getOrderDetails(orderId) }.collectAsState(initial = null)
    val suborders by remember { orderStore.getSuborders(orderId) }.collectAsState(initial = emptyList())
    val users by remember { userStore.users }.collectAsState(initial = emptyList())

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
          is OrderDetailsEvent.SuborderEvent.UserClicked -> TODO()
        }
      }
    }


    val order = orderState
    return when (order) {
      null -> OrderDetailsState(null, null)
      else -> OrderDetailsState(
        OrderInfoState(
          order.createdAt,
          order.total,
          order.payer,
          OrderStatus.COMPLETE,
          order.note,
          showDatePicker
        ),
        SubordersState(suborders, users)
      )
    }
  }

  @AssistedFactory
  interface OrderDetailsPresenterAssistedFactory {
    fun create(orderId: Long): OrderDetailsPresenter
  }
}