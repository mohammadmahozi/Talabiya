package com.mahozi.sayed.talabiya.order

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.TalabiyaDatabase
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import order.OrderQueries


@ContributesTo(AppScope::class)
interface OrderModule {

    @Provides
    fun provideOrderQuery(database: TalabiyaDatabase): OrderQueries = database.orderQueries
}