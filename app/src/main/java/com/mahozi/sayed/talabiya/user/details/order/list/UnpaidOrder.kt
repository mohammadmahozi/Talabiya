package com.mahozi.sayed.talabiya.user.details.order.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.mahozi.sayed.talabiya.core.ui.components.TlbText
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.core.ui.theme.onSurfaceVariant
import com.mahozi.sayed.talabiya.user.details.payment.create.UnpaidOrder
import java.time.Instant

@Preview
@Composable
private fun PreviewUnpaidOrder() {
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
      UnpaidOrder(
        order = order,
        onClick = {},
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
              TlbTextIcon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = stringResource(R.string.expense)
              )
            }
          )
          HorizontalSpacer(16.dp)
          TlbText(
            text = order.fullOrderTotal.format(),
            style = AppTheme.type.bodySmall,
            leadingIcon = {
              TlbTextIcon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.expense)
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