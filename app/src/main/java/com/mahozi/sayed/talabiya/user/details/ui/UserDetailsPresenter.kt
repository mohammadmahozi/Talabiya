package com.mahozi.sayed.talabiya.user.details.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.order.store.OrderStore
import com.mahozi.sayed.talabiya.payment.PaymentStore
import com.mahozi.sayed.talabiya.user.details.UserDetailsScreen
import com.mahozi.sayed.talabiya.user.details.payment.create.CreateUserPaymentEvent
import com.mahozi.sayed.talabiya.user.details.payment.create.CreateUserPaymentPresenter
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class UserDetailsPresenter @AssistedInject constructor(
  @Assisted private val screen: UserDetailsScreen,
  private val orderStore: OrderStore,
  private val paymentStore: PaymentStore,
  createUserPaymentPresenterFactory: CreateUserPaymentPresenter.Factory,
) : Presenter<UserDetailsEvent, UserDetailsState> {

  private val createUserPaymentPresenter = createUserPaymentPresenterFactory.create(screen.userId)

  @Composable
  override fun start(events: Flow<UserDetailsEvent>): UserDetailsState {
    var tab by remember { mutableStateOf(UserDetailsTab.CreatePayment) }
    val payments by paymentStore.getUserPayments(screen.userId).collectAsState(listOf())
    val orders by orderStore.getUserOrders(screen.userId).collectAsState(listOf())

    val createPaymentEvents = remember { MutableSharedFlow<CreateUserPaymentEvent>(extraBufferCapacity = 5) }
    val createPaymentState = createUserPaymentPresenter.start(createPaymentEvents)

    CollectEvents(events) { event ->
      when (event) {
        is UserDetailsEvent.SelectTab -> tab = event.tab
        is UserDetailsEvent.CreatePaymentEvent -> {
          launch { createPaymentEvents.emit(event.event) }
        }
      }
    }

    return UserDetailsState(
      userName = "",
      tab = tab,
      createUserPaymentState = createPaymentState,
      payments = payments,
      orders = orders
    )
  }

  @AssistedFactory
  interface Factory {
    fun create(screen: UserDetailsScreen): UserDetailsPresenter
  }
}