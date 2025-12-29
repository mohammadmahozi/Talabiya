package com.mahozi.talabiya.order

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.mahozi.sayed.talabiya.order.create.CreateOrderScreen
import com.mahozi.sayed.talabiya.order.details.OrderDetailsScreen
import com.mahozi.sayed.talabiya.order.list.Order
import com.mahozi.sayed.talabiya.order.list.ui.OrdersEvent
import com.mahozi.sayed.talabiya.order.list.ui.OrdersPresenter
import com.mahozi.sayed.talabiya.order.list.ui.OrdersState
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore
import com.mahozi.talabiya.Database
import com.mahozi.talabiya.FakeNavigator
import com.mahozi.talabiya.MainDispatcherRule
import com.mahozi.talabiya.database.createDatabase
import com.mahozi.talabiya.test
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class OrdersPresenterTest {

  private lateinit var database: Database
  private lateinit var orderStore: OrderStore
  private lateinit var restaurantStore: RestaurantStore

  private val navigator = FakeNavigator()

  private lateinit var presenter: OrdersPresenter

  private lateinit var events: Channel<OrdersEvent>

  private val scheduler = TestCoroutineScheduler()
  private val dispatcher = StandardTestDispatcher(scheduler)

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule(dispatcher)

  @Before
  fun before() {
    database = createDatabase()
    orderStore = OrderStore(
      orderQueries = database.orderQueries,
      menuQueries = database.menuItemQueries,
      dispatcher = dispatcher
    )
    restaurantStore = RestaurantStore(
      restaurantQueries = database.restaurantQueries,
      menuItemQueries = database.menuItemQueries,
      dispatcher = dispatcher
    )
    presenter = OrdersPresenter(
      orderStore = orderStore,
      navigator = navigator,
    )
    events = Channel()
  }

  @Test
  fun loadingOrders() {
    runTest {
      presenter.test(events.receiveAsFlow()) {
        assertThat(awaitItem()).isEqualTo(OrdersState(emptyList()))
        val restaurant = "Restaurant"
        restaurantStore.createRestaurant(restaurant)
        val instant = Instant.now().truncatedTo(ChronoUnit.SECONDS)

        orderStore.createOrder(
          restaurantId = 1L,
          date = instant.atZone(ZoneId.systemDefault()).toLocalDate(),
          time = instant.atZone(ZoneId.systemDefault()).toLocalTime(),
        )
        val order = Order(
          id = 1L,
          restaurant = restaurant,
          createdAt = instant
        )
        assertThat(awaitItem()).isEqualTo(OrdersState(listOf(order)))
      }
    }
  }

  @Test
  fun navigateToOrder() {
    runTest {
      presenter.test(events.receiveAsFlow()) {
        skipItems(1) //Skip initial state
        val order = Order(
          id = 1L,
          restaurant = "Restaurant",
          createdAt = Instant.now()
        )
        events.send(OrdersEvent.OrderClicked(order))
        assertThat(navigator.currentScreen()).isEqualTo(OrderDetailsScreen(1L))
      }
    }
  }

  @Test
  fun navigateToCreateOrder() {
    runTest {
      presenter.test(events.receiveAsFlow()) {
        skipItems(1) //Skip initial state
        events.send(OrdersEvent.CreateOrderClicked)
        assertThat(navigator.currentScreen()).isEqualTo(CreateOrderScreen)
      }
    }
  }
}