package com.mahozi.sayed.talabiya.order.list.ui

import com.mahozi.sayed.talabiya.order.store.OrderEntity

sealed interface OrdersEvent {
    object CreateOrderClicked: OrdersEvent
    data class OrderClicked(val order: OrderEntity): OrdersEvent
    data class DeleteOrderClicked(val order: OrderEntity): OrdersEvent
}
    