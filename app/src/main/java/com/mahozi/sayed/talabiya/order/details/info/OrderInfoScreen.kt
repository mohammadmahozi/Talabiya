package com.mahozi.sayed.talabiya.order.details.info

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Preview
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.ui.components.TlbDatePickerDialog
import com.mahozi.sayed.talabiya.core.ui.components.TlbTimePickerDialog
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.details.OrderDetailsEvent.OrderInfoEvent
import com.mahozi.sayed.talabiya.order.details.OrderInfoState
import com.mahozi.sayed.talabiya.order.title
import java.time.Instant

@Preview(showBackground = true)
@Composable
private fun PreviewOrderInfoScreen() {
  Preview {
    OrderInfoScreen(
      model = OrderInfoState(
        datetime = Instant.now(),
        total = 60.0.money,
        payer = "mmm",
        status = OrderStatus.COMPLETE,
        note = "Note",
        datePickerState = null,
        timePickerState = null,
      ),
      onEvent = { }
    )
  }
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
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    if (model.datePickerState != null) {
      TlbDatePickerDialog(
        state = model.datePickerState,
        onEvent = { onEvent(OrderInfoEvent.DateEvent(it)) },
      )
    }

    if (model.timePickerState != null) {
      TlbTimePickerDialog(
        state = model.timePickerState,
        onEvent = { onEvent(OrderInfoEvent.TimeEvent(it)) }
      )
    }

    InfoTextRow(
      text = formatter.formatShortDateWithDay(model.datetime),
      icon = R.drawable.ic_date,
      iconDescription = R.string.date,
      onclick = { onEvent(OrderInfoEvent.DateClicked) }
    )

    InfoTextRow(
      text = formatter.formatTime(model.datetime),
      icon = R.drawable.ic_time,
      iconDescription = R.string.time,
      onclick = { onEvent(OrderInfoEvent.TimeClicked) }
    )

    HorizontalDivider()

    InfoTextRow(
      text = model.total.format(),
      icon = R.drawable.ic_money,
      iconDescription = R.string.total,
      onclick = {}
    )

    InfoTextRow(
      text = model.payer ?: string(R.string.select_payer),
      icon = R.drawable.ic_payer,
      iconDescription = R.string.payer,
      onclick = { onEvent(OrderInfoEvent.PayerClicked) }
    )

    InfoTextRow(
      text = string(model.status.title),
      icon = R.drawable.ic_hourglass,
      iconDescription = R.string.status,
      onclick = { onEvent(OrderInfoEvent.StatusClicked) }
    )

    HorizontalDivider()

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
}

@Preview
@Composable
private fun PreviewRow() {
  InfoTextRow(
    text = "Test",
    icon = R.drawable.ic_date,
    iconDescription = R.string.date,
    onclick = { }
  )
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
      color = AppTheme.colors.onSurface
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
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onclick() }
      .padding(vertical = 8.dp)
  ) {
    Image(
      painter = painterResource(icon),
      contentDescription = string(iconDescription),
      colorFilter = ColorFilter.tint(AppTheme.colors.onSurface),
      modifier = Modifier
        .size(24.dp)
    )

    Spacer(modifier = Modifier.width(16.dp))

    content()
  }
}