package com.mahozi.sayed.talabiya.order.details.tabs

import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.order.OrderStatus
import java.time.Instant

data class OrderDetailsState(
    val info: OrderInfoState?,
)

data class OrderInfoState(
    val datetime: Instant,
    val total: Money,
    val payer: String?,
    val status: OrderStatus,
    val note: String,
    val datePickerVisible: Boolean,
)
