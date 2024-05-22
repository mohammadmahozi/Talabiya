package com.mahozi.sayed.talabiya.resturant.option

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.ConfirmFab
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaCheckbox
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

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
  data class NameChanged(val name: String) : CreateOptionEvent
  data class CategoryClicked(val category: FoodOption.Category) : CreateOptionEvent
  object SaveClicked : CreateOptionEvent
}

class CreateOptionPresenter @AssistedInject constructor(
  @Assisted private val restaurantId: Long,
  private val restaurantStore: RestaurantStore,
  private val navigator: Navigator,
) : Presenter<CreateOptionEvent, CreateOptionState> {

  @Composable
  override fun start(events: Flow<CreateOptionEvent>): CreateOptionState {
    var name by remember { mutableStateOf("") }

    var categories by remember { mutableStateOf(listOf<FoodOption.Category>()) }
    LaunchedEffect(Unit) {
      categories = restaurantStore.getCategories().map { FoodOption.Category(it, false) }
    }

    val canSave = name.isNotEmpty() && categories.any { it.selected }

    CollectEvents(events) { event ->
      when (event) {
        is CreateOptionEvent.NameChanged -> {
          name = event.name
        }

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
            navigator.back()
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

  @AssistedFactory interface CreateOptionPresenterFactory {
    fun create(restaurantId: Long): CreateOptionPresenter
  }
}

@Composable
fun CreateOptionScreen(
  state: CreateOptionState,
  onEvent: (CreateOptionEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TalabiyaBar(title = { Text(text = stringResource(id = R.string.create_option)) })
    },
    floatingActionButton = {
      ConfirmFab(
        enabled = state.canSave
      ) {
        onEvent(CreateOptionEvent.SaveClicked)
      }
    }
  ) { paddingValues ->
    Column(
      modifier = modifier
        .padding(paddingValues)
        .padding(16.dp)
    ) {
      OutlinedTextField(
        value = state.name,
        onValueChange = { newName -> onEvent(CreateOptionEvent.NameChanged(newName)) },
        placeholder = {
          Text(
            text = string(R.string.enter_option_name)
          )
        },
        modifier = Modifier
          .fillMaxWidth(),
      )

      Spacer(modifier = Modifier.height(16.dp))

      state.categories.forEach { category ->
        TalabiyaCheckbox(
          category.name,
          checked = category.selected,
          onCheckedChange = { onEvent(CreateOptionEvent.CategoryClicked(category)) }
        )
        Spacer(modifier = Modifier.height(8.dp))


      }
    }
  }
}

@Preview
@Composable
private fun PreviewCreateOptionScreen() {
  AppTheme {
    CreateOptionScreen(
      state = CreateOptionState(name = "Test",
        categories = listOf(
          FoodOption.Category("C1", false),
          FoodOption.Category("C2", true)),
        canSave = true),
      onEvent = {}
    )
  }
}





