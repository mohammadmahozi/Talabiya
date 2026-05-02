package com.mahozi.sayed.talabiya.order.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.order.create.CreateOrderScreen
import com.mahozi.sayed.talabiya.order.details.OrderDetailsScreen
import com.mahozi.sayed.talabiya.order.store.OrderStore
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class OrdersPresenter @Inject constructor(
  private val orderStore: OrderStore,
  private val navigator: Navigator,
) : Presenter<OrdersEvent, OrdersState> {


  @Composable
  override fun start(events: Flow<OrdersEvent>): OrdersState {
    val orders by remember { orderStore.orders }.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
      launch {
        events.collect { event ->
          when (event) {
            OrdersEvent.CreateOrderClicked -> {
              navigator.goto(CreateOrderScreen)
            }
            is OrdersEvent.DeleteOrderClicked -> {
              orderStore.deleteOrder(event.order.id)
            }
            is OrdersEvent.OrderClicked -> {
              navigator.goto(OrderDetailsScreen(event.order.id))
            }
          }
        }
      }
    }

    return OrdersState(orders)
  }
}

