package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Preview
@Composable private fun PreviewConfirmDialog() {
  ConfirmDialog(
    title = "Are you sure?",
    text = "Are you really sure?",
    onConfirm = {},
    onDismiss = {}
  )
}
@Composable fun ConfirmDialog(
  onConfirm: () -> Unit,
  onDismiss: () -> Unit,
  title: String,
  text: String? = null,
  ) {
  AlertDialog(
    title = {
      Text(
        text = title,
        style = AppTheme.types.title
      )
    },
    text = {
      if (text != null) {
        Text(
          text = text,
          style = AppTheme.types.subtitle
        )
      }
    },
    onDismissRequest = { onDismiss() },
    confirmButton = {
      TextButton(
        onClick = {
          onConfirm()
        }
      ) {
        Text(stringResource(R.string.confirm))
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          onDismiss()
        }
      ) {
        Text(stringResource(R.string.cancel))
      }
    }


  )
}