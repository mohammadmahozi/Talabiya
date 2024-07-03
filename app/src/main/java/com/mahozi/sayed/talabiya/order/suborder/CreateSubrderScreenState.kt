package com.mahozi.sayed.talabiya.order.suborder

import com.mahozi.sayed.talabiya.order.details.suborder.OrderItem
import com.mahozi.sayed.talabiya.resturant.menu.MenuItem

data class CreateSuborderState(
  val menuItems: List<MenuItem>,
  val addedItems: List<OrderItem>,
  val openedOrderItemState: OpenedOrderItemState?
)

sealed interface CreateSuborderEvent {
  data class MenuItemClicked(val priceId: Long): CreateSuborderEvent
  object AddMenuItemClicked: CreateSuborderEvent
  data class QuantityChanged(val newQuantity: Int): CreateSuborderEvent
  object OnSaveMenuItemClicked: CreateSuborderEvent
  object OnCancelAddingMenuItem: CreateSuborderEvent
}

data class OpenedOrderItemState(
  val menuItemPriceId: Long,
  val quantity: Int,
)