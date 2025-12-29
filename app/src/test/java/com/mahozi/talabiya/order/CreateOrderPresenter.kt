package com.mahozi.talabiya.order

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.mahozi.sayed.talabiya.order.create.CreateOrderEvent
import com.mahozi.sayed.talabiya.order.create.CreateOrderPresenter
import com.mahozi.sayed.talabiya.order.create.CreateOrderState
import com.mahozi.sayed.talabiya.order.list.Order
import com.mahozi.sayed.talabiya.order.list.ui.OrdersScreen
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import com.mahozi.talabiya.Database
import com.mahozi.talabiya.FakeNavigator
import com.mahozi.talabiya.MainDispatcherRule
import com.mahozi.talabiya.database.createDatabase
import com.mahozi.talabiya.test
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import restaurant.RestaurantEntity
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class CreateOrderPresenterTest() {

  private lateinit var database: Database
  private lateinit var orderStore: OrderStore
  private lateinit var restaurantStore: RestaurantStore
  private lateinit var presenter: CreateOrderPresenter

  private lateinit var events: Channel<CreateOrderEvent>
  private val eventsFlow get() = events.receiveAsFlow()

  private val navigator = FakeNavigator(OrdersScreen)
  private val clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

  private val restaurant = RestaurantEntity(id = 1, name = "A")

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Before
  fun setUp() {
    database = createDatabase()
    orderStore = OrderStore(
      orderQueries = database.orderQueries,
      menuQueries = database.menuItemQueries,
      dispatcher = mainDispatcherRule.testDispatcher
    )
    restaurantStore = RestaurantStore(
      restaurantQueries = database.restaurantQueries,
      menuItemQueries = database.menuItemQueries,
      dispatcher = mainDispatcherRule.testDispatcher
    )

    presenter = CreateOrderPresenter(
      orderStore = orderStore,
      restaurantStore = restaurantStore,
      navigator = navigator,
      clock = clock,
    )

    events = Channel()
  }

  @Test
  fun `initial state`() {
    runTest {
      restaurantStore.createRestaurant(restaurant.name)
      presenter.test(eventsFlow) {
        val state = CreateOrderState(
          selectedRestaurant = null,
          restaurants = listOf(),
          date = LocalDate.now(clock),
          time = LocalTime.now(clock),
          canConfirm = false,
        )
        assertThat(awaitItem()).isEqualTo(state)
        val restaurants = listOf(restaurant)
        assertThat(awaitItem()).isEqualTo(state.copy(restaurants = restaurants))
      }
    }
  }

  @Test
  fun `changing restaurant, date, and time`() {
    runTest {
      restaurantStore.createRestaurant(restaurant.name)
      presenter.test(eventsFlow) {
        skipItems(2) //skip initial state
        events.send(CreateOrderEvent.RestaurantSelected(restaurant))

        val date = LocalDate.now(clock).plusDays(1)
        events.send(CreateOrderEvent.DateSelected(date))

        val time = LocalTime.now(clock).plusHours(1)
        events.send(CreateOrderEvent.TimeSelected(time))

        skipItems(2)
        val state = CreateOrderState(
          selectedRestaurant = restaurant,
          restaurants = listOf(restaurant),
          date = date,
          time = time,
          canConfirm = true,
        )
        assertThat(awaitItem()).isEqualTo(state)
      }
    }
  }

  @Test
  fun `create order`() {
    runTest {
      restaurantStore.createRestaurant(restaurant.name)
      presenter.test(eventsFlow) {
        skipItems(2) //skip initial state

        events.send(CreateOrderEvent.RestaurantSelected(restaurant))
        skipItems(1)

        events.send(CreateOrderEvent.CreateOrder)
        assertThat(navigator.currentScreen()).isEqualTo(OrdersScreen)
        val order = Order(
          id = 1L,
          restaurant = restaurant.name,
          createdAt = Instant.now(clock).truncatedTo(ChronoUnit.SECONDS)
        )
        assertThat(orderStore.orders.first()).isEqualTo(listOf(order))
      }
    }
  }
}