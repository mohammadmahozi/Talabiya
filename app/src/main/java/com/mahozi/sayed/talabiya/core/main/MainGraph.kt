package com.mahozi.sayed.talabiya.core.main

import com.mahozi.sayed.talabiya.core.Presenters
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Scope

@MainScope
@MergeSubcomponent(MainScope::class)
interface MainGraph {

  fun presenterFactories(): Presenters

  @Subcomponent.Factory
  interface Factory {
    fun create(@BindsInstance navigator: Navigator): MainGraph
  }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

