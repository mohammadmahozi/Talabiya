package com.mahozi.sayed.talabiya.resturant.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.resturant.create.CreateRestaurantScreen
import com.mahozi.sayed.talabiya.resturant.menu.MenuItemsScreen
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RestaurantsPresenter @Inject constructor(
  private val restaurantStore: RestaurantStore,
  private val navigator: Navigator,
): Presenter<RestaurantsEvent, RestaurantsState> {

  @Composable override fun start(events: Flow<RestaurantsEvent>): RestaurantsState {
    val restaurants by remember { restaurantStore.restaurants }.collectAsState(initial = emptyList())

    CollectEvents(events) { event ->
      when(event) {
        RestaurantsEvent.CreateRestaurantClicked -> navigator.goto(CreateRestaurantScreen)
        is RestaurantsEvent.DeleteRestaurantClicked -> {
          restaurantStore.deleteRestaurant(event.restaurant.id)
        }
        is RestaurantsEvent.RestaurantClicked -> navigator.goto(MenuItemsScreen(event.restaurantEntity.id))
      }
    }
    return RestaurantsState(restaurants)
  }
}