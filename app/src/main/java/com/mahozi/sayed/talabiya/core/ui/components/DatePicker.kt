package com.mahozi.sayed.talabiya.core.ui.components

import android.annotation.SuppressLint
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.colors
import java.time.LocalDate


@Preview(showBackground = true)
@Composable
fun PreviewDatePickerDialog() {
  DatePickerDialog({}, {})
}

@SuppressLint("ComposeModifierMissing")
@Composable
fun DatePickerDialog(
  onConfirm: (LocalDate) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  Dialog(onDismissRequest = { onDismiss() }) {
    Column(
      modifier = modifier.background(colors.material.background, shape = RoundedCornerShape(5.dp))
    ) {
      Calendar()

      Row(
        modifier = Modifier.align(Alignment.End)
      ) {
        DialogTextButton(text = R.string.confirm) {

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
  TextButton(onClick = onClick) {
    Text(
      string(text)
    )
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalendar() {
  Calendar()
}

@Composable
fun Calendar() {
  AndroidView(
    factory = { context ->
      CalendarView(ContextThemeWrapper(context, R.style.CalenderView))
    },
    update = { view ->
      view.setOnDateChangeListener { _, year, month, dayOfMonth ->
        val date = LocalDate.of(year, month + 1, dayOfMonth)
      }
    }
  )
}