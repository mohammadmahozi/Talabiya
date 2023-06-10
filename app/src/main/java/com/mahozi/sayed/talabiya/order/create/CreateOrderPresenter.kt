package com.mahozi.sayed.talabiya.order.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.order.store.OrderStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import restaurant.RestaurantEntity
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class CreateOrderPresenter @Inject constructor(
  private val orderStore: OrderStore,
  private val navigator: Navigator,
): Presenter<CreateOrderEvent, CreateOrderState> {

  @Composable
  override fun start(events: Flow<CreateOrderEvent>): CreateOrderState {
    var selectedRestaurant by remember { mutableStateOf<RestaurantEntity?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }

    val canConfirm by remember(selectedRestaurant) { mutableStateOf(selectedRestaurant != null) }

    CollectEvents(events) {event ->
      when(event) {
        is CreateOrderEvent.RestaurantSelected -> selectedRestaurant = event.restaurant
        is CreateOrderEvent.DateSelected -> selectedDate = event.date
        is CreateOrderEvent.TimeSelected -> selectedTime = event.time
        CreateOrderEvent.CreateOrder -> {
          launch {
            orderStore.createOrder(selectedRestaurant!!.id, selectedDate, selectedTime)
            navigator.back()
          }
        }
      }
    }

    return CreateOrderState(
      selectedRestaurant,
      listOf(
        RestaurantEntity(0L, "Restaurant 1"),
        RestaurantEntity(0L, "Restaurant 2"),
        RestaurantEntity(0L, "Restaurant 3"),
      ),
      LocalDate.now(),
      LocalTime.now()
    )
  }
}