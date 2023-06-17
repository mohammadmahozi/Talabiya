package com.mahozi.sayed.talabiya.order.list.ui

import androidx.compose.runtime.*
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.order.create.CreateOrderScreen
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsScreen
import com.mahozi.sayed.talabiya.order.store.OrderStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class OrdersPresenter @Inject constructor(
  private val orderStore: OrderStore,
  private val navigator: Navigator,
) : Presenter<OrdersEvent, OrdersState> {


  @Composable
  override fun start(events: Flow<OrdersEvent>): OrdersState {
    val orders by remember { orderStore.orders }.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
      launch {
        events.collect {
          when (it) {
            OrdersEvent.CreateOrderClicked -> {
              navigator.goto(CreateOrderScreen)
            }
            is OrdersEvent.DeleteOrderClicked -> TODO()
            is OrdersEvent.OrderClicked -> {
              navigator.goto(OrderDetailsScreen(it.order.id))
            }
          }
        }
      }
    }

    return OrdersState(orders)
  }
}

