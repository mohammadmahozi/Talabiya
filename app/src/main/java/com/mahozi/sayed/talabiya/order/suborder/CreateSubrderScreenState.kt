package com.mahozi.sayed.talabiya.order.suborder

import com.mahozi.sayed.talabiya.resturant.menu.MenuItem

data class CreateSuborderState(
  val menuItems: List<MenuItem>,
  val openedOrderItemState: OpenedOrderItemState?
)

sealed interface CreateSuborderEvent {
  data class MenuItemClicked(val id: Long): CreateSuborderEvent
  object AddMenuItemClicked: CreateSuborderEvent
  data class QuantityChanged(val newQuantity: Int): CreateSuborderEvent
  object OnSaveMenuItemClicked: CreateSuborderEvent
}

data class OpenedOrderItemState(
  val menuItemId: Long,
  val quantity: Int,
)