package com.mahozi.sayed.talabiya.core.ui.components

import android.view.ContextThemeWrapper
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Preview
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import java.time.LocalTime
import android.widget.TimePicker as TimePickerView

@Preview(showBackground = true)
@Composable
private fun PreviewTimeField() {
  Preview {
    TimeField(selectedTime = LocalTime.now(), onTimeSelected = {})
  }
}
@Composable
fun TimeField(
  selectedTime: LocalTime,
  onTimeSelected: (LocalTime) -> Unit,
  modifier: Modifier = Modifier,
  padding: PaddingValues = PaddingValues()
) {
  val formatter = LocalDateTimeFormatter.current

  var showDialog by remember { mutableStateOf(false) }

  IconText(
    text = formatter.formatTime(selectedTime),
    painter = painterResource(R.drawable.ic_time),
    contentDescription = stringResource(R.string.select_date),
    modifier = modifier
      .clickable { showDialog = true }
      .padding(padding)
      .fillMaxWidth()
  )

  if (showDialog) {
    TimePickerDialog(
      selectedTime,
      onConfirm = onTimeSelected,
      onDismiss = { showDialog = false }
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimePicker() {
  TimePickerDialog(
    selectedTime = LocalTime.now(),
    onConfirm = {},
    onDismiss = {}
  )
}
@Composable
fun TimePickerDialog(
  selectedTime: LocalTime,
  onConfirm: (LocalTime) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {

  var time = selectedTime

  Dialog(onDismissRequest = { onDismiss() }) {
    Column(
      modifier = modifier
        .background(
          AppTheme.colors.material.background,
          shape = AppTheme.shapes.small
        )
    ) {
      TimePicker(time) {
        time = it
      }

      Row(
        modifier = Modifier.align(Alignment.End)
      ) {
        DialogTextButton(text = R.string.confirm) {
          onConfirm(time)
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

@Composable
private fun TimePicker(
  selectedTime: LocalTime,
  onTimeSelected: (LocalTime) -> Unit,
) {
  AndroidView(
    factory = { context ->
      TimePickerView(ContextThemeWrapper(context, R.style.TimePickerView)).apply {
        setOnTimeChangedListener { _, hourOfDay, minute ->
          onTimeSelected(LocalTime.of(hourOfDay, minute))
        }
      }
    },
    update = {
      it.hour = selectedTime.hour
      it.minute = selectedTime.minute
    }
  )
}