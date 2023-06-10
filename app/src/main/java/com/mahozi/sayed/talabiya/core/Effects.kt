package com.mahozi.sayed.talabiya.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

@Composable fun <T> CollectEvents(
  events: Flow<T>,
  key1: Any? = Unit,
  block: CoroutineScope.(T) -> Unit
) {
  LaunchedEffect(key1) {
    events.collect{
      block(it)
    }
  }
}