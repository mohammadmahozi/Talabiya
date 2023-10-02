package com.mahozi.sayed.talabiya.core.data

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.Database
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import order.OrderEntity
import restaurant.MenuItemPriceEntity
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
        val driver = AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = "main",
            callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.execSQL("PRAGMA foreign_keys=ON;");
                }
            })
        return Database(
            driver,
            MenuItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
            OrderEntity.Adapter(TypeAdapters.instantAdapter)
        )
    }

    @Provides fun provideDispatcher() = Dispatchers.IO
}