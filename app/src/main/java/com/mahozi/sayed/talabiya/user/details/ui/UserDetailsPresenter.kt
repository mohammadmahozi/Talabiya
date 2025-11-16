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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserDetailsPresenter @AssistedInject constructor(
  @Assisted private val screen: UserDetailsScreen,
  private val orderStore: OrderStore,
  private val paymentStore: PaymentStore,
) : Presenter<UserDetailsEvent, UserDetailsState> {

  @Composable
  override fun start(events: Flow<UserDetailsEvent>): UserDetailsState {
    var tab by remember { mutableStateOf(UserDetailsTab.Payments) }
    val payments by paymentStore.getUserPayments(screen.userId).collectAsState(listOf())
    val orders by orderStore.getUserOrders(screen.userId).collectAsState(listOf())

    CollectEvents(events) { event ->
      when (event) {
        is UserDetailsEvent.SelectTab -> tab = event.tab
      }
    }

    return UserDetailsState(
      userName = "",
      tab = tab,
      payments = payments,
      orders = orders
    )
  }

  @AssistedFactory
  interface Factory {
    fun create(screen: UserDetailsScreen): UserDetailsPresenter
  }
}