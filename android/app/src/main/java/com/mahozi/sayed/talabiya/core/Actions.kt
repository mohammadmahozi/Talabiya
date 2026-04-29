package com.mahozi.sayed.talabiya.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

data class ActionData<T>(
  val action: T,
  val continuation: CancellableContinuation<Unit>
)

@Stable
class ActionsState<T> {

  var currentAction by mutableStateOf<ActionData<T>?>(null)
    private set

  private val mutex = Mutex()

  suspend fun send(action: T) {
    mutex.withLock {
      try {
        suspendCancellableCoroutine { continuation ->
          currentAction = ActionData(
            action = action,
            continuation = continuation
          )
        }
      } finally {
        currentAction = null
      }
    }
  }
}

@Composable
fun <T> CollectActions(
  state: ActionsState<T>,
  block: (T) -> Unit
) {
  LaunchedEffect(state.currentAction) {
    val currentAction = state.currentAction
    if (currentAction != null) {
      block(currentAction.action)
      if (currentAction.continuation.isActive) currentAction.continuation.resume(Unit)
    }
  }
}