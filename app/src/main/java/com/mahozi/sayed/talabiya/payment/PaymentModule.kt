package com.mahozi.sayed.talabiya.payment

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.Database
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(AppScope::class)
object PaymentModule {

    @Provides fun providePaymentQuery(database: Database) = database.paymentQueries
}