package com.mahozi.sayed.talabiya.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface OrdersRoute: Parcelable {

    @Parcelize object Orders: OrdersRoute
}