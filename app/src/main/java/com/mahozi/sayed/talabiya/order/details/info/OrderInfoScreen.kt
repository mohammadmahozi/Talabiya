package com.mahozi.sayed.talabiya.order.details.info

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.colors
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsEvent
import com.mahozi.sayed.talabiya.order.details.tabs.OrderInfoState
import java.time.LocalDate
import java.time.LocalTime

@Preview(showBackground = true)
@Composable
fun PreviewOrderInfoScreen() {
  OrderInfoScreen(
      model = OrderInfoState(
          LocalDate.now(),
          LocalTime.now(),
          60.0,
          "mmm",
          OrderStatus.COMPLETE
      ),
      onEvent = { }
  )
}

@Composable
fun OrderInfoScreen(
    model: OrderInfoState,
    onEvent: (OrderDetailsEvent.OrderInfoEvent) -> Unit,
) {
  Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(16.dp)
  ) {
    InfoTextRow(
        text = "date",
        icon = R.drawable.ic_date,
        iconDescription = R.string.date,
    ) {}

    InfoTextRow(
        text = "time",
        icon = R.drawable.ic_time,
        iconDescription = R.string.time
    ) {}

    Divider(color = colors.lightBorder)

    InfoTextRow(
        text = "300",
        icon = R.drawable.ic_money,
        iconDescription = R.string.total
    ) {}

    InfoTextRow(
        text = "Not selected",
        icon = R.drawable.ic_payer,
        iconDescription = R.string.payer
    ) {}

    InfoTextRow(
        text = "in progress",
        icon = R.drawable.ic_hourglass,
        iconDescription = R.string.status
    ) {

    }
    Divider(color = colors.lightBorder)

    InfoRow(
        icon = R.drawable.ic_baseline_notes_24,
        iconDescription = R.string.note,
        onclick = {}) {

      BasicTextField(
          value = "note",
          onValueChange = {}
      )
    }
  }
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
fun InfoTextRow(
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
        color = colors.primaryText
    )
  }

}

@Composable
fun InfoRow(
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
        colorFilter = ColorFilter.tint(colors.primaryText),
        modifier = Modifier
            .size(24.dp)
    )

    Spacer(
        modifier = Modifier.width(16.dp)
    )

    content()
  }
}