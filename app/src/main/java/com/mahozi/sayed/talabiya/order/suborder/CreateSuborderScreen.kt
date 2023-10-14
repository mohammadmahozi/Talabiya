package com.mahozi.sayed.talabiya.order.suborder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.SearchBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.resturant.menu.MenuItem
import kotlinx.parcelize.Parcelize

@Parcelize data class CreateSuborderScreen(
  val orderId: Long,
  val userid: Long,
): Screen

@Preview
@Composable private fun PreviewCreateSuborderScreen() {
  AppTheme {
    CreateSuborderScreen(
      listOf(
        MenuItem(
          0,
          "Item 1",
          "Pastery",
          14.money
        )
      )
    )
  }
}

@Composable fun CreateSuborderScreen(
  menuItems: List<MenuItem>,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      SearchBar(
        title = R.string.menu,
        query = "",
        onSearchClicked = {},
        onQueryChanged = {},
      )
    },
    floatingActionButton = {
      AddFab {

      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = modifier
        .padding(paddingValues)
    ) {
      items(menuItems) {
        MenuItem(item = it, onItemClicked = {})
        Divider()
      }
    }
  }
}

@Composable private fun MenuItem(
  item: MenuItem,
  onItemClicked: (MenuItem) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable { onItemClicked(item) }
      .padding(vertical = 12.dp, horizontal = 16.dp)
  ) {

    Text(
      text = item.name,
      style = AppTheme.types.title
    )

    Spacer(Modifier.weight(1F))

    Text(
      text = item.price.format(),
      style = AppTheme.types.title
    )
  }
}