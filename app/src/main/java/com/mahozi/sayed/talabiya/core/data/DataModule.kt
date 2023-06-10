package com.mahozi.sayed.talabiya.core.data

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.Database
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import order.OrderEntity
import javax.inject.Singleton


@Module
@ContributesTo(AppScope::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context) =
        TalabiyaDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideTalabiyaDatabase(context: Context): Database {
        val driver = AndroidSqliteDriver(Database.Schema, context, "main")
        return Database(driver, OrderEntity.Adapter(TypeAdapters.instantAdapter))
    }

    @Provides fun provideDispatcher() = Dispatchers.IO
}