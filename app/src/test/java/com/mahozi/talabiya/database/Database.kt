package com.mahozi.talabiya.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.mahozi.sayed.talabiya.core.data.TypeAdapters
import com.mahozi.talabiya.Database
import order.OrderEntity
import order.OrderItemPriceEntity
import payment.PaymentEntity
import restaurant.MenuItemPriceEntity
import java.util.Properties


fun createDatabase(): Database {
  val driver = JdbcSqliteDriver(
    url = JdbcSqliteDriver.IN_MEMORY,
    properties = Properties().apply { put("foreign_keys", "true") }
  )
  Database.Schema.create(driver)
  return Database(
    driver,
    MenuItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
    OrderEntity.Adapter(TypeAdapters.instantAdapter),
    OrderItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
    PaymentEntity.Adapter(TypeAdapters.instantAdapter),
  )
}