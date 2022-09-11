package com.mahozi.sayed.talabiya.core

import androidx.compose.runtime.Composable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import java.lang.IllegalStateException

abstract class ViewModel<Model: Any, Event: Any> {

    private val eventsChannel = Channel<Event>()
    protected val events = eventsChannel.receiveAsFlow()

    fun event(event: Event) {
        eventsChannel.trySend(event)
            .onFailure {
                throw IllegalStateException("Failed to send event $event")
            }
    }

    @Composable abstract fun start(): Model
}