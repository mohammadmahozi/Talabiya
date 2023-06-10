package com.mahozi.sayed.talabiya.order

import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase
import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.sayed.talabiya.core.main.MainScope
import com.mahozi.sayed.talabiya.order.store.OrderDao
import com.mahozi.talabiya.Database
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@ContributesTo(AppScope::class)
object OrderModule {

    @Singleton
    @Provides fun provideOrderDao(database: TalabiyaDatabase): OrderDao = database.orderDao()

    @Provides fun provideOrderQuery(database: Database) = database.orderQueries
}