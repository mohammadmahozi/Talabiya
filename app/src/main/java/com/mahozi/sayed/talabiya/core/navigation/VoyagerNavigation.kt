package com.mahozi.sayed.talabiya.core.navigation

import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.ScreenModelStore
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mahozi.sayed.talabiya.core.Uis
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.TalabiyaApp
import com.mahozi.sayed.talabiya.core.Ui
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.extensions.getActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
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
    voyagerNavigator.pop()
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

    val presenterFactories = mainGraph.presenterFactories()
    val presenter = presenterFactories.create(screen) as Presenter<Any?, Any?>

    val screenModel = rememberScreenModel {
      MoleculeScreenModel(presenter)
    }

    val state by screenModel.states.collectAsState()
    val ui = Uis(screen = screen) as Ui<Any?, Any?>

    CompositionLocalProvider(LocalDateTimeFormatter provides mainGraph.formatter()) {
      ui.Content(state = state, onEvent = { screenModel.events.tryEmit(it) })
    }
  }

  override val key: ScreenKey
    get() = screen::class.qualifiedName!!
}

/**
 * Similar to Voyager's [StateScreenModel], except using Molecule to handle state generation.
 */
private class MoleculeScreenModel<Event, State>(
  presenter: Presenter<Event, State>,
): ScreenModel {
  val events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
  val states = moleculeScope.launchMolecule(RecompositionClock.Immediate) { presenter.start(events) }
}

private val ScreenModel.moleculeScope: CoroutineScope
  get() = ScreenModelStore.getOrPutDependency(
    screenModel = this,
    name = "ScreenModelMoleculeScope",
    factory = { key -> CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) + CoroutineName(key) },
    onDispose = { scope -> scope.cancel() }
  )

