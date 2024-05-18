package com.mahozi.sayed.talabiya.user.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
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
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.ConfirmFab
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.user.data.UserStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

data class CreateUserState(
  val name: String,
  val canCreate: Boolean,
)

sealed interface CreateUserEvent {
  data class NameChanged(val name: String) : CreateUserEvent
  object CreateUserClicked : CreateUserEvent
}

class CreateUserPresenter @Inject constructor(
  private val userStore: UserStore,
  private val navigator: Navigator,
) : Presenter<CreateUserEvent, CreateUserState> {

  @Composable
  override fun start(events: Flow<CreateUserEvent>): CreateUserState {
    var name by remember { mutableStateOf("") }
    val canCreate = name.isNotBlank()

    CollectEvents(events) { event ->
      when (event) {
        CreateUserEvent.CreateUserClicked -> {
          launch {
            userStore.create(name)
            navigator.back()
          }
        }

        is CreateUserEvent.NameChanged -> name = event.name
      }
    }
    return CreateUserState(name, canCreate)
  }
}

@Parcelize
object CreateUserScreen : Screen

@Preview
@Composable
private fun PreviewCreateUserScreen() {
  AppTheme {
    CreateUserScreen(
      state = CreateUserState("Name", true),
      onEvent = {}
    )
  }
}
@Composable
fun CreateUserScreen(
  state: CreateUserState,
  onEvent: (CreateUserEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      TalabiyaBar(title = R.string.create_user)
    },
    floatingActionButton = {
      ConfirmFab { onEvent(CreateUserEvent.CreateUserClicked) }
    }
  ) { paddingValues ->
    Box(
      modifier = modifier
        .padding(paddingValues)
        .padding(16.dp)
    ) {
      OutlinedTextField(
        value = state.name,
        onValueChange = { newName -> onEvent(CreateUserEvent.NameChanged(newName)) },
        placeholder = {
          Text(
            text = string(R.string.enter_user_name)
          )
        },
        modifier = Modifier
          .fillMaxWidth(),

      )
    }
  }
}