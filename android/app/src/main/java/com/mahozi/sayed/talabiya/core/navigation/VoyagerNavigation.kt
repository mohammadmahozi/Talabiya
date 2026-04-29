package com.mahozi.sayed.talabiya.core.navigation

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mahozi.sayed.talabiya.core.CollectActions
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.TalabiyaApp
import com.mahozi.sayed.talabiya.core.Ui
import com.mahozi.sayed.talabiya.core.Uis
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.extensions.getActivity
import com.mahozi.sayed.talabiya.core.main.MainAction
import com.mahozi.sayed.talabiya.core.main.MainEvent
import com.mahozi.sayed.talabiya.order.list.ui.OrdersScreen
import com.mahozi.sayed.talabiya.resturant.list.RestaurantsScreen
import com.mahozi.sayed.talabiya.user.list.UsersScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import cafe.adriel.voyager.core.screen.Screen as VoyagerScreen
import cafe.adriel.voyager.navigator.Navigator as VoyagerNavigator

/**
 * Takes in an initial [Screen] and uses Voyager as a [Navigator] implementation to
 * push and pop destinations.
 */
@Composable
fun VoyagerNavigation(initialScreen: Screen) {
  VoyagerNavigator(initialScreen.toVoyagerScreen())
}

/**
 * Adapts a [Screen] to Voyager's concept of a screen.
 */
private fun Screen.toVoyagerScreen(): VoyagerScreen {
  return DelegatingVoyagerScreen(this)
}

private class DelegatingNavigator(var voyagerNavigator: VoyagerNavigator) : Navigator {

  override fun goto(screen: Screen) {
    voyagerNavigator.push(screen.toVoyagerScreen())
  }

  override fun back(screen: Screen?) {
    if (screen != null) {
      voyagerNavigator.popUntil { it == screen }
    } else {
      voyagerNavigator.pop()
    }
  }

  override fun replaceAll(screen: Screen) {
    voyagerNavigator.replaceAll(screen.toVoyagerScreen())
  }
}

@Parcelize
private data class DelegatingVoyagerScreen(
  private val screen: Screen,
) : VoyagerScreen, Parcelable {

  @Composable
  override fun Content() {
    val context: Context = LocalContext.current
    val voyagerNavigator = LocalNavigator.currentOrThrow

    val navigator = remember(voyagerNavigator) { DelegatingNavigator(voyagerNavigator) }
    val appGraph = (context.applicationContext as TalabiyaApp).appGraph
    val mainGraph by remember {
      mutableStateOf(appGraph.mainGraph().create(navigator, context.getActivity()!!))
    }

    val mainScreenModel = remember { MoleculeScreenModel(mainGraph.mainPresenter()) }
    val mainState = mainScreenModel.states()
    val dirPickerLauncher = rememberLauncherForActivityResult(
      ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
      if (uri != null) {
        context.contentResolver.takePersistableUriPermission(
          uri,
          Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        mainScreenModel.events.tryEmit(MainEvent.SaveBackupDir(uri.toString()))
      }
    }

    CollectActions(mainState.actions) { action ->
      when(action) {
        MainAction.OpenDirectoryPicker -> dirPickerLauncher.launch(null)
      }
    }

    val presenterFactories = mainGraph.presenterFactories()
    val presenter = presenterFactories.create(screen) as Presenter<Any?, Any?>

    val screenModel = rememberScreenModel {
      MoleculeScreenModel(presenter)
    }

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val state = screenModel.states()
    val ui = Uis(
      screen = screen,
      onBack = { navigator.back() },
      onToggleDrawer = { scope.launch { drawerState.open() } },
    ) as Ui<Any?, Any?>

    CompositionLocalProvider(
      LocalDateTimeFormatter provides mainGraph.formatter(),
    ) {
      ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          ModalDrawerSheet {
            Drawer(
              onOrdersClicked = { navigator.replaceAll(OrdersScreen) },
              onRestaurantsClicked = { navigator.replaceAll(RestaurantsScreen) },
              onUsersClicked = { navigator.replaceAll(UsersScreen) },
              creatingBackup = mainState.creatingBackup,
              onCreateBackupClicked = { mainScreenModel.events.tryEmit(MainEvent.CreateBackup) }
            )
          }
        }
      ) {
        Scaffold { paddingValues ->
          Box(
            modifier = Modifier
              .padding(paddingValues)
          ) {
            ui.Content(state = state, onEvent = { screenModel.events.tryEmit(it) })
          }
        }
      }
    }
  }

  override val key: ScreenKey
    get() = screen::class.qualifiedName!!
}

/**
 * Similar to Voyager's [StateScreenModel], except using Molecule to handle state generation.
 */
@Stable
private class MoleculeScreenModel<Event, State>(
  private val presenter: Presenter<Event, State>,
) : ScreenModel {
  val events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
  @Composable fun states() = presenter.start(events)
}

