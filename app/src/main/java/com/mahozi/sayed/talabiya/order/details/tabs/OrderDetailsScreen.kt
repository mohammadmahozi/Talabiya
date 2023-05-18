package com.mahozi.sayed.talabiya.order.details.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.colors
import com.mahozi.sayed.talabiya.order.details.info.OrderInfoScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetailsScreen(val orderId: Int): Screen

@Composable fun OrderDetailsUi(state: OrderDetailsState, onEvent: (OrderDetailsEvent) -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tab = OrderDetailsTab.tab(selectedTabIndex)

    Column {
        if (state.info == null) return
        Tabs(
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        when (tab) {
            OrderDetailsTab.INFO -> OrderInfoScreen(
                state.info,
                onEvent
            )
            OrderDetailsTab.SUBORDERS -> {}
            OrderDetailsTab.FULL -> {}
        }
    }

}

@Composable fun Tabs(
    selectedTabIndex: Int,
    onTabSelected: (index: Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = colors.rowBackground,
        contentColor = colors.primaryText,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = colors.primary
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