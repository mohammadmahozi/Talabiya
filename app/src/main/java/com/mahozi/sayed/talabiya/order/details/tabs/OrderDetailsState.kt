package com.mahozi.sayed.talabiya.order.details.tabs

import com.mahozi.sayed.talabiya.order.OrderStatus
import java.time.LocalDate
import java.time.LocalTime

data class OrderDetailsState(
    val info: OrderInfoState,
)

data class OrderInfoState(
    val date: LocalDate,
    val time: LocalTime,
    val total: Double,
    val payer: String,
    val status: OrderStatus
)
