package com.mahozi.sayed.talabiya.core.main

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.navmodel.backstack.BackStack
import com.mahozi.sayed.talabiya.core.RootRoute
import dagger.Module
import dagger.Provides

@Module
object MainModule {

    @MainScope
    @Provides
    fun provideRootBackstack(buildContext: BuildContext): BackStack<RootRoute> = BackStack(
        initialElement = RootRoute.OrderRoot,
        savedStateMap = buildContext.savedStateMap
    )
}