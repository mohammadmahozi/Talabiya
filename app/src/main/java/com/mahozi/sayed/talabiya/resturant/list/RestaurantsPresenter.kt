package com.mahozi.sayed.talabiya.resturant.list

import androidx.compose.runtime.Composable
import com.mahozi.sayed.talabiya.core.Presenter
import kotlinx.coroutines.flow.Flow

class RestaurantsPresenter(

): Presenter<RestaurantsEvent, RestaurantsState> {


  @Composable override fun start(events: Flow<RestaurantsEvent>): RestaurantsState {
    TODO("Not yet implemented")
  }
}