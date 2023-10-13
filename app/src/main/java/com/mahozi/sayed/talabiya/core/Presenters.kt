package com.mahozi.sayed.talabiya.core

import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.order.create.CreateOrderPresenter
import com.mahozi.sayed.talabiya.order.create.CreateOrderScreen
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsPresenter
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsScreen
import com.mahozi.sayed.talabiya.order.list.ui.OrdersPresenter
import com.mahozi.sayed.talabiya.order.list.ui.OrdersScreen
import com.mahozi.sayed.talabiya.resturant.create.CreateRestaurantPresenter
import com.mahozi.sayed.talabiya.resturant.create.CreateRestaurantScreen
import com.mahozi.sayed.talabiya.resturant.list.RestaurantsEvent
import com.mahozi.sayed.talabiya.resturant.list.RestaurantsPresenter
import com.mahozi.sayed.talabiya.resturant.list.RestaurantsScreen
import com.mahozi.sayed.talabiya.resturant.menu.CreateMenuItemPresenter
import com.mahozi.sayed.talabiya.resturant.menu.CreateMenuItemScreen
import com.mahozi.sayed.talabiya.resturant.menu.MenuItemsPresenter
import com.mahozi.sayed.talabiya.resturant.menu.MenuItemsScreen
import com.mahozi.sayed.talabiya.user.list.UsersPresenter
import com.mahozi.sayed.talabiya.user.list.UsersScreen
import javax.inject.Inject
import javax.inject.Provider


class Presenters @Inject constructor(
  private val ordersPresenter: Provider<OrdersPresenter>,
  private val ordersDetailsPresenter: OrderDetailsPresenter.OrderDetailsPresenterAssistedFactory,
  private val createOrderPresenter: Provider<CreateOrderPresenter>,
  private val restaurantsPresenter: Provider<RestaurantsPresenter>,
  private val createRestaurantPresenter: Provider<CreateRestaurantPresenter>,
  private val menuItemPresenter: MenuItemsPresenter.Factory,
  private val createMenuItemPresenter: CreateMenuItemPresenter.Factory,
  private val usersPresenter: Provider<UsersPresenter>,
) {

  fun create(screen: Screen): Presenter<*, *> {
    return when (screen) {
      is OrdersScreen -> ordersPresenter.get()
      is OrderDetailsScreen -> ordersDetailsPresenter.create(screen.orderId)
      is CreateOrderScreen -> createOrderPresenter.get()
      is RestaurantsScreen -> restaurantsPresenter.get()
      is CreateRestaurantScreen -> createRestaurantPresenter.get()
      is MenuItemsScreen -> menuItemPresenter.create(screen.restaurantId)
      is CreateMenuItemScreen -> createMenuItemPresenter.create(screen.restaurantId)
      is UsersScreen -> usersPresenter.get()
      else -> throw IllegalStateException("Unknown screen $screen")
    }
  }
}