package com.mahozi.sayed.talabiya.resturant.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.ConfirmFab
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

data class CreateRestaurantState(
  val name: String,
  val canCreate: Boolean,
)

sealed interface CreateRestaurantEvent {
  data class NameChanged(val name: String) : CreateRestaurantEvent
  object CreateRestaurantClicked : CreateRestaurantEvent
}

class CreateRestaurantPresenter @Inject constructor(
  private val restaurantStore: RestaurantStore,
  private val navigator: Navigator,
) : Presenter<CreateRestaurantEvent, CreateRestaurantState> {

  @Composable
  override fun start(events: Flow<CreateRestaurantEvent>): CreateRestaurantState {
    var name by remember { mutableStateOf("") }
    val canCreate = name.isNotBlank()

    CollectEvents(events) { event ->
      when (event) {
        CreateRestaurantEvent.CreateRestaurantClicked -> {
          launch {
            restaurantStore.createRestaurant(name)

          }
        }

        is CreateRestaurantEvent.NameChanged -> name = event.name
      }
    }
    return CreateRestaurantState(name, canCreate)
  }
}

@Parcelize
object CreateRestaurantScreen : Screen

@Composable
fun CreateRestaurantScreen(
  state: CreateRestaurantState,
  onEvent: (CreateRestaurantEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      TalabiyaBar(title = R.string.create_restaurant)
    },
    floatingActionButton = {
      ConfirmFab { onEvent(CreateRestaurantEvent.CreateRestaurantClicked) }
    }
  ) { paddingValues ->
    Box(
      modifier = modifier
        .padding(paddingValues)
    ) {
      TextField(
        value = state.name,
        onValueChange = { newName -> onEvent(CreateRestaurantEvent.NameChanged(newName)) }
      )
    }
  }
}