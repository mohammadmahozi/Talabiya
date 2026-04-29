package com.mahozi.sayed.talabiya.core.data

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mahozi.sayed.talabiya.BuildConfig
import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.payment.PaymentDirection
import com.mahozi.sayed.talabiya.payment.PaymentStatus
import com.mahozi.sayed.talabiya.payment.key
import com.mahozi.talabiya.TalabiyaDatabase
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import order.OrderEntity
import order.OrderItemPriceEntity
import payment.PaymentEntity
import restaurant.MenuItemPriceEntity
import java.time.Instant


@ContributesTo(AppScope::class)
interface DataModule {

  @SingleIn(AppScope::class)
  @Provides
  fun provideTalabiyaDatabase(context: Context): TalabiyaDatabase {
    val driver = AndroidSqliteDriver(
      schema = Schema,
      context = context,
      name = DB_NAME,
      callback = object : AndroidSqliteDriver.Callback(Schema) {
        override fun onOpen(db: SupportSQLiteDatabase) {
          db.execSQL("PRAGMA foreign_keys=ON;");
        }
      }
    )
    return TalabiyaDatabase(
      driver,
      MenuItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
      OrderEntity.Adapter(TypeAdapters.instantAdapter),
      OrderItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
      PaymentEntity.Adapter(TypeAdapters.instantAdapter),
    )
  }

  @Provides
  fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO

  companion object {
    const val DB_NAME = "main"
  }
}

private object Schema : SqlSchema<QueryResult.Value<Unit>> by TalabiyaDatabase.Schema {
  override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
    TalabiyaDatabase.Schema.create(driver)
    TalabiyaDatabase(
      driver = driver,
      MenuItemPriceEntityAdapter = MenuItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
      OrderEntityAdapter = OrderEntity.Adapter(TypeAdapters.instantAdapter),
      OrderItemPriceEntityAdapter = OrderItemPriceEntity.Adapter(TypeAdapters.instantAdapter),
      PaymentEntityAdapter = PaymentEntity.Adapter(TypeAdapters.instantAdapter),
    ).apply {
      if (BuildConfig.DEBUG) seed()
      return QueryResult.Unit
    }
  }
}

private fun TalabiyaDatabase.seed() {

  fun Any.id() = orderQueries.lastInsertRowId().executeAsOne()

  val user1 = userQueries.insert(name = "User 1").id()
  val user2 = userQueries.insert(name = "User 2").id()
  val user3 =  userQueries.insert(name = "User 3").id()
  userQueries.insert(name = "User 4").id()
  userQueries.insert(name = "User 5").id()
  userQueries.insert(name = "User 6").id()
  userQueries.insert(name = "User 7").id()
  userQueries.insert(name = "User 8").id()
  userQueries.insert(name = "User 9").id()
  userQueries.insert(name = "User 10").id()


  restaurantQueries.insert(name = "Restaurant 1")

  menuItemQueries.insert(restaurantId = 1, name = "Item 1", category = "Pastry")
  menuItemQueries.insert(restaurantId = 1, name = "Item 2", category = "Pastry")
  menuItemQueries.insert(restaurantId = 1, name = "Item 3", category = "Pastry")

  val menu1 = menuItemQueries.insertPrice(
    menuItemId = 1,
    datetime = Instant.now(),
    price = 10.money.toCents()
  ).id()

  val menu2 = menuItemQueries.insertPrice(
    menuItemId = 2,
    datetime = Instant.now(),
    price = 5.money.toCents()
  ).id()

  //Both users ordered, user1 paid for whole order.
  val order1 = orderQueries.insert(restaurantId = 1, createdAt = Instant.now()).id()

  orderQueries.updatePayer(user1, order1)
  val item1 = orderQueries.insertOrderItemPrice(
    orderId = order1,
    menuItemId = menu1,
    price = 10.money.toCents(),
    datetime = Instant.now(),
  ).id()
  val item2 = orderQueries.insertOrderItemPrice(
    orderId = order1,
    menuItemId = menu2,
    price = 5.money.toCents(),
    datetime = Instant.now(),
  ).id()

  orderQueries.insertOrderItem(customerId = user1, quantity = 2, orderItemPriceId = item1, "")
  orderQueries.insertOrderItem(customerId = user1, quantity = 2, orderItemPriceId = item2, "")
  orderQueries.insertOrderItem(customerId = user2, quantity = 2, orderItemPriceId = item1, "")

  //Only user1 ordered
  val order2 = orderQueries.insert(restaurantId = 1, createdAt = Instant.now()).id()
  val item3 = orderQueries.insertOrderItemPrice(
    orderId = order2,
    menuItemId = menu1,
    price = 10.money.toCents(),
    datetime = Instant.now(),
  ).id()
  orderQueries.insertOrderItem(customerId = user1, quantity = 5, orderItemPriceId = item3, "")

  //Only user1 ordered, paid for his share, 1 pay canceled and 1 completed
  val order3 = orderQueries.insert(restaurantId = 1, createdAt = Instant.now()).id()
  val item4 = orderQueries.insertOrderItemPrice(
    orderId = order3,
    menuItemId = menu1,
    price = 10.money.toCents(),
    datetime = Instant.now(),
  ).id()
  orderQueries.insertOrderItem(customerId = user1, quantity = 2, orderItemPriceId = item4, "")
  val payment1 = paymentQueries.insertPayment(
    userId = user1,
    amount = 10.money.toCents(),
    createdAt = Instant.now(),
    status = PaymentStatus.Canceled.key,
    direction = PaymentDirection.Credit.key,
  ).id()
  val invoice1 = paymentQueries.insertInvoice(
    paymentId = payment1,
    totalOwed = 10.money.toCents(),
    totalPaid = 0.money.toCents()
  ).id()
  paymentQueries.insertInvoiceItem(
    invoiceId = invoice1,
    orderId = order3,
    amountPaid = 0.money.toCents(),
    amountOwed = 10.money.toCents()
  )
  val payment2 = paymentQueries.insertPayment(
    userId = user1,
    amount = 10.money.toCents(),
    createdAt = Instant.now(),
    status = PaymentStatus.Completed.key,
    direction = PaymentDirection.Credit.key
  ).id()
  val invoice2 = paymentQueries.insertInvoice(
    paymentId = payment2,
    totalOwed = 10.money.toCents(),
    totalPaid = 0.money.toCents()
  ).id()
  paymentQueries.insertInvoiceItem(
    invoiceId = invoice2,
    orderId = order3,
    amountPaid = 10.money.toCents(),
    amountOwed = 0
  )

  //Only user2 ordered, User1 paid for whole order and was reimbursed
  val order4 = orderQueries.insert(restaurantId = 1, createdAt = Instant.now()).id()
  orderQueries.updatePayer(payerId = user1, id = order4)
  val item5 = orderQueries.insertOrderItemPrice(
    orderId = order4,
    menuItemId = menu1,
    price = 10.money.toCents(),
    datetime = Instant.now(),
  ).id()
  orderQueries.insertOrderItem(customerId = user2, quantity = 2, orderItemPriceId = item5, "")
  val payment3 = paymentQueries.insertPayment(
    userId = user1,
    amount = 10.money.toCents(),
    createdAt = Instant.now(),
    status = PaymentStatus.Completed.key,
    direction = PaymentDirection.Credit.key,
  ).id()
  val invoice3 = paymentQueries.insertInvoice(
    paymentId = payment3,
    totalOwed = 0.money.toCents(),
    totalPaid = 10.money.toCents()
  ).id()
  paymentQueries.insertInvoiceItem(invoice3, order4, 10.money.toCents(), 0)

  val order5 = orderQueries.insert(restaurantId = 1, createdAt = Instant.now()).id()
  orderQueries.updatePayer(payerId = user1, id = order5)
  val item6 = orderQueries.insertOrderItemPrice(
    orderId = order5,
    menuItemId = menu1,
    price = 10.money.toCents(),
    datetime = Instant.now(),
  ).id()
  orderQueries.insertOrderItem(customerId = user2, quantity = 4, orderItemPriceId = item6, "")
  val payment4 = paymentQueries.insertPayment(
    userId = user1,
    amount = 40.money.toCents(),
    createdAt = Instant.now(),
    status = PaymentStatus.Canceled.key,
    direction = PaymentDirection.Credit.key,
  ).id()
  val invoice4 = paymentQueries.insertInvoice(
    paymentId = payment4,
    totalOwed = 40.money.toCents(),
    totalPaid = 0.money.toCents()
  ).id()
  paymentQueries.insertInvoiceItem(
    invoiceId = invoice4,
    orderId = order5,
    amountPaid = 0.money.toCents(),
    amountOwed = 40.money.toCents()
  )
}