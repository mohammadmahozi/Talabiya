package com.mahozi.sayed.talabiya.core.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme


@Composable fun rememberConfirmState(): MutableState<ConfirmState?> =
  remember { mutableStateOf(null as ConfirmState?) }

data class ConfirmState(
  val text: LocalString,
  val onDismiss: () -> Unit,
  val onConfirm: () -> Unit
)

@Composable
fun ConfirmDialog(
  state: ConfirmState,
  modifier: Modifier = Modifier,
  confirmText: String = stringResource(R.string.confirm),
  confirmContainerColor: Color = AppTheme.colors.primary,
  confirmTextColor: Color = AppTheme.colors.surface,
  cancelText: String = stringResource(R.string.cancel),
  cancelBorderColor: Color = AppTheme.colors.primary,
  cancelTextColor: Color = AppTheme.colors.primary,
) {
  ConfirmDialog(
    text = state.text.value,
    onDismiss = state.onDismiss,
    onConfirm = state.onConfirm,
    modifier = modifier,
    confirmText = confirmText,
    confirmContainerColor = confirmContainerColor,
    confirmTextColor = confirmTextColor,
    cancelText = cancelText,
    cancelBorderColor = cancelBorderColor,
    cancelTextColor = cancelTextColor,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDialog(
  text: String,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit,
  modifier: Modifier = Modifier,
  confirmText: String = stringResource(R.string.confirm),
  confirmContainerColor: Color = AppTheme.colors.primary,
  confirmTextColor: Color = AppTheme.colors.onPrimary,
  cancelText: String = stringResource(R.string.cancel),
  cancelBorderColor: Color = AppTheme.colors.primary,
  cancelTextColor: Color = AppTheme.colors.primary,
) {
  BasicAlertDialog(onDismissRequest = onDismiss) {
    Column(
      modifier = modifier
        .background(AppTheme.colors.surface, RoundedCornerShape(10.dp))
        .padding(16.dp)
    ) {
      Text(text = text)

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
          .fillMaxWidth()
      ) {

        DialogOutlinedButton(
          text = cancelText,
          onClick = onDismiss,
          borderColor = cancelBorderColor,
          contentColor = cancelTextColor
        )
        Spacer(modifier = Modifier.width(8.dp))

        DialogButton(
          text = confirmText,
          onClick = onConfirm,
          containerColor = confirmContainerColor,
          contentColor = confirmTextColor
        )

      }
    }
  }
}

@Composable
private fun DialogButton(
  text: String,
  onClick: () -> Unit,
  containerColor: Color,
  contentColor: Color,
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
      containerColor = containerColor,
      contentColor = contentColor,
    )
  ) {
    Text(
      text = text
    )
  }
}

@Composable
private fun DialogOutlinedButton(
  text: String,
  onClick: () -> Unit,
  borderColor: Color,
  contentColor: Color,
) {
  OutlinedButton(
    onClick = onClick,
    border = BorderStroke(1.dp, borderColor)
  ) {
    Text(
      text = text,
      color = contentColor
    )
  }
}

@Preview
@Composable
private fun PreviewConfirmDialog() {
  AppTheme {
    ConfirmDialog(
      text = "Are you sure?",
      onDismiss = {},
      onConfirm = {}
    )
  }
}