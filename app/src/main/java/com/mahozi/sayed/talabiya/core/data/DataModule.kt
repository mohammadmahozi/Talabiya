package com.mahozi.sayed.talabiya.core.data

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.Database
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import order.OrderEntity
import order.OrderItemPriceEntity
import payment.PaymentEntity
import restaurant.MenuItemPriceEntity


@ContributesTo(AppScope::class)
interface DataModule {

    @SingleIn(AppScope::class)
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
            OrderEntity.Adapter(TypeAdapters.instantAdapter),
            OrderItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
            PaymentEntity.Adapter(TypeAdapters.instantAdapter),
        )
    }

    @Provides fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}