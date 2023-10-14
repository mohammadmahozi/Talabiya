package com.mahozi.sayed.talabiya.order.suborder

import com.mahozi.sayed.talabiya.resturant.menu.MenuItem

data class CreateSuborderState(
  val menuItems: List<MenuItem>,
)

sealed interface CreateSuborderEvent {
  data class MenuItemClicked(val id: Long,): CreateSuborderEvent
  object AddMenuItemClicked: CreateSuborderEvent
}