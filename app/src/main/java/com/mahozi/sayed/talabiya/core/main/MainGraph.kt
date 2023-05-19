package com.mahozi.sayed.talabiya.core.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.mahozi.sayed.talabiya.core.Presenters
import com.mahozi.sayed.talabiya.core.datetime.AppDateTimeFormatter
import com.mahozi.sayed.talabiya.core.di.SingleIn
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Scope

@MainScope
@SingleIn(MainScope::class)
@MergeSubcomponent(MainScope::class)
interface MainGraph {

  fun presenterFactories(): Presenters

  fun formatter(): AppDateTimeFormatter

  @Subcomponent.Factory
  interface Factory {
    fun create(@BindsInstance navigator: Navigator, @BindsInstance context: AppCompatActivity): MainGraph
  }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

