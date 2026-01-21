package com.mahozi.sayed.talabiya.core

import android.app.Application
import com.mahozi.sayed.talabiya.core.di.AppGraph
import dev.zacsweers.metro.createGraphFactory

class TalabiyaApp: Application() {

    val appGraph = createGraphFactory<AppGraph.Factory>().create(this)
}