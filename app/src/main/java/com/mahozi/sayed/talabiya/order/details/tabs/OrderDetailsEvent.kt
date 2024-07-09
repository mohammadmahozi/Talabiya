package com.mahozi.sayed.talabiya.order.details.tabs

import com.mahozi.sayed.talabiya.order.details.suborder.Suborder
import user.UserEntity
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

    sealed interface SuborderEvent: OrderDetailsEvent {
        data class UserClicked(val user: UserEntity): SuborderEvent
        data class EditSuborderClicked(val suborder: Suborder) : SuborderEvent
    }
}