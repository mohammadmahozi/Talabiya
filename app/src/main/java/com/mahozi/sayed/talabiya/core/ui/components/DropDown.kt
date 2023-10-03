package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupProperties
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme


@Preview(showBackground = true)
@Composable private fun PreviewDropDown() {
  AppTheme {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxSize()
    ) {
      DropDown(value = "Test", items = listOf("Test"), onItemSelected = {}, itemContent = {})

    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DropDown(
  value: String,
  items: List<T>,
  onItemSelected: (T) -> Unit,
  itemContent: @Composable (T) -> Unit,
  modifier: Modifier = Modifier,
  ) {
  var expanded by remember { mutableStateOf(true) }

  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = { expanded = !expanded },
    modifier = modifier
  ) {
    OutlinedTextField(
      value = value,
      onValueChange = { },
      label = { Text(stringResource(R.string.select_restaurant)) },
      readOnly = true,
      trailingIcon = {
        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
      },
      colors = ExposedDropdownMenuDefaults.textFieldColors(
        backgroundColor = AppTheme.colors.material.background,
      ),
      modifier = Modifier
        .fillMaxWidth()
    )

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      properties = PopupProperties(
        focusable = true,
        dismissOnClickOutside = true,
        dismissOnBackPress = true
      ),
      modifier = Modifier
        .exposedDropdownSize()
    ) {
      items.forEach { item ->
        DropdownMenuItem(
          onClick = {
            onItemSelected(item)
            expanded = false
          },
        ) {
          itemContent(item)
        }
      }
    }
  }
}