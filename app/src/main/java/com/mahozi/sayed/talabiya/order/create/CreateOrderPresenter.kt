package com.mahozi.sayed.talabiya.order.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mahozi.sayed.talabiya.core.Presenter
import kotlinx.coroutines.flow.Flow
import restaurant.RestaurantEntity
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.CollectEvents
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateOrderPresenter @Inject constructor(

): Presenter<CreateOrderEvent, CreateOrderState> {

  @Composable
  override fun start(events: Flow<CreateOrderEvent>): CreateOrderState {
    var selectedRestaurant by remember { mutableStateOf<RestaurantEntity?>(null) }

    CollectEvents {
      launch {
        events.collect { event ->
          when(event) {
            is CreateOrderEvent.RestaurantSelected -> selectedRestaurant = event.restaurant
            is CreateOrderEvent.DateSelected -> TODO()
            is CreateOrderEvent.TimeSelected -> TODO()
            CreateOrderEvent.CreateOrder -> TODO()
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