package com.mahozi.sayed.talabiya.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Preview
@Composable
fun PreviewBar() {
  AppTheme {
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      TalabiyaBar(
        title = R.string.app_name,
      )

      TalabiyaBar(
        title = R.string.app_name,
        navigationIcon = { TalabiyaTopBarDefaults.BackIcon(onClick = { }) },
        actions = {
          TalabiyaIconButton(
            imageVector = Icons.Default.Call,
            contentDescription = null,
            onClick = {}
          )
        },
        overFlowActions = {
          DropdownMenuItem(
            text = {
              Text(text = "Test")
            },
            onClick = { /*TODO*/ }
          )
        }
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalabiyaBar(
  @StringRes title: Int,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  overFlowActions: (@Composable ColumnScope.() -> Unit)? = null
) {
  TalabiyaBar(
    title = { Text(text = string(title)) },
    modifier = modifier,
    navigationIcon = navigationIcon,
    actions = actions,
    overFlowActions = overFlowActions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalabiyaBar(
  title: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  overFlowActions: (@Composable ColumnScope.() -> Unit)? = null
) {
  var showMenu by remember { mutableStateOf(false) }

  TopAppBar(
    title = {
      title()
    },
    navigationIcon = navigationIcon,
    actions = {
      actions()

      if (overFlowActions != null) {
        TalabiyaIconButton(
          imageVector = Icons.Default.MoreVert,
          contentDescription = stringResource(R.string.show_menu),
          onClick = { showMenu = !showMenu }
        )

        DropdownMenu(
          expanded = showMenu,
          onDismissRequest = { showMenu = false }
        ) {
          overFlowActions()
        }
      }
    },
    modifier = modifier,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = AppTheme.colors.primary,
      titleContentColor = Color.White,
      navigationIconContentColor = Color.White,
      actionIconContentColor = Color.White
    )
  )
}

@Preview
@Composable
private fun PreviewSearchBar() {
  AppTheme {
    TalabiyaSearchBar(
      title = { Text("Orders") },
      query = "Test",
      onQueryChanged = {}
    )
  }
}

@Composable
fun TalabiyaSearchBar(
  title: @Composable () -> Unit,
  query: String,
  onQueryChanged: (String) -> Unit,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
) {
  var isSearching by remember { mutableStateOf(false) }

  TalabiyaBar(
    title = {
      if (isSearching) {
        Row {
          SearchField(
            query = query,
            onQueryChanged = onQueryChanged,
            Modifier.weight(1F)
          )

          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.cancel_search),
            modifier = Modifier
              .clickable {
                if (query.isBlank()) {
                  isSearching = false
                } else {
                  onQueryChanged("")
                }
              }
          )
        }
      } else {
        title()
      }
    },
    navigationIcon = navigationIcon,
    actions = {
      actions()

      SearchAction(
        onClick = {
          isSearching = true
        }
      )
    },
    modifier = modifier,
  )
}

@Composable
private fun SearchField(
  query: String,
  onQueryChanged: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  TextField(
    value = query,
    onValueChange = onQueryChanged,
    modifier = modifier,
    colors = TextFieldDefaults.colors(
      focusedContainerColor = Color.Transparent,
      unfocusedContainerColor = Color.Transparent,
      disabledIndicatorColor = Color.Transparent,
      errorIndicatorColor = Color.Transparent,
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent
    )
  )
}

@Composable
private fun SearchAction(
  onClick: () -> Unit,
) {
  IconButton(
    onClick = onClick,
  ) {
    Icon(
      imageVector = Icons.Default.Search,
      contentDescription = stringResource(R.string.search_by_food_name),
    )
  }
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PreviewTalabiyaCenterAlignedTopBar() {
  AppTheme {
    Column {
      TalabiyaCenterAlignedTopBar(
        title = { Text(text = "Test") },
        navigationIcon = { TalabiyaTopBarDefaults.BackIcon(onClick = { /*TODO*/ }) }
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalabiyaCenterAlignedTopBar(
  title: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  colors: TopAppBarColors = TalabiyaTopBarDefaults.primary(),
  windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
  CenterAlignedTopAppBar(
    title = title,
    modifier = modifier,
    navigationIcon = navigationIcon,
    actions = actions,
    colors = colors,
    windowInsets = windowInsets
  )
}

object TalabiyaTopBarDefaults {

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun primary() = TopAppBarColors(
    containerColor = AppTheme.colors.primary,
    scrolledContainerColor = AppTheme.colors.primary,
    navigationIconContentColor = Color.White,
    titleContentColor = Color.White,
    actionIconContentColor = Color.White,
  )

  @Composable
  fun BackIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
  ) {
    TalabiyaIconButton(
      imageVector = Icons.AutoMirrored.Default.ArrowBack,
      contentDescription = stringResource(id = R.string.back),
      onClick = onClick,
      modifier = modifier
    )
  }
}