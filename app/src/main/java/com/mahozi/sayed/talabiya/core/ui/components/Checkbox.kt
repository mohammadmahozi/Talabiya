package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import org.w3c.dom.Text


@Preview(showBackground = true)
@Composable
private fun PreviewAppCheckBox() {
  AppTheme {
    AppCheckbox(text = "Test", checked = true, onCheckedChange = {})
  }
}
@OptIn(ExperimentalMaterialApi::class)
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