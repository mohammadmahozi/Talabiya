package com.mahozi.sayed.talabiya.payment

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.TalabiyaDatabase
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import payment.PaymentQueries


@ContributesTo(AppScope::class)
interface PaymentModule {

    @Provides
    fun providePaymentQuery(database: TalabiyaDatabase): PaymentQueries = database.paymentQueries
}