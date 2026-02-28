package com.mahozi.sayed.talabiya.core.di

import android.content.Context
import com.mahozi.sayed.talabiya.core.main.MainGraph
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AppGraph {

  @DependencyGraph.Factory
  interface Factory {
    fun create(@Provides context: Context): AppGraph
  }

  fun mainGraph(): MainGraph.Factory
}

abstract class AppScope private constructor()

