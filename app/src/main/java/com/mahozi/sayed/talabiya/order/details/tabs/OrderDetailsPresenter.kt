package com.mahozi.sayed.talabiya.order.details.tabs

import androidx.compose.runtime.Composable
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.list.ui.*
import com.mahozi.sayed.talabiya.order.store.OrderRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Provider

class OrderDetailsPresenter @AssistedInject constructor(
  @Assisted private val orderId: Int,
  private val ordersRepository: OrderRepository,
  ) : Presenter<OrderDetailsEvent, OrderDetailsState> {

  @Composable
  override fun start(events: Flow<OrderDetailsEvent>): OrderDetailsState {
    return OrderDetailsState(
      OrderInfoState(
        LocalDate.now(),
        LocalTime.now(),
        60.0,
        "mmm",
        OrderStatus.COMPLETE
      )
    )
  }

  @AssistedFactory
  interface OrderDetailsPresenterAssistedFactory {
    fun create(orderId: Int): OrderDetailsPresenter
  }
}