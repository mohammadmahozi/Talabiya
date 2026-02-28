package com.mahozi.talabiya.user

import android.R.attr.direction
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.order.list.ui.OrdersScreen
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.payment.PaymentDirection
import com.mahozi.sayed.talabiya.payment.PaymentStatus
import com.mahozi.sayed.talabiya.payment.PaymentStore
import com.mahozi.sayed.talabiya.payment.key
import com.mahozi.sayed.talabiya.user.details.UserDetailsScreen
import com.mahozi.sayed.talabiya.user.details.payment.create.CreateUserPaymentPresenter
import com.mahozi.sayed.talabiya.user.details.payment.create.CreateUserPaymentState
import com.mahozi.sayed.talabiya.user.details.payment.create.PaymentSummary
import com.mahozi.sayed.talabiya.user.details.payment.create.PaymentTotals
import com.mahozi.sayed.talabiya.user.details.ui.UserDetailsEvent
import com.mahozi.sayed.talabiya.user.details.ui.UserDetailsPresenter
import com.mahozi.sayed.talabiya.user.details.ui.UserDetailsState
import com.mahozi.sayed.talabiya.user.details.ui.UserDetailsTab
import com.mahozi.talabiya.FakeNavigator
import com.mahozi.talabiya.MainDispatcherRule
import com.mahozi.talabiya.TalabiyaDatabase
import com.mahozi.talabiya.clock
import com.mahozi.talabiya.database.createDatabase
import com.mahozi.talabiya.database.orderStore
import com.mahozi.talabiya.database.paymentStore
import com.mahozi.talabiya.test
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class UserDetailsPresenterTest {

  private lateinit var database: TalabiyaDatabase
  private lateinit var orderStore: OrderStore
  private lateinit var paymentStore: PaymentStore
  private lateinit var presenter: UserDetailsPresenter

  private lateinit var events: Channel<UserDetailsEvent>
  private val eventsFlow get() = events.receiveAsFlow()

  private val navigator = FakeNavigator(OrdersScreen)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    database = createDatabase()
    orderStore = database.orderStore()
    paymentStore = database.paymentStore()

    presenter = UserDetailsPresenter(
      screen = UserDetailsScreen(1, "User"),
      orderStore = orderStore,
      paymentStore = paymentStore,
      createUserPaymentPresenterFactory = object : CreateUserPaymentPresenter.Factory {
        override fun create(userId: Long): CreateUserPaymentPresenter {
          return CreateUserPaymentPresenter(userId, paymentStore, clock)
        }
      }
    )

    events = Channel()
  }

  @Test
  fun `initial state`() = runTest {
    presenter.test(eventsFlow) {
      val state = UserDetailsState(
        userName = "User",
        tab = UserDetailsTab.CreatePayment,
        createUserPaymentState = CreateUserPaymentState(
          user = "",
          numberOfSelectedOrders = 0,
          allOrdersSelected = true,
          summary = PaymentSummary(
            from = Instant.now(clock),
            to = Instant.now(clock),
            ordersPlaced = 0,
            ordersCovered = 0
          ),
          totals = PaymentTotals(
            placedOrdersTotal = 0.money,
            coveredOrdersTotal = 0.money,
            finalTotal = 0.money
          ),
          showPay = false,
          selectUnpaidOrderState = null
        ),
        payments = listOf(),
        orders = listOf()
      )
      assertThat(awaitItem()).isEqualTo(state)
      println(awaitItem())
    }
  }

  fun TalabiyaDatabase.seed() {
    fun Any.id() = orderQueries.lastInsertRowId().executeAsOne()

    val user1 = userQueries.insert("User1").id()
    val user2 = userQueries.insert("User2").id()
    val user3 = userQueries.insert("User3").id()

    val restaurant = restaurantQueries.insert("Restaurant").id()
    val menuItem = menuItemQueries.insert(restaurantId = restaurant, name = "Item", category = "Pastry").id()
    menuItemQueries.insertPrice(
      menuItemId = menuItem,
      datetime = Instant.now(clock),
      price = 10.money.toCents()
    )

    val order1 = orderQueries.insert(
      restaurantId = restaurant,
      createdAt = Instant.now(clock).truncatedTo(ChronoUnit.SECONDS)
    ).id()
    orderQueries.updatePayer(payerId = user1, id = order1)
    val orderItem = orderQueries.insertOrderItemPrice(
      orderId = order1,
      menuItemId = menuItem,
      price = 10.money.toCents(),
      datetime = Instant.now(clock)
    ).id()
    orderQueries.insertOrderItem(customerId = user1, quantity = 5, orderItemPriceId = orderItem)
    orderQueries.insertOrderItem(customerId = user2, quantity = 1, orderItemPriceId = orderItem)

    val payment = paymentQueries.insertPayment(
      userId = user1,
      amount = 50.money.toCents(),
      createdAt = Instant.now(clock),
      status = PaymentStatus.Completed.key,
      direction = PaymentDirection.Debit.key
    ).id()

    val invoice = paymentQueries.insertInvoice(
      paymentId = payment,
      totalOwed = 50.money.toCents(),
      totalPaid = 60.money.toCents()
    ).id()

    paymentQueries.insertInvoiceItem(
      invoiceId = invoice,
      orderId = order1,
      amountOwed = 50.money.toCents(),
      amountPaid = 60.money.toCents()
    )
  }
}