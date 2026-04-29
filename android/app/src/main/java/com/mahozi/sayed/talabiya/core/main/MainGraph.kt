package com.mahozi.sayed.talabiya.core.main

import androidx.appcompat.app.AppCompatActivity
import com.mahozi.sayed.talabiya.core.Presenters
import com.mahozi.sayed.talabiya.core.datetime.AppDateTimeFormatter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.Scope

@GraphExtension(MainScope::class)
interface MainGraph {

  fun mainPresenter(): MainPresenter

  fun presenterFactories(): Presenters

  fun formatter(): AppDateTimeFormatter

  @GraphExtension.Factory
  interface Factory {
    fun create(
      @Provides navigator: Navigator,
      @Provides context: AppCompatActivity
    ): MainGraph
  }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

