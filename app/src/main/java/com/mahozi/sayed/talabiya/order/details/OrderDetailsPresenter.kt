package com.mahozi.sayed.talabiya.order.details

import androidx.compose.runtime.*
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.core.ui.components.TlbDatePickerEvent
import com.mahozi.sayed.talabiya.core.ui.components.TlbDatePickerState
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.details.edit.EditOrderPricesScreen
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.order.suborder.CreateSuborderScreen
import com.mahozi.sayed.talabiya.user.data.UserStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class OrderDetailsPresenter @AssistedInject constructor(
  @Assisted private val orderId: Long,
  private val orderStore: OrderStore,
  private val userStore: UserStore,
  private val navigator: Navigator,
) : Presenter<OrderDetailsEvent, OrderDetailsState> {

  @Composable
  override fun start(events: Flow<OrderDetailsEvent>): OrderDetailsState {
    val orderState by remember { orderStore.getOrderDetails(orderId) }.collectAsState(initial = null)
    val suborders by remember { orderStore.getSuborders(orderId) }.collectAsState(initial = emptyList())
    val fullOrderItems by remember { orderStore.getFullOrderItems(orderId) }.collectAsState(initial = emptyList())
    val users by remember { userStore.users }.collectAsState(initial = emptyList())

    var datePickerState by remember { mutableStateOf(null as TlbDatePickerState?) }

    CollectEvents(events) { event ->
      when (event) {
        OrderDetailsEvent.OrderInfoEvent.DateClicked -> {
          datePickerState = TlbDatePickerState(
            initial = LocalDate.now(),
          )
        }
        is OrderDetailsEvent.OrderInfoEvent.DateEvent -> when (event.event) {
          is TlbDatePickerEvent.SelectDate -> {
            launch {
              orderStore.updateOrder(
                orderId = orderId,
                date = event.event.date,
              )
              datePickerState = null
            }
          }
          TlbDatePickerEvent.Dismiss -> datePickerState = null
        }
        OrderDetailsEvent.OrderInfoEvent.TimeClicked -> TODO()
        OrderDetailsEvent.OrderInfoEvent.InvoiceClicked -> TODO()
        OrderDetailsEvent.OrderInfoEvent.PayerClicked -> TODO()
        OrderDetailsEvent.OrderInfoEvent.AddInvoiceClicked -> TODO()
        OrderDetailsEvent.OrderInfoEvent.StatusClicked -> TODO()
        is OrderDetailsEvent.OrderInfoEvent.NoteChanged -> TODO()
        OrderDetailsEvent.EditPricesClicked -> {
          navigator.goto(EditOrderPricesScreen(orderId))
        }
        is OrderDetailsEvent.SuborderEvent.UserClicked -> navigator.goto(
          CreateSuborderScreen(
            orderId,
            event.user.id
          )
        )
        is OrderDetailsEvent.SuborderEvent.EditSuborderClicked -> {
          navigator.goto(CreateSuborderScreen(orderId, event.suborder.userId))
        }
      }
    }

    val order = orderState
    return when (order) {
      null -> OrderDetailsState(null, null, listOf())
      else -> OrderDetailsState(
        info = OrderInfoState(
          datetime = order.createdAt,
          total = order.total,
          payer = order.payer,
          status = OrderStatus.COMPLETE,
          note = order.note,
          datePickerState = datePickerState
        ),
        subordersState = SubordersState(suborders, users),
        fullOrderItems = fullOrderItems
      )
    }
  }

  @AssistedFactory
  interface OrderDetailsPresenterAssistedFactory {
    fun create(orderId: Long): OrderDetailsPresenter
  }
}