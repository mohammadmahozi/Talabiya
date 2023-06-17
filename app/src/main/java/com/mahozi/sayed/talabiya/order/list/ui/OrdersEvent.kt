package com.mahozi.sayed.talabiya.order.list.ui

import com.mahozi.sayed.talabiya.order.list.Order

sealed interface OrdersEvent {
    object CreateOrderClicked: OrdersEvent
    data class OrderClicked(val order: Order): OrdersEvent
    data class DeleteOrderClicked(val order: Order): OrdersEvent
}
    