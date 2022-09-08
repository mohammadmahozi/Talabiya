package com.mahozi.sayed.talabiya.order.list.ui

import com.mahozi.sayed.talabiya.core.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrdersVM(

): ViewModel<OrdersModel, OrdersEvent>() {

    override fun start(): StateFlow<OrdersModel> {

        return MutableStateFlow(OrdersModel(emptyList()))
    }
}