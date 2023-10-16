package com.mahozi.sayed.talabiya.order.details.tabs

import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.details.suborder.Suborder
import user.UserEntity
import java.time.Instant

data class OrderDetailsState(
    val info: OrderInfoState?,
    val subordersState: SubordersState?
)

data class OrderInfoState(
    val datetime: Instant,
    val total: Money,
    val payer: String?,
    val status: OrderStatus,
    val note: String,
    val datePickerVisible: Boolean,
)

data class SubordersState(
    val suborders: List<Suborder>,
    val users: List<UserEntity>,
)
