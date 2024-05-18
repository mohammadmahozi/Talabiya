package com.mahozi.sayed.talabiya.order.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.order.list.Order
import kotlinx.parcelize.Parcelize
import java.time.Instant


@Parcelize
object OrdersScreen : Screen


@Composable
fun OrdersUi(
  state: OrdersState,
  onEvent: (OrdersEvent) -> Unit,
  modifier: Modifier = Modifier
) {
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
      modifier = modifier
        .padding(it)
        .padding(horizontal = 16.dp)
    ) {
      Orders(state.orders) { order ->
        onEvent(OrdersEvent.OrderClicked(order))
      }
    }
  }
}

@Composable
private fun Orders(orders: List<Order>, onClick: (Order) -> Unit) {
  LazyColumn {
    items(orders) {
      OrderRow(order = it, onClick)
    }
  }
}

@Preview
@Composable
private fun PreviewOrderRow() {
  OrderRow(
    order = Order(
      1L,
      "Tannoor",
      Instant.now()
    ),
    onClick = {}
  )
}

@Composable
private fun OrderRow(order: Order, onClick: (Order) -> Unit) {
  Row(
    Modifier
      .fillMaxWidth()
      .background(AppTheme.colors.lightBackground)
      .clickable { onClick(order) }
      .padding(vertical = 8.dp)) {

    Text(
      text = order.id.toString(), modifier = Modifier.padding(end = 48.dp)
    )
    Column {
      Text(
        text = order.restaurant, Modifier.padding(bottom = 4.dp)
      )

      val formatter = LocalDateTimeFormatter.current

      Text(
        text = formatter.formatShortDateWithDay(order.createdAt),
        color = AppTheme.colors.secondaryText,
        fontSize = 12.sp,
      )
    }
  }
}