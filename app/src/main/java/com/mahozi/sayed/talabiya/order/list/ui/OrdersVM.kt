package com.mahozi.sayed.talabiya.order.list.ui

import com.mahozi.sayed.talabiya.core.ViewModel
import com.mahozi.sayed.talabiya.order.store.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class OrdersVM(
    private val ordersRepository: OrderRepository,
    private val scope: CoroutineScope
): ViewModel<OrdersModel, OrdersEvent>() {

    override fun start(): StateFlow<OrdersModel> {
        events
            .onEach {
                when(it) {
                    OrdersEvent.CreateOrderClicked -> TODO()
                    is OrdersEvent.DeleteOrderClicked -> TODO()
                    is OrdersEvent.OrderClicked -> TODO()
                }
            }.launchIn(scope)

        val orders = ordersRepository.selectAllOrders()

        return orders
            .map { OrdersModel(it) }
            .stateIn(scope, SharingStarted.WhileSubscribed(), OrdersModel(emptyList()))
    }
}