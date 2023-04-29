package com.mahozi.sayed.talabiya.order

import androidx.annotation.StringRes
import com.mahozi.sayed.talabiya.R

enum class OrderStatus {
    NEW,
    COMPLETE
}

@get:StringRes val OrderStatus.title get(): Int = when(this) {
    OrderStatus.NEW -> R.string.new_
    OrderStatus.COMPLETE -> R.string.complete
}