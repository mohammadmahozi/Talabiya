package com.mahozi.sayed.talabiya.order.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mahozi.sayed.talabiya.core.ViewModel
import com.mahozi.sayed.talabiya.order.store.OrderRepository
import kotlinx.coroutines.CoroutineScope

class OrdersVM(
    private val ordersRepository: OrderRepository,
    private val scope: CoroutineScope
): ViewModel<OrdersModel, OrdersEvent>() {

    @Composable override fun start(): OrdersModel {
        LaunchedEffect(Unit) {
            events.collect {
                when(it) {
                    OrdersEvent.CreateOrderClicked -> TODO()
                    is OrdersEvent.DeleteOrderClicked -> TODO()
                    is OrdersEvent.OrderClicked -> TODO()
                }
           }
        }

        val orders by ordersRepository.selectAllOrders().collectAsState(initial = emptyList())
        return  OrdersModel(orders)
    }
}