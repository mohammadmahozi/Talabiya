package com.mahozi.sayed.talabiya.core.main

import android.os.Bundle
import androidx.activity.compose.setContent
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.mahozi.sayed.talabiya.core.RootNode
import com.mahozi.sayed.talabiya.core.TalabiyaApp
import com.mahozi.sayed.talabiya.core.di.DaggerAppGraph
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

class MainActivity : NodeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appGraph = (applicationContext as TalabiyaApp).appGraph

        setContent {
            AppTheme {
                NodeHost(integrationPoint = integrationPoint) {
                    appGraph.mainGraph().create(it).root()
                }
            }
        }
    }
}