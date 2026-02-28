package com.mahozi.sayed.talabiya.user.details.order.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.datetime.AppDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.ProvideDateTimeFormatter
import com.mahozi.sayed.talabiya.core.extensions.locale
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.ui.components.HorizontalSpacer
import com.mahozi.sayed.talabiya.core.ui.components.TlbCard
import com.mahozi.sayed.talabiya.core.ui.components.TlbIcon
import com.mahozi.sayed.talabiya.core.ui.components.TlbText
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.core.ui.theme.onSurfaceVariant
import com.mahozi.sayed.talabiya.user.details.payment.create.UnpaidOrder
import java.time.Instant

data class SelectUnpaidOrderState(
  val orders: List<UnpaidOrder>,
)

sealed interface SelectUnpaidOrdersEvent {
  data class SelectOrder(val order: UnpaidOrder) : SelectUnpaidOrdersEvent
  object Dismiss : SelectUnpaidOrdersEvent
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectUnpaidOrdersDialog(
  state: SelectUnpaidOrderState,
  onEvent: (SelectUnpaidOrdersEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = { onEvent(SelectUnpaidOrdersEvent.Dismiss) },
    modifier = modifier,
  ) {
    SelectUnpaidOrdersScreen(
      state = state,
      onEvent = onEvent
    )
  }
}

@Preview
@Composable
private fun PreviewUnpaidOrdersScreen() {
  AppTheme {
    val order = UnpaidOrder(
      orderId = 1L,
      createdAt = Instant.now(),
      restaurant = "Restaurant Name",
      fullOrderTotal = 100.money,
      userOrderTotal = 50.money,
      selected = false
    )
    ProvideDateTimeFormatter(AppDateTimeFormatter(LocalContext.current.locale)) {
      SelectUnpaidOrdersScreen(
        state = SelectUnpaidOrderState(listOf(order)),
        onEvent = {},
      )
    }
  }
}

@Composable
private fun SelectUnpaidOrdersScreen(
  state: SelectUnpaidOrderState,
  onEvent: (SelectUnpaidOrdersEvent) -> Unit,
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    modifier = Modifier.heightIn(max = LocalWindowInfo.current.containerDpSize.height * .8F)
  ) {
    items(state.orders) {
      UnpaidOrder(
        order = it,
        onClick = { onEvent(SelectUnpaidOrdersEvent.SelectOrder(it)) },
      )
    }
  }
}

@Composable
internal fun UnpaidOrder(
  order: UnpaidOrder,
  onClick: () -> Unit,
) {
  TlbCard(
    border = BorderStroke(
      width = 1.dp,
      color = if (order.selected) AppTheme.colors.primary else AppTheme.colors.surfaceContainer
    ),
    modifier = Modifier
      .clickable(onClick = onClick)
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = order.orderId.toString(),
      )

      Column {
        Row {
          Text(
            text = order.restaurant,
          )
          HorizontalSpacer(1F)
          TlbText(
            text = order.userOrderTotal.format(),
            style = AppTheme.type.bodySmall,
            leadingIcon = {
              TlbIcon(
                painter = painterResource(R.drawable.arrow_forward_rounded),
                contentDescription = stringResource(R.string.expense)
              )
            }
          )
          HorizontalSpacer(16.dp)
          TlbText(
            text = order.fullOrderTotal.format(),
            style = AppTheme.type.bodySmall,
            leadingIcon = {
              TlbIcon(
                painter = painterResource(R.drawable.arrow_back_rounded),
                contentDescription = stringResource(R.string.income)
              )
            }
          )
        }
        val formatter = LocalDateTimeFormatter.current
        Text(
          text = formatter.formatShortDateWithDay(order.createdAt),
          style = AppTheme.type.bodyMedium.onSurfaceVariant,
        )
      }
    }
  }
}