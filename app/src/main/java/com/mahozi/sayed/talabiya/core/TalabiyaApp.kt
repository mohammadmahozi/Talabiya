package com.mahozi.sayed.talabiya.core

import android.app.Application
import com.mahozi.sayed.talabiya.core.di.AppGraph

class TalabiyaApp: Application() {

    lateinit var graph: AppGraph
    private set

    override fun onCreate() {
        super.onCreate()

        graph = AppGraph(this)
    }
}