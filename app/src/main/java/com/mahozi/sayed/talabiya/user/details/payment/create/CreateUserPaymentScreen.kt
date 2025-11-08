package com.mahozi.sayed.talabiya.user.details.payment.create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Ui
import com.mahozi.sayed.talabiya.core.datetime.AppDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.ProvideDateTimeFormatter
import com.mahozi.sayed.talabiya.core.extensions.locale
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.HorizontalSpacer
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaTopBarDefaults
import com.mahozi.sayed.talabiya.core.ui.components.TlbButton
import com.mahozi.sayed.talabiya.core.ui.components.TlbCard
import com.mahozi.sayed.talabiya.core.ui.components.VerticalSpacer
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import kotlinx.parcelize.Parcelize
import java.time.Instant


@Parcelize
data class CreateUserPaymentScreen(
  val userId: Long,
) : Screen

class CreateUserPaymentScreenUi: Ui<CreateUserPaymentState, CreateUserPaymentEvent> {
  @Composable
  override fun Content(
    state: CreateUserPaymentState,
    onEvent: (CreateUserPaymentEvent) -> Unit
  ) {
    CreateUserPaymentScreen(
      state = state,
      onEvent = onEvent,
      onBack = {}
    )
  }
}

@Composable
private fun CreateUserPaymentScreen(
  state: CreateUserPaymentState,
  onEvent: (CreateUserPaymentEvent) -> Unit,
  onBack: () -> Unit,
) {
  Scaffold(
    topBar = {
      TalabiyaBar(
        title = { Text(stringResource(R.string.create_payment_for, state.user))},
        navigationIcon = { TalabiyaTopBarDefaults.BackIcon(onBack) }
      )
    }
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .padding(it)
        .padding(16.dp)
    ) {
      val ordersTitle = when(state.allOrdersSelected) {
        true -> stringResource(R.string.all_orders_selected, state.numberOfSelectedOrders)
        false -> stringResource(R.string.n_orders_selected, state.numberOfSelectedOrders)
      }
      SelectedOrders(
        title = ordersTitle,
        onClick = { onEvent(CreateUserPaymentEvent.SelectOrders)}
      )

      PaymentSummary(
        summary = state.summary
      )

      PaymentTotals(
        totals = state.totals
      )

      VerticalSpacer(1F)

      if (state.showPay) {
        TlbButton(
          text = stringResource(R.string.pay),
          onClick = {},
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSelectedOrders() {
  AppTheme {
    SelectedOrders(
      title = "All orders selected",
      onClick = {}
    )
  }
}

@Composable
private fun SelectedOrders(
  title: String,
  onClick: () -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(AppTheme.shapes.medium)
      .background(AppTheme.colors.primaryContainer)
      .clickable { onClick() }
      .padding(8.dp)
  ) {
    Text(
      text = title
    )
    HorizontalSpacer(1F)
    Icon(
      imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
      contentDescription = stringResource(R.string.view_selected_orders)
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPaymentSummary() {
  AppTheme {
    ProvideDateTimeFormatter(AppDateTimeFormatter(LocalContext.current.locale)) {
      val summary = PaymentSummary(
        from = Instant.now(),
        to = Instant.now(),
        ordersPlaced = 10,
        ordersCovered = 5,
      )
      PaymentSummary(summary)
    }
  }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PaymentSummary(
  summary: PaymentSummary,
) {
  val formatter = LocalDateTimeFormatter.current
  TlbCard(
    title = { TlbCardTitle(R.string.order_summary) }
  ) {
    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      maxItemsInEachRow = 2,
      modifier = Modifier
        .border(width = 1.dp, AppTheme.colors.outlineVariant, AppTheme.shapes.medium)
        .padding(8.dp)
    ) {
      val itemModifier = Modifier.weight(1F)
      LabeledText(
        label = R.string.from,
        text = formatter.formatShortDate(summary.from),
        modifier = itemModifier
      )
      LabeledText(
        label = R.string.to,
        text = formatter.formatShortDate(summary.to),
        modifier = itemModifier
      )
      LabeledText(
        label = R.string.orders_placed,
        text = summary.ordersPlaced.toString(),
        modifier = itemModifier
      )
      LabeledText(
        label = R.string.orders_covered,
        text = summary.ordersCovered.toString(),
        modifier = itemModifier
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPaymentTotals() {
  AppTheme {
    PaymentTotals(
      totals = PaymentTotals(
        placedOrdersTotal = 300.money,
        coveredOrdersTotal = 200.money,
        finalTotal = 100.money
      )
    )
  }
}
@Composable
private fun PaymentTotals(
  totals: PaymentTotals,
) {
  TlbCard(
    title = { TlbCardTitle(R.string.total) }
  ){
    PaymentRow(
      R.string.placed_orders_total,
      totals.placedOrdersTotal.format(),
      AppTheme.colors.primary
    )
    PaymentRow(
      R.string.covered_orders_total,
      totals.coveredOrdersTotal.format(),
      AppTheme.colors.primary
    )
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    PaymentRow(
      R.string.payment_total,
      totals.finalTotal.format(),
      AppTheme.colors.primary
    )
  }
}

@Composable
private fun PaymentRow(
  label: Int,
  total: String,
  totalColor: Color,
) {
  Row(
    modifier = Modifier
      .padding(8.dp)
  ) {
    Text(stringResource(label))
    HorizontalSpacer(1f)
    Text(
      text = total,
      color = totalColor,
      style = AppTheme.type.labelMedium
    )
  }
}

@Composable
private fun LabeledText(
  label: Int,
  text: String,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
  ) {
    Text(
      text = stringResource(label),
      style = AppTheme.type.labelSmall
    )
    VerticalSpacer(8.dp)
    Text(text = text)
  }
}
