package com.mahozi.sayed.talabiya.user.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.user.create.CreateUserScreen
import com.mahozi.sayed.talabiya.user.data.UserStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UsersPresenter @Inject constructor(
  private val userStore: UserStore,
  private val navigator: Navigator,
): Presenter<UsersEvent, UsersState> {

  @Composable override fun start(events: Flow<UsersEvent>): UsersState {
    val users by remember { userStore.users }.collectAsState(initial = emptyList())

    CollectEvents(events) { event ->
      when(event) {
        UsersEvent.CreateUserClicked -> navigator.goto(CreateUserScreen)
        is UsersEvent.DeleteUserClicked -> {
          launch {
            userStore.delete(event.user.id)
          }
        }
        is UsersEvent.UserClicked -> { }
      }
    }
    return UsersState(users)
  }
}