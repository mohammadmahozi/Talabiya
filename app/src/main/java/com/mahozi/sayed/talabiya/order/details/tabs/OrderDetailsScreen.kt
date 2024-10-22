package com.mahozi.sayed.talabiya.order.details.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.order.details.full.FullOrderScreen
import com.mahozi.sayed.talabiya.order.details.info.OrderInfoScreen
import com.mahozi.sayed.talabiya.order.details.suborder.SubordersScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetailsScreen(val orderId: Long) : Screen

@Composable
fun OrderDetailsUi(
  state: OrderDetailsState,
  onEvent: (OrderDetailsEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TalabiyaBar(
        title = R.string.order_number,
        overFlowActions = {
          DropdownMenuItem(
            text = {
              Text(text = stringResource(id = R.string.edit_prices))
            },
            onClick = { onEvent(OrderDetailsEvent.EditPricesClicked) }
          )
        }
      )
    }
  ) {
    Box(
      modifier = modifier.padding(it)
    ) {
      var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
      val tab = OrderDetailsTab.tab(selectedTabIndex)

      Column {
        Tabs(
          selectedTabIndex = selectedTabIndex,
          onTabSelected = { selectedTabIndex = it }
        )

        when (tab) {
          OrderDetailsTab.INFO -> if (state.info != null) OrderInfoScreen(state.info, onEvent)
          OrderDetailsTab.SUBORDERS -> if (state.subordersState != null) SubordersScreen(state.subordersState, onEvent)
          OrderDetailsTab.FULL -> FullOrderScreen(state.fullOrderItems)
        }
      }
    }
  }
}

@Composable
private fun Tabs(
  selectedTabIndex: Int,
  onTabSelected: (index: Int) -> Unit
) {
  TabRow(
    selectedTabIndex = selectedTabIndex,
    containerColor = AppTheme.colors.lightBackground,
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