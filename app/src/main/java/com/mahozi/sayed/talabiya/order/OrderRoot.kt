package com.mahozi.sayed.talabiya.order

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsNode
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsVM
import com.mahozi.sayed.talabiya.order.list.ui.OrdersNode
import com.mahozi.sayed.talabiya.order.list.ui.OrdersVM
import javax.inject.Inject

class OrderRoot @Inject constructor(
    private val buildContext: BuildContext,
    private val backStack: BackStack<OrderRoute>,
    private val orderVM: OrdersVM
): ParentNode<OrderRoute>(
    backStack,
    buildContext
) {

    override fun resolve(routing: OrderRoute, buildContext: BuildContext): Node =
        when(routing) {
            OrderRoute.Orders -> OrdersNode(buildContext, orderVM)

            is OrderRoute.OrderDetails -> {
                OrderDetailsNode(buildContext, OrderDetailsVM())
            }
        }

    @Composable override fun View(modifier: Modifier) {
        Children(navModel = backStack)
    }
}