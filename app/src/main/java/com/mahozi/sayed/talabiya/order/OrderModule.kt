package com.mahozi.sayed.talabiya.order

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.Database
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(AppScope::class)
object OrderModule {

    @Provides fun provideOrderQuery(database: Database) = database.orderQueries
}