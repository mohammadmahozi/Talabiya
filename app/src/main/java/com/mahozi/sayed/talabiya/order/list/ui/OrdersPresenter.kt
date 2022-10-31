package com.mahozi.sayed.talabiya.order.list.ui

import androidx.compose.runtime.*
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsScreen
import com.mahozi.sayed.talabiya.order.store.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class OrdersPresenter @Inject constructor(
    private val ordersRepository: OrderRepository,
    private val navigator: Navigator,
) : Presenter<OrdersEvent, OrdersState> {

    @Composable override fun start(events: Flow<OrdersEvent>): OrdersState {
        val orders by remember { ordersRepository.selectAllOrders() }.collectAsState(initial = emptyList())

        LaunchedEffect(Unit) {
            launch {
                events.collect {
                    when (it) {
                        OrdersEvent.CreateOrderClicked -> TODO()
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

