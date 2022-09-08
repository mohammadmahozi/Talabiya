package com.mahozi.sayed.talabiya.core.di

import android.app.Application
import androidx.fragment.app.Fragment
import com.mahozi.sayed.talabiya.core.TalabiyaApp
import com.mahozi.sayed.talabiya.core.TalabiyaDatabase
import com.mahozi.sayed.talabiya.order.store.OrderRepository

class AppGraph(
    private val application: Application
) {

    private val database = TalabiyaDatabase.getDatabase(application)
    val ordersRepository = OrderRepository(database.orderDao())
}

val Fragment.appGraph get() = (requireContext().applicationContext as TalabiyaApp).graph