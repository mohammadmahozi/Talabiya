package com.mahozi.sayed.talabiya.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Preview
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import java.time.LocalTime

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

  TlbText(
    text = formatter.formatTime(selectedTime),
    leadingIcon = {
      TlbIcon(
        painter = painterResource(R.drawable.ic_time),
        contentDescription = stringResource(R.string.select_date)
      )
    },
    modifier = modifier
      .clickable { showDialog = true }
      .padding(padding)
      .fillMaxWidth()
  )

  if (showDialog) {
    TlbTimePickerDialog(
      onDismissRequest = { showDialog = false },
      onTimeSelected = onTimeSelected
    )
  }
}

data class TlbTimePickerState(
  val initialTime: LocalTime = LocalTime.now(),
)

sealed interface TlbTimePickerEvent {
  data class SelectTime(val time: LocalTime) : TlbTimePickerEvent
  object Dismiss : TlbTimePickerEvent
}

@Composable
fun TlbTimePickerDialog(
  state: TlbTimePickerState,
  onEvent: (TlbTimePickerEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  TlbTimePickerDialog(
    initialTime = state.initialTime,
    onDismissRequest = { onEvent(TlbTimePickerEvent.Dismiss) },
    onTimeSelected = { onEvent(TlbTimePickerEvent.SelectTime(it)) },
    modifier = modifier
  )
}

@Composable
fun TlbTimePickerDialog(
  onTimeSelected: (LocalTime) -> Unit,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  initialTime: LocalTime = LocalTime.now(),
) {
  var time by remember { mutableStateOf(initialTime) }

  AlertDialog(
    modifier = modifier,
    onDismissRequest = onDismissRequest,
    title = {
      Text(
        text = stringResource(R.string.select_time)
      )
    },
    text = {
      TlbTimePicker(
        onTimeChanged = {
          time = it
        },
        initialTime = initialTime
      )
    },
    confirmButton = {
      DialogTextButton(
        text = R.string.confirm,
        onClick = {
          onTimeSelected(time)
          onDismissRequest()
        }
      )
    },
    dismissButton = {
      DialogTextButton(
        text = R.string.cancel,
        onClick = onDismissRequest
      )
    }
  )
}

@Preview
@Composable
private fun PreviewTlbTimePicker() {
  MaterialTheme {
    TlbTimePicker(
      onTimeChanged = {}
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TlbTimePicker(
  onTimeChanged: (LocalTime) -> Unit,
  modifier: Modifier = Modifier,
  initialTime: LocalTime = LocalTime.now(),
) {
  val state = rememberTimePickerState(
    initialHour = initialTime.hour,
    initialMinute = initialTime.minute,
    is24Hour = false,
  )

  LaunchedEffect(state.hour, state.minute) {
    val time = LocalTime.of(state.hour, state.minute)
    onTimeChanged(time)
  }

  TimePicker(
    state = state,
    modifier = modifier
  )
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