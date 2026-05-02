package com.mahozi.sayed.talabiya.order.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.datetime.AppDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.ProvideDateTimeFormatter
import com.mahozi.sayed.talabiya.core.extensions.locale
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.DeleteMenuItem
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaTopBarDefaults
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
  onOpenNavDrawer: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TalabiyaBar(
        title = R.string.app_name,
        navigationIcon = {
          TalabiyaTopBarDefaults.MenuIcon(onOpenNavDrawer)
        }
      )
    },
    floatingActionButton = {
      AddFab {
        onEvent(OrdersEvent.CreateOrderClicked)
      }
    }, floatingActionButtonPosition = FabPosition.End
  ) { paddingValues ->
    Box(
      modifier = modifier
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
    ) {
      Orders(
        orders = state.orders,
        onClick = { onEvent(OrdersEvent.OrderClicked(it)) },
        onDelete = { onEvent(OrdersEvent.DeleteOrderClicked(it)) }
      )
    }
  }
}

@Composable
private fun Orders(
  orders: List<Order>,
  onClick: (Order) -> Unit,
  onDelete: (Order) -> Unit,
) {
  LazyColumn {
    items(orders) { order ->
      OrderRow(
        order = order,
        onClick = { onClick(order) },
        onDelete = { onDelete(order) }
      )
    }
  }
}

@Preview
@Composable
private fun PreviewOrderRow() {
  AppTheme {
    ProvideDateTimeFormatter(AppDateTimeFormatter(LocalContext.current.locale)) {
      OrderRow(
        order = Order(
          id = 1L,
          restaurant = "Tannoor",
          createdAt = Instant.now()
        ),
        onClick = {},
        onDelete = {}
      )
    }
  }
}

@Composable
private fun OrderRow(
  order: Order,
  onClick: () -> Unit,
  onDelete: () -> Unit,
) {
  var showDropdown by remember { mutableStateOf(false) }
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(AppTheme.colors.surface)
      .combinedClickable(
        onClick = { onClick() },
        onLongClick = { showDropdown = true }
      )
      .padding(vertical = 8.dp)
  ) {
    DropdownMenu(
      expanded = showDropdown,
      onDismissRequest = { showDropdown = false },
    ) {
      DeleteMenuItem(
        onDelete = onDelete,
        onDismiss = { showDropdown = false },
      )
    }

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
        color = AppTheme.colors.onSurfaceVariant,
        fontSize = 12.sp,
      )
    }
  }
}