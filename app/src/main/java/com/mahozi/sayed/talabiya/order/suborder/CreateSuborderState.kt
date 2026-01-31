package com.mahozi.sayed.talabiya.order.suborder

import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.order.details.suborder.OrderItem

data class CreateSuborderState(
  val query: String,
  val menuItems: List<MenuItemState>,
  val addedItems: List<OrderItem>,
  val openedOrderItemState: OpenedOrderItemState?
)

sealed interface CreateSuborderEvent {
  data class MenuItemClicked(val item: MenuItemState): CreateSuborderEvent
  data object AddMenuItemClicked: CreateSuborderEvent
  data class QuantityChanged(val newQuantity: Int): CreateSuborderEvent
  object OnSaveMenuItemClicked: CreateSuborderEvent
  data object OnCancelAddingMenuItem: CreateSuborderEvent
  data object DeleteItem: CreateSuborderEvent

  data class QueryChanged(val query: String): CreateSuborderEvent
}

data class OpenedOrderItemState(
  val menuItemId: Long,
  val quantity: Int,
  val price: Money,
  val orderItemId: Long? = null,
) {
  val showDelete get() = quantity == 1 && orderItemId != null
}

data class MenuItemState(
  val id: Long,
  val name: String,
  val category: String,
  val priceId: Long,
  val price: Money,
  val quantity: Int,
)