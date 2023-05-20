package com.mahozi.sayed.talabiya.order.details.tabs

import java.time.LocalDate

sealed interface OrderDetailsEvent {
    sealed interface OrderInfoEvent: OrderDetailsEvent {
        object DateClicked: OrderInfoEvent
        data class DateSelected(val date: LocalDate): OrderInfoEvent
        object DateDialogDismissed: OrderInfoEvent
        object TimeClicked: OrderInfoEvent
        object InvoiceClicked: OrderInfoEvent
        object AddInvoiceClicked: OrderInfoEvent
        object PayerClicked: OrderInfoEvent
        object StatusClicked: OrderInfoEvent
        data class NoteChanged(val note: String): OrderInfoEvent
    }
}