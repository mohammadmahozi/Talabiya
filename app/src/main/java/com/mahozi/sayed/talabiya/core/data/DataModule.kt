package com.mahozi.sayed.talabiya.core.data

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context) =
        TalabiyaDatabase.getDatabase(context)

}