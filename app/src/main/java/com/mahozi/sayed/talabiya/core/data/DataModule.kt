package com.mahozi.sayed.talabiya.core.data

import android.content.Context
import com.mahozi.sayed.talabiya.core.di.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
@ContributesTo(AppScope::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context) =
        TalabiyaDatabase.getDatabase(context)

}