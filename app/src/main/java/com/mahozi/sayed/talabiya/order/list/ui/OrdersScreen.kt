package com.mahozi.sayed.talabiya.order.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.core.ui.theme.colors
import com.mahozi.sayed.talabiya.order.store.OrderEntity
import kotlinx.parcelize.Parcelize


@Parcelize
object OrdersScreen : Screen


@Composable
fun OrdersUi(state: OrdersState, onEvent: (OrdersEvent) -> Unit) {
  AppTheme {
    Scaffold(
      topBar = {
        TalabiyaBar(title = R.string.app_name)
      },
      floatingActionButton = {
        AddFab {
          onEvent(OrdersEvent.CreateOrderClicked)
        }
      }, floatingActionButtonPosition = FabPosition.End
    ) {
      Box(
        modifier = Modifier
          .padding(it)
          .padding(horizontal = 16.dp)
      ) {
        Orders(state.orders) { order ->
          onEvent(OrdersEvent.OrderClicked(order))
        }
      }
    }
  }
}

@Composable
fun Orders(orders: List<OrderEntity>, onClick: (OrderEntity) -> Unit) {
  LazyColumn {
    items(orders) {
      OrderRow(order = it, onClick)
    }
  }
}

@Composable
fun PreviewOrderRow() {
  OrderRow(order = OrderEntity("Tannoor", "200", "20").apply {
    id = 0
    status = false
  }) {}
}

@Composable
fun OrderRow(order: OrderEntity, onClick: (OrderEntity) -> Unit) {
  Row(Modifier
    .background(colors.rowBackground)
    .clickable { onClick(order) }
    .padding(vertical = 8.dp)) {

    Text(
      text = order.id.toString(), modifier = Modifier.padding(end = 48.dp)
    )
    Column {
      Text(
        text = order.restaurantName, Modifier.padding(bottom = 4.dp)
      )

      Text(
        text = order.date,
        color = colors.secondaryText,
        fontSize = 12.sp,
      )
    }

    Spacer(Modifier.weight(1f))
    if (order.status) {
      Text(
        text = stringResource(R.string.complete),
      )
    }
  }
}