package com.mahozi.sayed.talabiya.order.details.tabs

import androidx.compose.runtime.Composable
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
    return OrderDetailsState(
      OrderInfoState(
        LocalDate.now(),
        LocalTime.now(),
        60.0,
        "mmm",
        OrderStatus.COMPLETE,
        "Note"
      )
    )
  }

  @AssistedFactory
  interface OrderDetailsPresenterAssistedFactory {
    fun create(orderId: Int): OrderDetailsPresenter
  }
}