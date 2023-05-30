package com.mahozi.sayed.talabiya.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable fun CollectEvents(
  key1: Any? = Unit,
  block: CoroutineScope.() -> Unit
) {
  LaunchedEffect(key1) {
    block()
  }
}