package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme


@Preview(showBackground = true)
@Composable
private fun PreviewCheckBox() {
  AppTheme {
    TalabiyaCheckbox(text = "Test", checked = true, onCheckedChange = {})
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalabiyaCheckbox(
  text: String,
  checked: Boolean,
  onCheckedChange: ((Boolean) -> Unit),
  modifier: Modifier = Modifier,
) {

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .clickable {
        onCheckedChange(!checked)
      },
  ) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
      Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
      )
    }

    Spacer(modifier = Modifier.width(8.dp))
    Text(text = text)
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewTalabiyaCheckBox() {
  AppTheme {
    TalabiyaCheckbox(true, {}, shape = CircleShape)
  }
}

@Composable
fun TalabiyaCheckbox(
  checked: Boolean,
  onCheckedChange: ((Boolean) -> Unit)?,
  modifier: Modifier = Modifier,
  colors: CheckboxColors = CheckboxDefaults.colors(),
  shape: Shape = RoundedCornerShape(2.dp),
) {


}

@Preview(showBackground = true)
@Composable
private fun PreviewTalabiyaTextCheckbox() {
  AppTheme {
    TalabiyaTextCheckbox(false, {}) {
      Text("Test")
    }
  }
}

@Composable
fun TalabiyaTextCheckbox(
  text: String,
  checked: Boolean,
  onCheckedChange: ((Boolean) -> Unit)?,
  modifier: Modifier = Modifier,
  colors: CheckboxColors = CheckboxDefaults.colors(),
  shape: RoundedCornerShape = RoundedCornerShape(2.dp),
  style: TextStyle = LocalTextStyle.current
) {
  TalabiyaTextCheckbox(
    checked = checked,
    onCheckedChange = onCheckedChange,
    modifier = modifier,
    colors = colors,
    shape = shape
  ) {
    Text(text = text, style = style)
  }
}

@Composable
fun TalabiyaTextCheckbox(
  checked: Boolean,
  onCheckedChange: ((Boolean) -> Unit)?,
  modifier: Modifier = Modifier,
  colors: CheckboxColors = CheckboxDefaults.colors(),
  shape: RoundedCornerShape = RoundedCornerShape(2.dp),
  text: @Composable () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
  ) {
    TalabiyaCheckbox(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = colors,
      shape = shape
    )

    Spacer(modifier = Modifier.width(8.dp))

    text()
  }
}