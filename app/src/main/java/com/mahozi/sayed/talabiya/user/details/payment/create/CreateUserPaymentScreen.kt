package com.mahozi.sayed.talabiya.user.details.payment.create

import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaOutlinedTextField
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.components.HorizontalSpacer
import com.mahozi.sayed.talabiya.core.ui.components.VerticalSpacer
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Composable
private fun CreateUserPaymentScreen(

) {
  Column {
    TalabiyaOutlinedTextField(
      value = "50", //default is order total - balance - order he paid for
      onValueChange = {},
      placeholder = {
        Text(text = stringResource(R.string.enter_amount))
      }
    )

    //Some way to select orders. All by default.

    //Button -> "Pay"
  }
}

@Preview(showBackground = true)
@Composable
private fun SelectedOrders(
  
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(AppTheme.shapes.medium)
      .padding(8.dp)
  ) {
    Text("All orders selected (52), 50 orders selected")
    HorizontalSpacer(1F)
    Icon(
      imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
      contentDescription = stringResource(R.string.view_selected_orders)
    )
  }
}

@Preview(showBackground = true)
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OrderSummary(

) {
  Column {
    Text(stringResource(R.string.order_summary))
    VerticalSpacer(8.dp)


    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      maxItemsInEachRow = 2,
      modifier = Modifier
        .border(width = 1.dp, AppTheme.colors.outlineVariant, AppTheme.shapes.medium)
        .padding(8.dp)
    ) {
      val itemModifier = Modifier.weight(1F)
      LabeledText(R.string.from, "2023-01-01", modifier = itemModifier)
      LabeledText(R.string.to, "2024-01-01", modifier = itemModifier)
      LabeledText(R.string.orders_placed, "50", modifier = itemModifier)
      LabeledText(R.string.orders_total, "200", modifier = itemModifier)
      LabeledText(R.string.orders_covered, "2", modifier = itemModifier)
      LabeledText(R.string.total_paid, "100", modifier = itemModifier)
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PaymentSummary() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
  ) {
    PaymentRow(
      R.string.covered_orders_total,
      "+ 200",
      AppTheme.colors.primary
    )

    PaymentRow(
      R.string.placed_orders_total,
      "- 300",
      AppTheme.colors.primary
    )

    PaymentRow(
      R.string.orders_net_total,
      "100",
      AppTheme.colors.primary
    )

    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

    PaymentRow(
      R.string.payment_total,
      "500",
      AppTheme.colors.primary
    )
    PaymentRow(
      R.string.balance_used,
      "0",
      AppTheme.colors.primary
    )

    PaymentRow(
      R.string.balance_recharge,
      "400",
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
        style = AppTheme.typography.labelMedium
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
        style = AppTheme.typography.labelSmall
        )
      VerticalSpacer(8.dp)
      Text(text = text)
    }
}