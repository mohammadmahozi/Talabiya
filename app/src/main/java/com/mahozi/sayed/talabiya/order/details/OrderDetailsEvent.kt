package com.mahozi.sayed.talabiya.order.details

import com.mahozi.sayed.talabiya.core.ui.components.TlbDatePickerEvent
import com.mahozi.sayed.talabiya.order.details.suborder.Suborder
import user.UserEntity

sealed interface OrderDetailsEvent {
    object EditPricesClicked: OrderDetailsEvent

    sealed interface OrderInfoEvent: OrderDetailsEvent {
        object DateClicked: OrderInfoEvent
        data class DateEvent(val event: TlbDatePickerEvent): OrderInfoEvent
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