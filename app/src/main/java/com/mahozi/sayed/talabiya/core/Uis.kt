package com.mahozi.sayed.talabiya.core

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.order.create.CreateOrderEvent
import com.mahozi.sayed.talabiya.order.create.CreateOrderScreen
import com.mahozi.sayed.talabiya.order.create.CreateOrderState
import com.mahozi.sayed.talabiya.order.create.CreateOrderUi
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsEvent
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsScreen
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsState
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsUi
import com.mahozi.sayed.talabiya.order.list.ui.OrdersEvent
import com.mahozi.sayed.talabiya.order.list.ui.OrdersScreen
import com.mahozi.sayed.talabiya.order.list.ui.OrdersState
import com.mahozi.sayed.talabiya.order.list.ui.OrdersUi
import com.mahozi.sayed.talabiya.resturant.create.CreateRestaurantEvent
import com.mahozi.sayed.talabiya.resturant.create.CreateRestaurantScreen
import com.mahozi.sayed.talabiya.resturant.create.CreateRestaurantState
import com.mahozi.sayed.talabiya.resturant.list.RestaurantsEvent
import com.mahozi.sayed.talabiya.resturant.list.RestaurantsScreen
import com.mahozi.sayed.talabiya.resturant.list.RestaurantsState
import com.mahozi.sayed.talabiya.resturant.menu.CreateMenuItemEvent
import com.mahozi.sayed.talabiya.resturant.menu.CreateMenuItemScreen
import com.mahozi.sayed.talabiya.resturant.menu.CreateMenuItemState
import com.mahozi.sayed.talabiya.resturant.menu.MenuItemsEvent
import com.mahozi.sayed.talabiya.resturant.menu.MenuItemsScreen
import com.mahozi.sayed.talabiya.resturant.menu.MenuItemsState
import com.mahozi.sayed.talabiya.user.create.CreateUserEvent
import com.mahozi.sayed.talabiya.user.create.CreateUserScreen
import com.mahozi.sayed.talabiya.user.create.CreateUserState
import com.mahozi.sayed.talabiya.user.list.UsersEvent
import com.mahozi.sayed.talabiya.user.list.UsersScreen
import com.mahozi.sayed.talabiya.user.list.UsersState

@SuppressLint("ComposeNamingLowercase")
@Composable
fun Uis(
  screen: Screen,
): Ui<*, *> {
  return when (screen) {
    is OrdersScreen -> ui<OrdersState, OrdersEvent> { state, onEvent ->
      OrdersUi(state, onEvent)
    }

    is OrderDetailsScreen -> ui<OrderDetailsState, OrderDetailsEvent> { state, onEvent ->
      OrderDetailsUi(state, onEvent)
    }

    is CreateOrderScreen -> ui<CreateOrderState, CreateOrderEvent> { state, onEvent ->
      CreateOrderUi(state, onEvent)
    }

    is RestaurantsScreen -> ui<RestaurantsState, RestaurantsEvent> { state, onEvent ->
      RestaurantsScreen(state, onEvent)
    }

    is CreateRestaurantScreen -> ui<CreateRestaurantState, CreateRestaurantEvent> { state, onEvent ->
      CreateRestaurantScreen(state, onEvent)
    }

    is MenuItemsScreen -> ui<MenuItemsState, MenuItemsEvent> { state, onEvent ->
      MenuItemsScreen(state, onEvent)
    }

    is CreateMenuItemScreen -> ui<CreateMenuItemState, CreateMenuItemEvent> { state, onEvent ->
      CreateMenuItemScreen(state, onEvent)
    }

    is UsersScreen -> ui<UsersState, UsersEvent> { state, onEvent ->
      UsersScreen(state, onEvent)
    }

    is CreateUserScreen -> ui<CreateUserState, CreateUserEvent> { state, onEvent ->
      CreateUserScreen(state, onEvent)
    }

    else -> throw IllegalStateException("Unknown screen $screen")
  }
}

inline fun <State, Event> ui(
  crossinline body: @Composable (state: State, onEvent: (Event) -> Unit) -> Unit
): Ui<State, Event> {
  return object : Ui<State, Event> {
    @Composable
    override fun Content(state: State, onEvent: (Event) -> Unit) {
      body(state, onEvent)
    }
  }
}

interface Ui<State, Event> {
  @Composable
  fun Content(state: State, onEvent: (Event) -> Unit)
}