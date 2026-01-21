package com.mahozi.sayed.talabiya.order.details

import androidx.compose.runtime.*
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.core.ui.components.TlbDatePickerEvent
import com.mahozi.sayed.talabiya.core.ui.components.TlbDatePickerState
import com.mahozi.sayed.talabiya.core.ui.components.TlbTimePickerEvent
import com.mahozi.sayed.talabiya.core.ui.components.TlbTimePickerState
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.details.OrderDetailsEvent.OrderInfoEvent
import com.mahozi.sayed.talabiya.order.details.edit.EditOrderPricesScreen
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.order.suborder.CreateSuborderScreen
import com.mahozi.sayed.talabiya.user.data.UserStore
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Optional

class OrderDetailsPresenter @AssistedInject constructor(
  @Assisted private val orderId: Long,
  private val orderStore: OrderStore,
  private val userStore: UserStore,
  private val navigator: Navigator,
) : Presenter<OrderDetailsEvent, OrderDetailsState> {

  @Composable
  override fun start(events: Flow<OrderDetailsEvent>): OrderDetailsState {
    val info by remember { orderStore.getOrderDetails(orderId) }.collectAsState(initial = null)
    val suborders by remember { orderStore.getSuborders(orderId) }.collectAsState(initial = emptyList())
    val fullOrderItems by remember { orderStore.getFullOrderItems(orderId) }.collectAsState(initial = emptyList())
    val users by remember { userStore.users }.collectAsState(initial = emptyList())

    var note by remember { mutableStateOf("") }
    LaunchedEffect(info) {
      note = info?.note ?: ""
    }

    var datePickerState by remember { mutableStateOf(null as TlbDatePickerState?) }
    var timePickerState by remember { mutableStateOf(null as TlbTimePickerState?) }

    CollectEvents(events) { event ->
      when (event) {
        OrderInfoEvent.DateClicked -> {
          datePickerState = TlbDatePickerState(
            initial = LocalDate.now(),
          )
        }
        is OrderInfoEvent.DateEvent -> when (event.event) {
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
        OrderInfoEvent.TimeClicked -> {
          timePickerState = TlbTimePickerState(LocalTime.now())
        }
        is OrderInfoEvent.TimeEvent -> when (event.event) {
          is TlbTimePickerEvent.SelectTime -> {
            launch {
              orderStore.updateOrder(
                orderId = orderId,
                time = event.event.time,
              )
              timePickerState = null
            }
          }
          TlbTimePickerEvent.Dismiss -> timePickerState = null
        }
        is OrderInfoEvent.ChangeInvoice -> {
          launch {
            orderStore.updateOrder(
              orderId = orderId,
              invoice = Optional.of(event.invoice.toString())
            )
          }
        }
        OrderInfoEvent.PayerClicked -> { /*TODO */ }
        OrderInfoEvent.StatusClicked -> { /*TODO */ }
        is OrderInfoEvent.NoteChanged -> {
          note = event.note
          launch {
            orderStore.updateOrder(
              orderId = orderId,
              note = event.note,
            )
          }
        }
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

    val order = info
    return when (order) {
      null -> OrderDetailsState(null, null, listOf())
      else -> OrderDetailsState(
        info = OrderInfoState(
          datetime = order.createdAt,
          total = order.total,
          invoice = order.invoice,
          payer = order.payer,
          status = OrderStatus.COMPLETE,
          note = note,
          datePickerState = datePickerState,
          timePickerState = timePickerState,
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