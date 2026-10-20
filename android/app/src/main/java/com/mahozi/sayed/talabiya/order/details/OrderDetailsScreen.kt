package com.mahozi.sayed.talabiya.order.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaTopBarDefaults
import com.mahozi.sayed.talabiya.core.ui.components.TlbTab
import com.mahozi.sayed.talabiya.core.ui.components.TlbTabRow
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
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TalabiyaBar(
        title = {
          Text(stringResource(R.string.order_number, state.orderId))
        },
        navigationIcon = {
          TalabiyaTopBarDefaults.BackIcon(onBack)
        },
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


      Column {
        Tabs(
          tab = state.tab,
          onTabSelected = { tab -> onEvent(OrderDetailsEvent.ChangeTab(tab)) }
        )

        when (state.tab) {
          OrderDetailsTab.Info -> if (state.info != null) OrderInfoScreen(state.info, onEvent)
          OrderDetailsTab.Suborders -> if (state.subordersState != null) SubordersScreen(state.subordersState, onEvent)
          OrderDetailsTab.Full -> FullOrderScreen(state.fullOrderItems)
        }
      }
    }
  }
}

@Composable
private fun Tabs(
  tab: OrderDetailsTab,
  onTabSelected: (OrderDetailsTab) -> Unit
) {
  TlbTabRow(
    selectedTabIndex = tab.ordinal,
    containerColor = AppTheme.colors.surface,
    contentColor = AppTheme.colors.onSurface,
  ) {
    TlbTab(
      selected = tab == OrderDetailsTab.Info,
      onClick = { onTabSelected(OrderDetailsTab.Info) },
      text = { Text(text = string(R.string.info)) }
    )
    TlbTab(
      selected = tab == OrderDetailsTab.Suborders,
      onClick = { onTabSelected(OrderDetailsTab.Suborders) },
      text = { Text(text = string(R.string.suborders)) }
    )
    TlbTab(
      selected = tab == OrderDetailsTab.Full,
      onClick = { onTabSelected(OrderDetailsTab.Full) },
      text = { Text(text = string(R.string.full_order)) }
    )
  }
}