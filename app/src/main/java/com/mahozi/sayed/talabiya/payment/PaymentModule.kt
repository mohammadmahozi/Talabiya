package com.mahozi.sayed.talabiya.payment

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.Database
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import payment.PaymentQueries


@ContributesTo(AppScope::class)
interface PaymentModule {

    @Provides
    fun providePaymentQuery(database: Database): PaymentQueries = database.paymentQueries
}