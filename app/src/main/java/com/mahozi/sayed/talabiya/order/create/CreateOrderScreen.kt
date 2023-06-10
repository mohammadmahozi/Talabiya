package com.mahozi.sayed.talabiya.order.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Preview
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.DateField
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.components.TimeField
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import kotlinx.parcelize.Parcelize
import restaurant.RestaurantEntity
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
object CreateOrderScreen : Screen

@Preview
@Composable
fun PreviewCreateOrderUi() {
  Preview {
    CreateOrderUi(
      state = CreateOrderState(
        RestaurantEntity(1, "Name"),
        listOf(),
        LocalDate.now(),
        LocalTime.now()
      ),
      onEvent = {}
    )
  }
}

@Composable
fun CreateOrderUi(
  state: CreateOrderState,
  onEvent: (CreateOrderEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      TalabiyaBar(title = R.string.app_name)
    }) { paddingValues ->
    Column(
      modifier = modifier
        .padding(paddingValues)
        .padding(16.dp)
    )
    {
      Restaurants(
        selectedRestaurant = state.selectedRestaurant,
        restaurants = state.restaurants,
        onRestaurantSelected = { onEvent(CreateOrderEvent.RestaurantSelected(it)) }
      )

      Spacer(modifier = Modifier.height(16.dp))

      DateField(
        selectedDate = state.date,
        onDateSelected = { onEvent(CreateOrderEvent.DateSelected(it)) },
        padding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        modifier = Modifier
          .background(
            color = AppTheme.colors.backgroundSecondary,
            shape = AppTheme.shapes.small
          )
      )

      Spacer(modifier = Modifier.height(8.dp))

      TimeField(
        selectedTime = state.time,
        onTimeSelected = { onEvent(CreateOrderEvent.TimeSelected(it)) },
        padding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        modifier = Modifier
          .background(
            color = AppTheme.colors.backgroundSecondary,
            shape = AppTheme.shapes.small
          )
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewRestaurants() {
  Restaurants(
    selectedRestaurant = null,
    restaurants = listOf(
      RestaurantEntity(0L, "Restaurant 1"),
      RestaurantEntity(0L, "Restaurant 2"),
      RestaurantEntity(0L, "Restaurant 3"),
    ),
    onRestaurantSelected = {}
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Restaurants(
  selectedRestaurant: RestaurantEntity?,
  restaurants: List<RestaurantEntity>,
  onRestaurantSelected: (RestaurantEntity) -> Unit,
) {
  var expanded by remember { mutableStateOf(true) }

  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = !expanded },
  ) {
    TextField(
      value = selectedRestaurant?.name ?: "",
      onValueChange = { },
      label = { Text(stringResource(R.string.select_restaurant)) },
      readOnly = true,
      trailingIcon = {
        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
      },
      colors = ExposedDropdownMenuDefaults.textFieldColors(
        backgroundColor = AppTheme.colors.backgroundSecondary,
      ),
      modifier = Modifier
        .fillMaxWidth()
    )

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      properties = PopupProperties(
        focusable = true,
        dismissOnClickOutside = true,
        dismissOnBackPress = true
      ),
      modifier = Modifier
        .exposedDropdownSize()
    ) {
      restaurants.forEach { restaurant ->
        DropdownMenuItem(
          onClick = {
            onRestaurantSelected(restaurant)
            expanded = false
          },
        ) {
          Text(
            text = restaurant.name,
          )
        }
      }
    }
  }
}