package com.mahozi.sayed.talabiya.resturant.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.ConfirmFab
import com.mahozi.sayed.talabiya.core.ui.components.DropDown
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

data class CreateMenuItemState(
  val name: String,
  val price: String,
  val selectedCategory: String?,
  val categories: List<String>,
  val canSave: Boolean
) {
}

sealed interface CreateMenuItemEvent {
  data class NameChanged(val name: String): CreateMenuItemEvent
  data class PriceChanged(val price: String): CreateMenuItemEvent
  data class CategoryChanged(val category: String): CreateMenuItemEvent
  object SaveClicked: CreateMenuItemEvent
}

class CreateMenuItemPresenter @AssistedInject constructor(
  @Assisted private val restaurantId: Long,
  private val navigator: Navigator,
  private val restaurantStore: RestaurantStore,
) : Presenter<CreateMenuItemEvent, CreateMenuItemState> {

  @Composable
  override fun start(events: Flow<CreateMenuItemEvent>): CreateMenuItemState {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val categories by produceState(listOf<String>()) { value = restaurantStore.getCategories() }

    val canSave by produceState(
      initialValue = false,
      key1 = name,
      key2 = price,
      key3 = selectedCategory,
      producer = {
        value = name.isNotBlank() && price.isNotBlank() && selectedCategory != null && selectedCategory!!.isNotBlank()
      }
    )

    CollectEvents(events) { event ->
      when(event) {
        is CreateMenuItemEvent.NameChanged -> name = event.name
        is CreateMenuItemEvent.CategoryChanged -> selectedCategory = event.category
        is CreateMenuItemEvent.PriceChanged -> price = event.price
        CreateMenuItemEvent.SaveClicked -> {
          launch {
            restaurantStore.createMenuItem(
              restaurantId,
              name,
              price.money,
              selectedCategory!!,
            )
            navigator.back()
          }
        }
      }
    }

    return CreateMenuItemState(name, price, selectedCategory, categories, canSave)
  }

  @AssistedFactory interface Factory {
    fun create(restaurantId: Long): CreateMenuItemPresenter
  }
}

@Parcelize
data class CreateMenuItemScreen(val restaurantId: Long) : Screen

@Preview
@Composable
private fun PreviewCreateMenuItemScreen() {
  AppTheme {
    CreateMenuItemScreen(
      state = CreateMenuItemState(
        "Name",
        "12.5",
        null,
        listOf(),
        false
      ),
      onEvent = {}
    )
  }
}

@Composable
fun CreateMenuItemScreen(
  state: CreateMenuItemState,
  onEvent: (CreateMenuItemEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TalabiyaBar(R.string.add_new_item)
    },
    floatingActionButton = {
      ConfirmFab(
        enabled = state.canSave
      ) {
        onEvent(CreateMenuItemEvent.SaveClicked)
      }
    },
  ) {
    CreateMenuItemContent(
      state = state,
      onEvent = onEvent,
      modifier.padding(it)
    )
  }
}

@Composable
private fun CreateMenuItemContent(
  state: CreateMenuItemState,
  onEvent: (CreateMenuItemEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  val focusRequester = remember { FocusRequester() }

  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier
      .padding(16.dp)
  ) {
    OutlinedTextField(
      value = state.name,
      onValueChange = { onEvent(CreateMenuItemEvent.NameChanged(it)) },
      placeholder = { 
        Text(text = stringResource(R.string.name))
      },
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
      modifier = Modifier
        .fillMaxWidth()
        .focusRequester(focusRequester),
    )

    OutlinedTextField(
      value = state.price,
      onValueChange = { onEvent(CreateMenuItemEvent.PriceChanged(it)) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
      placeholder = {
        Text(text = stringResource(R.string.price))
      },
      modifier = Modifier
        .fillMaxWidth()
    )

    DropDown(
      value = state.selectedCategory.orEmpty(),
      items = state.categories,
      onItemSelected = { onEvent(CreateMenuItemEvent.CategoryChanged(it)) },
      startExpanded = false,
      itemContent = {
        Text(text = it)
      }
    )
  }

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}