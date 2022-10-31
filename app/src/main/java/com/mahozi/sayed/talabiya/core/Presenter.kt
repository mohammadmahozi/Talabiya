package com.mahozi.sayed.talabiya.core

import androidx.compose.runtime.Composable
import com.mahozi.sayed.talabiya.core.navigation.Screen
import kotlinx.coroutines.flow.Flow

/**
 * Takes a stream of events and produces state over time for a [Screen].
 */
interface Presenter<Event, State> {
  @Composable
  fun start(events: Flow<Event>): State

  fun interface Factory {
    fun create(screen: Screen): Presenter<*, *>
  }
}

