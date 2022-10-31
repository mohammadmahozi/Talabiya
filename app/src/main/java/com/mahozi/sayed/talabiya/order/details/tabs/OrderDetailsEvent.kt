package com.mahozi.sayed.talabiya.order.details.tabs

sealed interface OrderDetailsEvent {
    sealed interface OrderInfoEvent: OrderDetailsEvent {
        object DateClicked: OrderDetailsEvent
        object TimeClicked: OrderDetailsEvent
        object InvoiceClicked: OrderDetailsEvent
        object AddInvoiceClicked: OrderDetailsEvent
        object PayerClicked: OrderDetailsEvent
        object StatusClicked: OrderDetailsEvent
        data class NoteChanged(val note: String): OrderDetailsEvent
    }
}