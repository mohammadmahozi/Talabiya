package com.mahozi.sayed.talabiya.order.details.tabs

import androidx.compose.runtime.*
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.store.OrderStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

class OrderDetailsPresenter @AssistedInject constructor(
  @Assisted private val orderId: Int,
  private val orderStore: OrderStore,
) : Presenter<OrderDetailsEvent, OrderDetailsState> {



  @Composable
  override fun start(events: Flow<OrderDetailsEvent>): OrderDetailsState {
    val orderState by remember { orderStore.getOrder(orderId) }.collectAsState(initial = null)

    val order = orderState
    return when (order) {
      null -> OrderDetailsState(null)
      else -> OrderDetailsState(
        OrderInfoState(
          LocalDate.parse(order.date),
          LocalTime.parse(order.time),
          order.total,
          order.payer,
          OrderStatus.COMPLETE,
          order.note
        )
      )
    }
  }

  @AssistedFactory
  interface OrderDetailsPresenterAssistedFactory {
    fun create(orderId: Int): OrderDetailsPresenter
  }
}