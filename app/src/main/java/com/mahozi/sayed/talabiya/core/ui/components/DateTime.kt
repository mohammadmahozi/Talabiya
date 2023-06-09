package com.mahozi.sayed.talabiya.core.ui.components

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import java.time.LocalDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable fun DateField(
  selectedDate: LocalDate,
  onDateSelected: (LocalDate) -> Unit,
  modifier: Modifier = Modifier,
) {

  val formatter = LocalDateTimeFormatter.current

  var showDialog by remember { mutableStateOf(false) }

  IconText(
    text = formatter.formatShortDateWithDay(selectedDate),
    painter = painterResource(R.drawable.ic_date),
    contentDescription = stringResource(R.string.select_date),
    modifier = Modifier
      .clickable { showDialog = true }
      .padding(vertical = 8.dp)
      .fillMaxWidth()
  )

  if (showDialog) {
    DatePickerDialog(
      onConfirm = onDateSelected,
      onDismiss = { showDialog = false }
    )
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewDatePickerDialog() {
  AppTheme {
    DatePickerDialog({}, {})
  }
}

@Composable
fun DatePickerDialog(
  onConfirm: (LocalDate) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  var date = LocalDate.now()

  Dialog(onDismissRequest = { onDismiss() }) {
    Column(
      modifier = modifier
        .background(
          AppTheme.colors.material.background,
          shape = RoundedCornerShape(5.dp)
        )
    ) {
      Calendar(date) {
        date = it
      }

      Row(
        modifier = Modifier.align(Alignment.End)
      ) {
        DialogTextButton(text = R.string.confirm) {
          onConfirm(date)
        }
        Spacer(modifier = Modifier.width(16.dp))
        DialogTextButton(text = R.string.cancel, onClick = onDismiss)
        Spacer(modifier = Modifier.width(24.dp))
      }
    }

  }
}

@Composable
private fun DialogTextButton(
  @StringRes text: Int,
  onClick: () -> Unit
) {
  TextButton(
    onClick = onClick,
  ) {
    Text(
      text = string(text),
      color = AppTheme.colors.material.onSurface
    )
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalendar() {
  Calendar(LocalDate.now()) {}
}

@Composable
fun Calendar(
  selectedDate: LocalDate,
  onDateSelected: (LocalDate)  -> Unit
) {
  AndroidView(
    factory = { context ->
      CalendarView(ContextThemeWrapper(context, R.style.CalenderView)).apply {
        date = selectedDate.toEpochDay()
      }
    },
    update = { view ->
      view.date = selectedDate.toEpochDay()
      view.setOnDateChangeListener { _, year, month, dayOfMonth ->
        val date = LocalDate.of(year, month + 1, dayOfMonth)
        onDateSelected(date)
      }
    }
  )
}