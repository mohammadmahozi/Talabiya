package com.mahozi.sayed.talabiya.order.details.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.order.details.info.OrderInfoScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetailsScreen(val orderId: Long) : Screen

@Composable
fun OrderDetailsUi(state: OrderDetailsState, onEvent: (OrderDetailsEvent) -> Unit) {
  Scaffold(
    topBar = { TalabiyaBar(R.string.order_number) }
  ) {
    Box(
      modifier = Modifier.padding(it)
    ) {
      var selectedTabIndex by remember { mutableStateOf(0) }
      val tab = OrderDetailsTab.tab(selectedTabIndex)

      Column {
        Tabs(
          selectedTabIndex = selectedTabIndex,
          onTabSelected = { selectedTabIndex = it }
        )

        when (tab) {
          OrderDetailsTab.INFO -> if (state.info != null) OrderInfoScreen(state.info, onEvent)
          OrderDetailsTab.SUBORDERS -> {}
          OrderDetailsTab.FULL -> {}
        }
      }
    }
  }
}

@Composable
fun Tabs(
  selectedTabIndex: Int,
  onTabSelected: (index: Int) -> Unit
) {
  TabRow(
    selectedTabIndex = selectedTabIndex,
    backgroundColor = AppTheme.colors.backgroundSecondary,
    contentColor = AppTheme.colors.primaryText,
    indicator = @Composable { tabPositions ->
      TabRowDefaults.Indicator(
        modifier = Modifier
          .tabIndicatorOffset(tabPositions[selectedTabIndex]),
        color = AppTheme.colors.material.primary
      )
    },
  ) {
    Tab(
      selected = false,
      onClick = { onTabSelected(0) },
      text = {
        Text(
          text = string(R.string.info)
        )
      }
    )
    Tab(
      selected = false,
      onClick = { onTabSelected(1) },
      text = {
        Text(
          text = string(R.string.suborders)
        )
      }
    )
    Tab(
      selected = false,
      onClick = { onTabSelected(2) },
      text = {
        Text(
          text = string(R.string.full_order)
        )
      }
    )
  }
}