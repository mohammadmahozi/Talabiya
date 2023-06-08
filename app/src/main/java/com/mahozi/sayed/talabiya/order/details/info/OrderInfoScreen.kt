package com.mahozi.sayed.talabiya.order.details.info

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.ui.components.DatePickerDialog
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsEvent.OrderInfoEvent
import com.mahozi.sayed.talabiya.order.details.tabs.OrderInfoState
import com.mahozi.sayed.talabiya.order.title
import java.time.Instant

@Preview(showBackground = true)
@Composable
fun PreviewOrderInfoScreen() {
  OrderInfoScreen(
    model = OrderInfoState(
      Instant.now(),
      60.0,
      "mmm",
      OrderStatus.COMPLETE,
      "Note",
      false
    ),
    onEvent = { }
  )
}

@Composable
fun OrderInfoScreen(
  model: OrderInfoState,
  onEvent: (OrderInfoEvent) -> Unit,
  modifier: Modifier = Modifier
) {

  val formatter = LocalDateTimeFormatter.current

  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier.padding(16.dp)
  ) {
    InfoTextRow(
      text = formatter.formatShortDateWithDay(model.datetime),
      icon = R.drawable.ic_date,
      iconDescription = R.string.date,
    ) { onEvent(OrderInfoEvent.DateClicked) }

    InfoTextRow(
      text = formatter.formatTime(model.datetime),
      icon = R.drawable.ic_time,
      iconDescription = R.string.time
    ) { onEvent(OrderInfoEvent.TimeClicked) }

    Divider(color = AppTheme.colors.lightBorder)

    InfoTextRow(
      text = model.total.toString(),
      icon = R.drawable.ic_money,
      iconDescription = R.string.total
    ) {}

    InfoTextRow(
      text = model.payer ?: string(R.string.select_payer),
      icon = R.drawable.ic_payer,
      iconDescription = R.string.payer
    ) { onEvent(OrderInfoEvent.PayerClicked) }

    InfoTextRow(
      text = string(model.status.title),
      icon = R.drawable.ic_hourglass,
      iconDescription = R.string.status
    ) { onEvent(OrderInfoEvent.StatusClicked) }

    Divider(color = AppTheme.colors.lightBorder)

    InfoRow(
      icon = R.drawable.ic_baseline_notes_24,
      iconDescription = R.string.note,
      onclick = {}
    ) {

      BasicTextField(
        value = model.note,
        onValueChange = { note -> onEvent(OrderInfoEvent.NoteChanged(note)) }
      )
    }
  }
  if (model.datePickerVisible) DatePickerDialog(
    onConfirm = { onEvent(OrderInfoEvent.DateSelected(it)) },
    onDismiss = { onEvent(OrderInfoEvent.DateDialogDismissed) }
  )
}

@Preview
@Composable
fun PreviewRow() {
  InfoTextRow(
    text = "Test",
    R.drawable.ic_date,
    R.string.date,
  ) { }
}

@Composable
private fun InfoTextRow(
  text: String,
  @DrawableRes icon: Int,
  @StringRes iconDescription: Int,
  onclick: () -> Unit,
) {
  InfoRow(
    icon,
    iconDescription,
    onclick
  ) {
    Text(
      text = text,
      color = AppTheme.colors.primaryText
    )
  }

}

@Composable
private fun InfoRow(
  @DrawableRes icon: Int,
  @StringRes iconDescription: Int,
  onclick: () -> Unit,
  content: @Composable () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onclick() }
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Image(
      painter = painterResource(icon),
      contentDescription = string(iconDescription),
      colorFilter = ColorFilter.tint(AppTheme.colors.primaryText),
      modifier = Modifier
        .size(24.dp)
    )

    Spacer(
      modifier = Modifier.width(16.dp)
    )

    content()
  }
}