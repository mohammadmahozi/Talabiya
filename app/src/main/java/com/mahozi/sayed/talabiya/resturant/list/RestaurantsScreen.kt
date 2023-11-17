package com.mahozi.sayed.talabiya.resturant.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.DeleteContextMenu
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import kotlinx.parcelize.Parcelize
import restaurant.RestaurantEntity

@Parcelize
object RestaurantsScreen : Screen

@Preview
@Composable
private fun PreviewRestaurantScreen() {
  AppTheme {
    RestaurantsScreen(
      state = RestaurantsState(listOf(RestaurantEntity(0L, "Name"))),
      onEvent = {}
    )
  }
}

@Composable fun RestaurantsScreen(
  state: RestaurantsState,
  onEvent: (RestaurantsEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TalabiyaBar(title = R.string.restaurants)
    },
    floatingActionButton = { AddFab { onEvent(RestaurantsEvent.CreateRestaurantClicked) } }
  ) { paddingValues ->
    Box(
      modifier = modifier
        .padding(paddingValues)
    ) {
      LazyColumn {
        items(state.restaurants) { restaurant ->
          Restaurant(
            restaurant = restaurant,
            onClick = { onEvent(RestaurantsEvent.RestaurantClicked(it)) },
            onDelete = { onEvent(RestaurantsEvent.DeleteRestaurantClicked(it)) }
          )
          Divider()
        }
      }
    }
  }
}

@Preview
@Composable
private fun PreviewRestaurant() {
  AppTheme {
    Restaurant(
      restaurant = RestaurantEntity(0L, "Name"),
      onClick = {},
      onDelete = {},
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable private fun Restaurant(
  restaurant: RestaurantEntity,
  onClick: (RestaurantEntity) -> Unit,
  onDelete: (RestaurantEntity) -> Unit,
  ) {
  var expanded by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier
      .combinedClickable(
        onClick = { onClick(restaurant) },
        onLongClick = { expanded = true }
      )
      .padding(16.dp)
      .fillMaxWidth()
  ) {
    Text(
      text = restaurant.id.toString(),
      color = AppTheme.colors.primaryText,
      fontSize = 14.sp,
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = restaurant.name,
      color = AppTheme.colors.primaryText,
      fontSize = 14.sp,
    )

    DeleteContextMenu(
      expanded = expanded,
      onDelete = { onDelete(restaurant) },
      onDismiss = { expanded = false }
    )
  }
}


