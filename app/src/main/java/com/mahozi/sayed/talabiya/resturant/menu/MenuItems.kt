package com.mahozi.sayed.talabiya.resturant.menu

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

data class MenuItemsState(
  val menuItems: List<MenuItem>
)

sealed interface MenuItemsEvent {
  data class MenuItemClicked(val item: MenuItem) : MenuItemsEvent
  object AddMenuItemClicked : MenuItemsEvent
}

class MenuItemsPresenter @AssistedInject constructor(
  @Assisted private val restaurantId: Long,
  private val restaurantStore: RestaurantStore,
  private val navigator: Navigator,
) : Presenter<MenuItemsEvent, MenuItemsState> {

  @Composable
  override fun start(events: Flow<MenuItemsEvent>): MenuItemsState {
    val menuItems by remember { restaurantStore.menuItems(restaurantId) }.collectAsState(initial = listOf())

    CollectEvents(events) { event ->
      when(event) {
        MenuItemsEvent.AddMenuItemClicked -> navigator.goto(CreateMenuItemScreen(restaurantId))
        is MenuItemsEvent.MenuItemClicked -> TODO()
      }
    }

    return MenuItemsState(menuItems)
  }

  @AssistedFactory
  interface Factory {
    fun create(restaurantId: Long): MenuItemsPresenter
  }
}

@Parcelize
data class MenuItemsScreen(val restaurantId: Long) : Screen

@Composable
fun MenuItemsScreen(
  state: MenuItemsState,
  onEvent: (MenuItemsEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = { TalabiyaBar(title = R.string.menu) },
    floatingActionButton = {
      AddFab {
        onEvent(MenuItemsEvent.AddMenuItemClicked)
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .padding(paddingValues)
        .padding(16.dp)
    ) {
      items(state.menuItems) { menuItem ->
        MenuItem(
          menuItem,
          onClick = { onEvent(MenuItemsEvent.MenuItemClicked(menuItem))}
        )
        Divider()
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuItem() {
  AppTheme {
    MenuItem(
      MenuItem(
        0L,
        "Pizza",
        "Pastery",
        15.0.money
      )
    ){}
  }
}

@Composable
private fun MenuItem(
  menuItem: MenuItem,
  onClick: (MenuItem) -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick(menuItem) }
      .padding(vertical = 8.dp)
  ) {

    Row {
      Text(text = menuItem.name)

      Spacer(modifier = Modifier.weight(1F))

      Text(text = menuItem.price.format())

    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = menuItem.category,
      fontSize = 12.sp,
      color = AppTheme.colors.secondaryText
    )
  }
}

