package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mahozi.sayed.talabiya.R


@Composable fun DeleteContextMenu(
  expanded: Boolean,
  onDelete: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var confirm by remember { mutableStateOf(false) }

  DropdownMenu(
    expanded = expanded,
    onDismissRequest = onDismiss,
    modifier = modifier,
  ) {
    DropdownMenuItem(
      text = { Text(stringResource(R.string.delete)) },
      onClick = {
        confirm = true
        onDismiss()
      }
    )
  }

  if (confirm) {
    ConfirmDialog(
      title = stringResource(R.string.are_you_sure),
      onConfirm = {
        confirm = false
        onDelete()
      },
      onDismiss = {
        confirm = false
        onDismiss()
      }
    )
  }
}