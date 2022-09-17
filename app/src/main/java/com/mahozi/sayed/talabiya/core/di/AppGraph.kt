package com.mahozi.sayed.talabiya.core.di

import android.content.Context
import com.mahozi.sayed.talabiya.core.RootNode
import com.mahozi.sayed.talabiya.core.data.DataModule
import com.mahozi.sayed.talabiya.core.main.MainGraph
import com.mahozi.sayed.talabiya.order.OrderModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppGraph {

    @Component.Factory interface Factory {
        fun create(@BindsInstance context: Context): AppGraph
    }

    fun mainGraph(): MainGraph.Factory
}

