package com.mahozi.sayed.talabiya.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface OrderRoute: Parcelable {

    @Parcelize object Orders: OrderRoute
    @Parcelize data class OrderDetails(val orderId: Int): OrderRoute
}