package com.mahozi.sayed.talabiya.order.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import com.mahozi.sayed.talabiya.core.ViewModel
import com.mahozi.sayed.talabiya.order.OrderRoute
import com.mahozi.sayed.talabiya.order.store.OrderRepository
import kotlinx.coroutines.CoroutineScope

class OrdersVM(
    private val ordersRepository: OrderRepository,
    private val scope: CoroutineScope,
    private val backStack: BackStack<OrderRoute>
): ViewModel<OrdersModel, OrdersEvent>() {

    @Composable override fun start(): OrdersModel {
        LaunchedEffect(Unit) {
            events.collect {
                when(it) {
                    OrdersEvent.CreateOrderClicked -> TODO()
                    is OrdersEvent.DeleteOrderClicked -> TODO()
                    is OrdersEvent.OrderClicked -> backStack.push(OrderRoute.OrderDetails(it.order.id))
                }
           }
        }

        val orders by ordersRepository.selectAllOrders().collectAsState(initial = emptyList())
        return  OrdersModel(orders)
    }
}