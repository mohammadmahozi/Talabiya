package com.mahozi.sayed.talabiya.order.suborder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.resturant.menu.MenuItem
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking


class CreateSuborderPresenter @AssistedInject constructor(
  @Assisted private val screen: CreateSuborderScreen,
  private val orderStore: OrderStore,
  private val restaurantStore: RestaurantStore,
): Presenter<CreateSuborderEvent, CreateSuborderState> {

  @Composable override fun start(events: Flow<CreateSuborderEvent>): CreateSuborderState {
    val menuItems by remember { getMenuItems() }.collectAsState(initial = emptyList())

    var openedMenuItem by remember { mutableStateOf(null as OpenedOrderItemState?) }

    CollectEvents(events) { event ->
      when(event) {
        CreateSuborderEvent.AddMenuItemClicked -> TODO()
        is CreateSuborderEvent.MenuItemClicked -> TODO()
        is CreateSuborderEvent.OnSaveMenuItemClicked -> TODO()
        is CreateSuborderEvent.QuantityChanged -> TODO()
      }
    }

    return CreateSuborderState(menuItems, null)
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