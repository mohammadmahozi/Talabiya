package com.mahozi.sayed.talabiya.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Preview @Composable fun PreviewBar() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TalabiyaBar(title = R.string.app_name)

            TalabiyaBar(
                title = R.string.app_name,
                actions = {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null)
                },
                overFlowActions = {
                    DropdownMenuItem(onClick = { /*TODO*/ }) {
                        Text(text = "Test")
                    }
                }
            )
        }
    }
}

@Composable fun TalabiyaBar(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    overFlowActions: (@Composable ColumnScope.() -> Unit)? = null
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = stringResource(id = title),
                color = Color.White
            )
        },
        actions = {
            actions()

            if (overFlowActions != null) {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.show_menu)
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    overFlowActions()
                }
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable private fun PreviewSearchBar() {
    AppTheme {
        SearchBar(
            title = R.string.orders,
            query = "Test",
            onSearchClicked = { },
            onQueryChanged = {}
        )
    }
}
@Composable fun SearchBar(
    @StringRes title: Int,
    query: String,
    onSearchClicked: () -> Unit,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSearching by remember { mutableStateOf(false) }

    if (isSearching) {
        TopAppBar(
            modifier = modifier,
            content = {
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
                            } else{
                                onQueryChanged("")
                            }
                        }
                )
            },
        )
    } else {
        TalabiyaBar(
            title = title,
            actions = {
                SearchAction(
                    onClick = {
                        onSearchClicked()
                        isSearching = true
                    }
                )
            }
        )
    }
}

@Composable fun SearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
@Composable fun SearchAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = Icons.Default.Search,
        contentDescription = stringResource(R.string.search_by_food_name),
        modifier = modifier
          .clickable(onClick = onClick)
    )
}