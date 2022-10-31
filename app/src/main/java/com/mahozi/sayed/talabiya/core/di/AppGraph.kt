package com.mahozi.sayed.talabiya.core.di

import android.content.Context
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.data.DataModule
import com.mahozi.sayed.talabiya.core.main.MainGraph
import com.mahozi.sayed.talabiya.order.OrderModule
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
@MergeComponent(AppScope::class)
interface AppGraph {

    @Component.Factory interface Factory {
        fun create(@BindsInstance context: Context): AppGraph
    }

    fun mainGraph(): MainGraph.Factory
}

abstract class AppScope private constructor()

