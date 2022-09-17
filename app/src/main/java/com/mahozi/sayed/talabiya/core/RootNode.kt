package com.mahozi.sayed.talabiya.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.mahozi.sayed.talabiya.core.di.AppGraph
import com.mahozi.sayed.talabiya.order.OrderRoot
import javax.inject.Inject
import javax.inject.Provider

class RootNode @Inject constructor(
    private val buildContext: BuildContext,
    private val backStack: BackStack<RootRoute> = BackStack(
        initialElement = RootRoute.OrderRoot,
        savedStateMap = buildContext.savedStateMap
    ),
    private val orderRootProvider: Provider<OrderRoot>
) : ParentNode<RootRoute>(navModel = backStack, buildContext) {

    override fun resolve(routing: RootRoute, buildContext: BuildContext): Node = when (routing) {
        is RootRoute.OrderRoot -> orderRootProvider.get() //OrderRoot(buildContext, graph = appGraph)
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(navModel = backStack)
    }

}