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
import com.mahozi.sayed.talabiya.resturant.menu.CreateMenuItemScreen
import com.mahozi.sayed.talabiya.resturant.menu.MenuItem
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
    val menuItems by remember { getMenuItems() }.collectAsState(initial = emptyList())
    val addedItems by remember {
      orderStore.getUserOrderItems(orderId = screen.orderId, userId = screen.userid)
    }.collectAsState(initial = emptyList())
    var openedMenuItem by remember { mutableStateOf(null as OpenedOrderItemState?) }

    CollectEvents(events) { event ->
      when(event) {
        CreateSuborderEvent.AddMenuItemClicked -> {
          launch {
            val restaurantId = orderStore.getRestaurantId(screen.orderId)
            navigator.goto(CreateMenuItemScreen(restaurantId))
          }
        }
        is CreateSuborderEvent.MenuItemClicked -> {
          //TODO fix id comparision
          val quantity = addedItems.find { it.id == event.priceId }?.quantity ?: 1
          openedMenuItem = OpenedOrderItemState(event.priceId, quantity)
        }
        is CreateSuborderEvent.QuantityChanged -> openedMenuItem = openedMenuItem!!.copy(quantity = event.newQuantity)
        is CreateSuborderEvent.OnSaveMenuItemClicked -> {
          launch {
            orderStore.insertOrderItem(
              screen.orderId,
              screen.userid,
              openedMenuItem!!.quantity.toLong(),
              openedMenuItem!!.menuItemPriceId
            )
            openedMenuItem = null
          }
        }
        is CreateSuborderEvent.OnCancelAddingMenuItem -> openedMenuItem = null
      }
    }

    return CreateSuborderState(
      menuItems = menuItems,
      addedItems = addedItems,
      openedOrderItemState = openedMenuItem
    )
  }

  //Todo not sure if this is a good way to deal with this
  private fun getMenuItems(): Flow<List<MenuItem>> {
    return runBlocking {
      val restaurantId = orderStore.getRestaurantId(screen.orderId)
      restaurantStore.menuItems(restaurantId)
    }


  }
  @AssistedFactory interface Factory {
    fun create(screen: CreateSuborderScreen): CreateSuborderPresenter
  }
}