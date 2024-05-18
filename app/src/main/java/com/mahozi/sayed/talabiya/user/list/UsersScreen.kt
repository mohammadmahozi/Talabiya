package com.mahozi.sayed.talabiya.user.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.DeleteContextMenu
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import kotlinx.parcelize.Parcelize
import user.UserEntity

@Parcelize
object UsersScreen : Screen

@Preview
@Composable
private fun PreviewUsersScreen() {
  AppTheme {
    UsersScreen(
      state = UsersState(listOf(UserEntity(0L, "Name"))),
      onEvent = {}
    )

  }
}

@Composable fun UsersScreen(
  state: UsersState,
  onEvent: (UsersEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TalabiyaBar(title = R.string.users)
    },
    floatingActionButton = { AddFab { onEvent(UsersEvent.CreateUserClicked) } }
  ) { paddingValues ->
    Box(
      modifier = modifier
        .padding(paddingValues)
    ) {
      LazyColumn {
        items(state.users) { user ->
          User(
            user = user,
            onClick = { onEvent(UsersEvent.UserClicked(it)) },
            onDelete = { onEvent(UsersEvent.DeleteUserClicked(it)) }
          )
          Divider()
        }
      }
    }
  }
}

@Preview
@Composable
private fun PreviewUser() {
  AppTheme {
    User(
      user = UserEntity(0L, "Name"),
      onClick = {},
      onDelete = {},
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable private fun User(
  user: UserEntity,
  onClick: (UserEntity) -> Unit,
  onDelete: (UserEntity) -> Unit,
  ) {
  var expanded by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier
      .combinedClickable(
        onClick = { onClick(user) },
        onLongClick = { expanded = true }
      )
      .padding(16.dp)
      .fillMaxWidth()
  ) {
    Text(
      text = user.id.toString(),
      color = AppTheme.colors.primaryText,
      fontSize = 14.sp,
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = user.name,
      color = AppTheme.colors.primaryText,
      fontSize = 14.sp,
    )

    DeleteContextMenu(
      expanded = expanded,
      onDelete = { onDelete(user) },
      onDismiss = { expanded = false }
    )
  }
}


