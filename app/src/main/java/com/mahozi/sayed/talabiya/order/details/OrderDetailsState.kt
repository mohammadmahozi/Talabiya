package com.mahozi.sayed.talabiya.order.details

import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.details.full.FullOrderItem
import com.mahozi.sayed.talabiya.order.details.suborder.Suborder
import user.UserEntity
import java.lang.IllegalArgumentException
import java.time.Instant

data class OrderDetailsState(
    val info: OrderInfoState?,
    val subordersState: SubordersState?,
    val fullOrderItems: List<FullOrderItem>
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

enum class OrderDetailsTab {
  Info,
  Suborders,
  Full;

  companion object {
    fun tab(index: Int) =
      when (index) {
        0 -> Info
        1 -> Suborders
        2 -> Full
        else -> throw IllegalArgumentException("Max tab index is 2 but got $index")
      }
  }
}
