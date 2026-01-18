package com.mahozi.sayed.talabiya.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun DateField(
  selectedDate: LocalDate,
  onDateSelected: (LocalDate) -> Unit,
  modifier: Modifier = Modifier,
  padding: PaddingValues = PaddingValues(),
) {
  val formatter = LocalDateTimeFormatter.current

  var showDialog by remember { mutableStateOf(false) }

  TlbText(
    text = formatter.formatShortDateWithDay(selectedDate),
    leadingIcon = {
      TlbIcon(
        painter = painterResource(R.drawable.ic_date),
        contentDescription = stringResource(R.string.select_date)
      )
    },
    modifier = modifier
      .clickable { showDialog = true }
      .padding(padding)
      .fillMaxWidth()
  )

  if (showDialog) {
    TlbDatePickerDialog(
      initial = selectedDate,
      onDateSelected = {
        onDateSelected(it)
        showDialog = false
      },
      onDismissRequest = { showDialog = false }
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDatePickerDialog() {
  AppTheme {
    TlbDatePickerDialog(
      initial = LocalDate.now(),
      onDateSelected = {},
      onDismissRequest = {}
    )
  }
}

data class TlbDatePickerState(
  val initial: LocalDate = LocalDate.now(),
  val minDate: LocalDate = LocalDate.now(),
  val maxDate: LocalDate = LocalDate.MAX,
)
sealed interface TlbDatePickerEvent {
  data class SelectDate(val date: LocalDate) : TlbDatePickerEvent
  object Dismiss : TlbDatePickerEvent
}

@Composable
fun TlbDatePickerDialog(
  state: TlbDatePickerState,
  onEvent: (TlbDatePickerEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  TlbDatePickerDialog(
    initial = state.initial,
    onDateSelected = { onEvent(TlbDatePickerEvent.SelectDate(it)) },
    onDismissRequest = { onEvent(TlbDatePickerEvent.Dismiss) },
    minDate = state.minDate,
    maxDate = state.maxDate,
    modifier = modifier
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TlbDatePickerDialog(
  initial: LocalDate,
  onDateSelected: (LocalDate) -> Unit,
  onDismissRequest: () -> Unit,
  modifier: Modifier = Modifier,
  minDate: LocalDate = LocalDate.now(),
  maxDate: LocalDate = LocalDate.MAX,
) {
  val initialOrMin = if (initial.isBefore(minDate)) minDate else initial

  val initialMillis = initialOrMin
    .atTime(LocalTime.now())
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

  val state = rememberDatePickerState(
    initialSelectedDateMillis = initialMillis,
    selectableDates = object : SelectableDates {
      override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val date = Instant
          .ofEpochMilli(utcTimeMillis)
          .atZone(ZoneId.systemDefault())
          .toLocalDate()

        return (date.isAfter(minDate) || date.isEqual(minDate)) && (date.isBefore(maxDate) || date.isEqual(
          maxDate
        ))
      }
    }
  )

  Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = modifier
        .background(AppTheme.colors.surfaceContainerHigh, RoundedCornerShape(10.dp))
        .padding(vertical = 16.dp)
        .fillMaxWidth(.95F)
    ) {
      DatePicker(
        state = state,
        title = null,
        headline = null,
        showModeToggle = false,
        modifier = Modifier.fillMaxWidth()
      )

      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.align(Alignment.End)
      ) {
        DialogTextButton(text = R.string.cancel, onClick = onDismissRequest)
        DialogTextButton(text = R.string.confirm) {
          if (state.selectedDateMillis != null) {
            val date = Instant
              .ofEpochMilli(state.selectedDateMillis!!)
              .atZone(ZoneOffset.UTC)
              .toLocalDate()

            onDateSelected(date)
          }
        }
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