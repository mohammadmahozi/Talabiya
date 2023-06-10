package com.mahozi.sayed.talabiya.core.navigation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.mahozi.sayed.talabiya.core.Presenter


interface Navigator {
  fun goto(screen: Screen)
  fun back(screen: Screen? = null)
}

@Immutable interface Screen: Parcelable


