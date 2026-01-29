package com.mahozi.sayed.talabiya.order.suborder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.order.suborder.CreateSuborderEvent.*
import com.mahozi.sayed.talabiya.resturant.menu.CreateMenuItemScreen
import com.mahozi.sayed.talabiya.resturant.menu.MenuItem
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CreateSuborderPresenter @AssistedInject constructor(
  @Assisted private val screen: CreateSuborderScreen,
  private val orderStore: OrderStore,
  private val restaurantStore: RestaurantStore,
  private val navigator: Navigator,
): Presenter<CreateSuborderEvent, CreateSuborderState> {

  @Composable override fun start(events: Flow<CreateSuborderEvent>): CreateSuborderState {
    var query by remember { mutableStateOf("") }
    val menuItems by remember(query) { getMenuItems(query) }.collectAsState(initial = emptyList())
    val addedItems by remember {
      orderStore.getUserOrderItems(orderId = screen.orderId, userId = screen.userid)
    }.collectAsState(initial = emptyList())

    var openedMenuItem by remember { mutableStateOf(null as OpenedOrderItemState?) }

    CollectEvents(events) { event ->
      when(event) {
        AddMenuItemClicked -> {
          launch {
            val restaurantId = orderStore.getRestaurantId(screen.orderId)
            navigator.goto(CreateMenuItemScreen(restaurantId))
          }
        }
        is MenuItemClicked -> {
          val orderItem = addedItems.find { it.menuItemId == event.item.id }
          openedMenuItem = OpenedOrderItemState(
            menuItemId = event.item.id,
            quantity = orderItem?.quantity ?: 1,
            price = event.item.price,
            orderItemId = orderItem?.id
          )
        }
        is QuantityChanged -> {
          openedMenuItem = openedMenuItem!!.copy(quantity = event.newQuantity.coerceAtLeast(1))
        }
        is DeleteItem -> {
          launch {
            orderStore.deleteOrderItem(
              itemId = openedMenuItem!!.orderItemId!!,
              orderId = screen.orderId,
              userId = screen.userid
            )
            openedMenuItem = null
          }
        }
        is OnSaveMenuItemClicked -> {
          launch {
            orderStore.insertOrderItem(
              screen.orderId,
              screen.userid,
              openedMenuItem!!.menuItemId,
              openedMenuItem!!.quantity.toLong(),
              openedMenuItem!!.price
            )
            openedMenuItem = null
          }
        }
        is OnCancelAddingMenuItem -> openedMenuItem = null
        is QueryChanged -> query = event.query
      }
    }

    return CreateSuborderState(
      query = query,
      menuItems = menuItems,
      addedItems = addedItems,
      openedOrderItemState = openedMenuItem
    )
  }

  //Todo not sure if this is a good way to deal with this
  private fun getMenuItems(query: String): Flow<List<MenuItem>> {
    return runBlocking {
      val restaurantId = orderStore.getRestaurantId(screen.orderId)
      restaurantStore.menuItems(restaurantId, query)
    }


  }
  @AssistedFactory interface Factory {
    fun create(screen: CreateSuborderScreen): CreateSuborderPresenter
  }
}