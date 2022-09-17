package com.mahozi.sayed.talabiya.core

import android.app.Application
import com.mahozi.sayed.talabiya.core.di.AppGraph
import com.mahozi.sayed.talabiya.core.di.DaggerAppGraph

class TalabiyaApp: Application() {

    val appGraph = DaggerAppGraph.factory().create(this)

    override fun onCreate() {
        super.onCreate()
    }
}