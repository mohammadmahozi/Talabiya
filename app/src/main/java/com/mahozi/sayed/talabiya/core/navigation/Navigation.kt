package com.mahozi.sayed.talabiya.core.navigation

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf


interface Navigator {
  fun goto(screen: Screen)
  fun back(screen: Screen? = null)
  fun replaceAll(screen: Screen)
}

@Immutable interface Screen: Parcelable

@SuppressLint("ComposeCompositionLocalUsage")
val LocalNavigator = staticCompositionLocalOf<Navigator> {
  error("Navigator is not provided")
}

class NoOpNavigator(): Navigator {
  override fun goto(screen: Screen) {}

  override fun back(screen: Screen?) {}

  override fun replaceAll(screen: Screen) {}
}


