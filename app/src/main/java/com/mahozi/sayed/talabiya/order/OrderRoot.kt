package com.mahozi.sayed.talabiya.order

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.cash.molecule.AndroidUiDispatcher
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.mahozi.sayed.talabiya.core.TalabiyaApp
import com.mahozi.sayed.talabiya.core.di.AppGraph
import com.mahozi.sayed.talabiya.order.list.ui.OrdersNode
import com.mahozi.sayed.talabiya.order.list.ui.OrdersVM
import kotlinx.coroutines.CoroutineScope

class OrderRoot(
    private val buildContext: BuildContext,
    private val backStack: BackStack<OrdersRoute> = BackStack(
        initialElement = OrdersRoute.Orders,
        savedStateMap = buildContext.savedStateMap
    ),
    private val graph: AppGraph
): ParentNode<OrdersRoute>(
    backStack,
    buildContext
) {

    override fun resolve(routing: OrdersRoute, buildContext: BuildContext): Node =
        when(routing) {
            OrdersRoute.Orders -> {
                val viewModel = OrdersVM(graph.ordersRepository, CoroutineScope(AndroidUiDispatcher.Main))
                OrdersNode(buildContext, viewModel)
            }
        }

    @Composable override fun View(modifier: Modifier) {
        Children(navModel = backStack)
    }
}