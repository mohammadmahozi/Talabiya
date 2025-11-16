package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Preview
@Composable
private fun PreviewTlbTabRow() {
  AppTheme {
    TlbTabRow(
      selectedTabIndex = 0,
      tabs = {
        TlbTab(
          selected = true,
          onClick = {},
          text = { Text("Orders") }
        )
        TlbTab(
          selected = false,
          onClick = {},
          text = { Text("Products") }
        )
        TlbTab(
          selected = false,
          onClick = {},
          text = { Text("Customers") }
        )
      }
    )
  }
}
@Composable
fun TlbTabRow(
  selectedTabIndex: Int,
  modifier: Modifier = Modifier,
  containerColor: Color = AppTheme.colors.surface,
  contentColor: Color = AppTheme.colors.onSurface,
  tabs: @Composable () -> Unit
  ) {
  TabRow(
    selectedTabIndex = selectedTabIndex,
    containerColor = containerColor,
    contentColor = contentColor,
    indicator = @Composable { tabPositions ->
      TabRowDefaults.SecondaryIndicator(
        modifier = Modifier
          .tabIndicatorOffset(tabPositions[selectedTabIndex]),
        color = AppTheme.colors.material.primary
      )
    },
    tabs = tabs,
    modifier = modifier
  )
}

@Composable
fun TlbTab(
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  text: @Composable (() -> Unit)? = null,
  icon: @Composable (() -> Unit)? = null,
  selectedContentColor: Color = LocalContentColor.current,
  unselectedContentColor: Color = selectedContentColor,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
  Tab(
    selected = selected,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    text = text,
    icon = icon,
    selectedContentColor = selectedContentColor,
    unselectedContentColor = unselectedContentColor,
    interactionSource = interactionSource
  )
}