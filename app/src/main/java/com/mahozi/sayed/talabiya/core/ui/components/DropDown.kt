package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDown(
  value: String,
  items: List<T>,
  onItemSelected: (T) -> Unit,
  itemContent: @Composable (T) -> Unit,
  modifier: Modifier = Modifier,
  startExpanded: Boolean = true,
  ) {
  var expanded by remember { mutableStateOf(startExpanded) }

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
        focusedContainerColor = AppTheme.colors.material.background,
        unfocusedContainerColor = AppTheme.colors.material.background,
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
          text = {
            itemContent(item)
          },
          onClick = {
            onItemSelected(item)
            expanded = false
          },
        )
      }
    }
  }
}