package com.mahozi.talabiya.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.mahozi.sayed.talabiya.core.ActionData
import com.mahozi.sayed.talabiya.core.ActionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.coroutines.resume

@OptIn(ExperimentalCoroutinesApi::class)
class ActionsTest {

  @Test
  fun `action is received by collector`() = runTest(UnconfinedTestDispatcher()) {
    val state = ActionsState<Int>()

    launch { state.send(1) }
    val action1 = state.currentAction?.consume()
    assertThat(action1).isEqualTo(1)

    launch { state.send(2) }
    val action2 = state.currentAction?.consume()
    assertThat(action2).isEqualTo(2)

    assertThat(state.currentAction).isNull()
  }

  @Test
  fun `action are queued`() = runTest(UnconfinedTestDispatcher()) {
    val state = ActionsState<Int>()

    launch { state.send(1) }
    launch { state.send(2) }

    val action1 = state.currentAction?.consume()
    assertThat(action1).isEqualTo(1)

    val action2 = state.currentAction?.consume()
    assertThat(action2).isEqualTo(2)

    assertThat(state.currentAction).isNull()
  }

  private fun <T> ActionData<T>.consume(
  ): T {
    continuation.resume(Unit)
    return action
  }

  @Test
  fun `when scope is canceled current action is removed`() = runTest(UnconfinedTestDispatcher()) {
    val state = ActionsState<Int>()

    val scope = CoroutineScope(coroutineContext + Job())

    scope.launch { state.send(1) }
    val action1 = state.currentAction?.action
    assertThat(action1).isEqualTo(1)

    scope.cancel()
    assertThat(state.currentAction).isNull()
  }
}