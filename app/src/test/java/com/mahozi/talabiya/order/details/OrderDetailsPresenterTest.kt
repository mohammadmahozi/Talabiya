package com.mahozi.talabiya.order.details

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.core.datetime.withDate
import com.mahozi.sayed.talabiya.core.datetime.withTime
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.ui.components.TlbDatePickerEvent
import com.mahozi.sayed.talabiya.core.ui.components.TlbDatePickerState
import com.mahozi.sayed.talabiya.core.ui.components.TlbTimePickerEvent
import com.mahozi.sayed.talabiya.core.ui.components.TlbTimePickerState
import com.mahozi.sayed.talabiya.order.OrderStatus
import com.mahozi.sayed.talabiya.order.details.OrderDetailsEvent
import com.mahozi.sayed.talabiya.order.details.OrderDetailsEvent.OrderInfoEvent
import com.mahozi.sayed.talabiya.order.details.OrderDetailsEvent.OrderInfoEvent.DateEvent
import com.mahozi.sayed.talabiya.order.details.OrderDetailsPresenter
import com.mahozi.sayed.talabiya.order.details.OrderDetailsState
import com.mahozi.sayed.talabiya.order.details.OrderInfoState
import com.mahozi.sayed.talabiya.order.details.SubordersState
import com.mahozi.sayed.talabiya.order.details.full.FullOrderItem
import com.mahozi.sayed.talabiya.order.details.suborder.OrderItem
import com.mahozi.sayed.talabiya.order.details.suborder.Suborder
import com.mahozi.sayed.talabiya.order.list.ui.OrdersScreen
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.user.data.UserStore
import com.mahozi.talabiya.FakeNavigator
import com.mahozi.talabiya.MainDispatcherRule
import com.mahozi.talabiya.TalabiyaDatabase
import com.mahozi.talabiya.database.createDatabase
import com.mahozi.talabiya.database.orderStore
import com.mahozi.talabiya.database.seedOrder
import com.mahozi.talabiya.database.userStore
import com.mahozi.talabiya.test
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import user.UserEntity
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class OrderDetailsPresenterTest {

  private lateinit var database: TalabiyaDatabase
  private lateinit var orderStore: OrderStore
  private lateinit var userStore: UserStore
  private lateinit var presenter: OrderDetailsPresenter

  private lateinit var events: Channel<OrderDetailsEvent>
  private val eventsFlow get() = events.receiveAsFlow()

  private val navigator = FakeNavigator(OrdersScreen)
  private val clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setup() {
    database = createDatabase()
    orderStore = database.orderStore()
    userStore = database.userStore()

    presenter = OrderDetailsPresenter(
      orderId = 1,
      orderStore = orderStore,
      userStore = userStore,
      navigator = navigator,
      clock = clock,
    )

    events = Channel()
  }

  @Test
  fun `initial state`() {
    runTest {
      database.seedOrder()
      presenter.test(eventsFlow) {
        assertThat(awaitItem()).isEqualTo(
          OrderDetailsState(
            orderId = 1,
            info = null,
            subordersState = null,
            fullOrderItems = listOf()
          )
        )
        skipItems(1) //skip intermediate state
        assertThat(50.0.money).isEqualTo(50.money)
        assertThat(awaitItem()).isEqualTo(createOrder())
      }
    }
  }

  @Test
  fun `update date`() {
    runTest {
      val date = LocalDate.of(2026, 2, 19)
      database.seedOrder(date = date)
      presenter.test(eventsFlow) {
        skipItems(3) //skip initial states
        var order = createOrder(date = date)

        events.send(OrderInfoEvent.DateClicked)
        order = order.copy(
          info = order.info!!.copy(
            datePickerState = TlbDatePickerState(
              LocalDate.now(clock)
            )
          )
        )
        assertThat(awaitItem()).isEqualTo(order)

        val date = LocalDate.of(2026, 2, 20)
        val newDateTime = order.info!!.datetime.withDate(date)

        events.send(DateEvent(TlbDatePickerEvent.SelectDate(date)))
        skipItems(1) //Skip date picker dismiss

        order = order.copy(
          info = order.info.copy(datetime = newDateTime, datePickerState = null)
        )
        assertThat(awaitItem()).isEqualTo(order)
      }
    }
  }

  @Test
  fun `update time`() {
    runTest {
      val time = LocalTime.of(10, 0)
      database.seedOrder(time = time)
      presenter.test(eventsFlow) {
        skipItems(3) //skip initial states
        var order = createOrder(time = time)

        events.send(OrderInfoEvent.TimeClicked)
        order = order.copy(
          info = order.info!!.copy(
            timePickerState = TlbTimePickerState(initialTime = LocalTime.now(clock))
          )
        )
        assertThat(awaitItem()).isEqualTo(order)

        val newTime = LocalTime.of(15, 0)
        events.send(OrderInfoEvent.TimeEvent(TlbTimePickerEvent.SelectTime(newTime)))
        order = order.copy(
          info = order.info!!.copy(
            datetime = order.info.datetime.withTime(newTime),
            timePickerState = null
          )
        )
        skipItems(1) //Skip time picker dismiss
        assertThat(awaitItem()).isEqualTo(order)
      }
    }
  }

  @Test
  fun `update invoice`() {
    runTest {
      database.seedOrder(invoice = "123")
      presenter.test(eventsFlow) {
        skipItems(3) //skip initial states
        var order = createOrder(invoice = "123")

        events.send(OrderInfoEvent.ChangeInvoice("Test"))
        order = order.copy(
          info = order.info!!.copy(invoice = "Test")
        )
        assertThat(awaitItem()).isEqualTo(order)
      }
    }
  }


  @Test
  fun `update note`() {
    runTest {
      database.seedOrder(note = "")
      presenter.test(eventsFlow) {
        skipItems(3) //skip initial states
        var order = createOrder(note = "")

        events.send(OrderInfoEvent.NoteChanged("Test"))
        order = order.copy(
          info = order.info!!.copy(note = "Test")
        )
        assertThat(awaitItem()).isEqualTo(order)
        assertThat(awaitItem()).isEqualTo(order)
      }
    }
  }

  private fun createOrder(
    date: LocalDate = LocalDate.now(clock),
    time: LocalTime = LocalTime.now(clock),
    total: Money = 50.money,
    invoice: String? = null,
    payer: String? = null,
    note: String = "",
    datePickerState: TlbDatePickerState? = null,
    timePickerState: TlbTimePickerState? = null,
    suborders: List<Suborder> = listOf(
      Suborder(
        id = 1, userId = 1, user = "User",
        items = listOf(OrderItem(1, 1, "Item", 5, 50.money)),
        total = 50.money
      )
    ),
    users: List<UserEntity> = listOf(UserEntity(1, "User")),
    fullOrderItems: List<FullOrderItem> = listOf(FullOrderItem(1, "Item", 5)),
  ) = OrderDetailsState(
    orderId = 1,
    info = OrderInfoState(
      datetime = ZonedDateTime.of(
        /* date = */ date,
        /* time = */ time,
        /* zone = */ ZoneId.systemDefault()
      ).toInstant()
      .truncatedTo(ChronoUnit.SECONDS),
      total = total,
      invoice = invoice,
      payer = payer,
      status = OrderStatus.COMPLETE,
      note = note,
      datePickerState = datePickerState,
      timePickerState = timePickerState
    ),
    subordersState = SubordersState(suborders = suborders, users = users),
    fullOrderItems = fullOrderItems,
  )


}