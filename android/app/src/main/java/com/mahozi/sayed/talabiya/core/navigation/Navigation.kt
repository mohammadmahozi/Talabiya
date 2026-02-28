package com.mahozi.sayed.talabiya.core.navigation

import android.os.Parcelable
import androidx.compose.runtime.Immutable


interface Navigator {
  fun goto(screen: Screen)
  fun back(screen: Screen? = null)
  fun replaceAll(screen: Screen)
}

@Immutable interface Screen: Parcelable

class NoOpNavigator(): Navigator {
  override fun goto(screen: Screen) {}

  override fun back(screen: Screen?) {}

  override fun replaceAll(screen: Screen) {}
}


