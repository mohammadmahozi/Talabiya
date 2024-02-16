package com.mahozi.sayed.talabiya.resturant.option

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class CreateOptionScreen(
  val restaurantId: Long,
) : Screen

data class CreateOptionState(
  val name: String,
  val categories: List<FoodOption.Category>,
  val canSave: Boolean,
)

sealed interface CreateOptionEvent {
  data class NameChanged(val name: String): CreateOptionEvent
  data class CategoryClicked(val category: FoodOption.Category) : CreateOptionEvent
  object SaveClicked : CreateOptionEvent
}

class CreateOptionPresenter @Inject constructor(
  private val restaurantId: Long,
  private val restaurantStore: RestaurantStore,
): Presenter<CreateOptionEvent, CreateOptionState> {

  @Composable override fun start(events: Flow<CreateOptionEvent>): CreateOptionState {
    var name by remember { mutableStateOf("") }

    var categories by remember { mutableStateOf(listOf<FoodOption.Category>()) }
    LaunchedEffect(Unit) {
      categories = restaurantStore.getCategories().map { FoodOption.Category(it, false) }
    }

    val canSave = name.isNotEmpty() && categories.any { it.selected }

    CollectEvents(events) { event ->
      when(event) {
        is CreateOptionEvent.NameChanged -> { name = event.name }
        is CreateOptionEvent.CategoryClicked -> categories = categories.map { category ->
          if (event.category.name == category.name) {
            category.copy(selected = !category.selected)
          } else {
            category
          }
        }
        CreateOptionEvent.SaveClicked -> {
          launch {
            restaurantStore.createOption(
              restaurantId,
              name,
              categories.filter { it.selected }.map { it.name },
            )
          }
        }
      }
    }

    return CreateOptionState(
      name,
      categories,
      canSave
    )
  }
}





