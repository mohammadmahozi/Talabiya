package com.mahozi.sayed.talabiya.order

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.molecule.AndroidUiDispatcher
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.mahozi.sayed.talabiya.core.di.AppGraph
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsNode
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsVM
import com.mahozi.sayed.talabiya.order.list.ui.OrdersNode
import com.mahozi.sayed.talabiya.order.list.ui.OrdersVM
import kotlinx.coroutines.CoroutineScope

class OrderRoot(
    private val buildContext: BuildContext,
    private val backStack: BackStack<OrderRoute> = BackStack(
        initialElement = OrderRoute.Orders,
        savedStateMap = buildContext.savedStateMap
    ),
    private val graph: AppGraph
): ParentNode<OrderRoute>(
    backStack,
    buildContext
) {

    override fun resolve(routing: OrderRoute, buildContext: BuildContext): Node =
        when(routing) {
            OrderRoute.Orders -> {
                val viewModel = OrdersVM(
                    graph.ordersRepository,
                    CoroutineScope(AndroidUiDispatcher.Main),
                    backStack
                )
                OrdersNode(buildContext, viewModel)
            }
            is OrderRoute.OrderDetails -> {
                OrderDetailsNode(buildContext, OrderDetailsVM())
            }
        }

    @Composable override fun View(modifier: Modifier) {
        Children(navModel = backStack)
    }
}