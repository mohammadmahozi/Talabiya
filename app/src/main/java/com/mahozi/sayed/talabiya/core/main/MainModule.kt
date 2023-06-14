package com.mahozi.sayed.talabiya.core.main

import android.content.Context
import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase
import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.sayed.talabiya.core.extensions.locale
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import java.util.Locale
import javax.inject.Singleton


@Module
@ContributesTo(MainScope::class)
object MainModule {

    @Provides
    fun provideDatabase(context: Context): Locale = context.locale

}