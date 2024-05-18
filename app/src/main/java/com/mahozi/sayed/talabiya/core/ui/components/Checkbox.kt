package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme


@Preview(showBackground = true)
@Composable
private fun PreviewAppCheckBox() {
  AppTheme {
    AppCheckbox(text = "Test", checked = true, onCheckedChange = {})
  }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCheckbox(
  text: String,
  checked: Boolean,
  onCheckedChange: ((Boolean) -> Unit),
  modifier: Modifier = Modifier,
) {

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier,
  ) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
      Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange
      )
    }

    Text(text = text)
  }
}