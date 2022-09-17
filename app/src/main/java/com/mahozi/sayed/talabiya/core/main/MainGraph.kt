package com.mahozi.sayed.talabiya.core.main

import com.bumble.appyx.core.modality.BuildContext
import com.mahozi.sayed.talabiya.core.RootNode
import com.mahozi.sayed.talabiya.order.OrderModule
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Scope

@MainScope
@Subcomponent(modules = [MainModule::class, OrderModule::class])
interface MainGraph {

    @Subcomponent.Factory interface Factory {
        fun create(@BindsInstance buildContext: BuildContext): MainGraph
    }

    fun root(): RootNode

}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

