package com.mahozi.sayed.talabiya.core

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import app.cash.molecule.AndroidUiDispatcher
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.order.list.ui.OrdersVM
import kotlinx.coroutines.CoroutineScope

class MainActivity : NodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appGraph = (applicationContext as TalabiyaApp).graph

        setContent {
            AppTheme {
                NodeHost(integrationPoint = integrationPoint) {
                    RootNode(buildContext = it, appGraph = appGraph)
                }
            }
        }
    }
}