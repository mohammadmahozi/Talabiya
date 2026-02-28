package com.mahozi.talabiya.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mahozi.sayed.talabiya.core.data.TypeAdapters
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.payment.PaymentStore
import com.mahozi.sayed.talabiya.user.data.UserStore
import com.mahozi.talabiya.TalabiyaDatabase
import kotlinx.coroutines.Dispatchers
import order.OrderEntity
import order.OrderItemPriceEntity
import payment.PaymentEntity
import restaurant.MenuItemPriceEntity
import java.util.Properties


fun createDatabase(): TalabiyaDatabase {
  val driver = JdbcSqliteDriver(
    url = JdbcSqliteDriver.IN_MEMORY,
    properties = Properties().apply { put("foreign_keys", "true") }
  )
  TalabiyaDatabase.Schema.create(driver)
  return TalabiyaDatabase(
    driver,
    MenuItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
    OrderEntity.Adapter(TypeAdapters.instantAdapter),
    OrderItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
    PaymentEntity.Adapter(TypeAdapters.instantAdapter),
  )
}

fun TalabiyaDatabase.orderStore() =
  OrderStore(orderQueries, menuItemQueries, Dispatchers.Main)

fun TalabiyaDatabase.paymentStore() =
  PaymentStore(paymentQueries, Dispatchers.Main)

fun TalabiyaDatabase.userStore() =
  UserStore(userQueries, Dispatchers.Main)