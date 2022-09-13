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

class RootNode(
    private val buildContext: BuildContext,
    private val backStack: BackStack<RootRoute> = BackStack(
        initialElement = RootRoute.OrderRoot,
        savedStateMap = buildContext.savedStateMap
    ),
    private val appGraph: AppGraph
) : ParentNode<RootRoute>(navModel = backStack, buildContext) {

    override fun resolve(routing: RootRoute, buildContext: BuildContext): Node = when (routing) {
        is RootRoute.OrderRoot -> OrderRoot(buildContext, graph = appGraph)
    }

    @Composable
    override fun View(modifier: Modifier) {
        // Children composable will automatically render the currently active child
        Children(navModel = backStack)
    }

}