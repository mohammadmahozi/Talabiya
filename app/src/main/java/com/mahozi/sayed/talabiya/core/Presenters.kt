package com.mahozi.sayed.talabiya.core

import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsPresenter
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsScreen
import com.mahozi.sayed.talabiya.order.list.ui.OrdersPresenter
import com.mahozi.sayed.talabiya.order.list.ui.OrdersScreen
import javax.inject.Inject
import javax.inject.Provider


class Presenters @Inject constructor(
  private val ordersPresenter: Provider<OrdersPresenter>,
  private val ordersDetailsPresenter: OrderDetailsPresenter.OrderDetailsPresenterAssistedFactory,
) {

  fun create(screen: Screen): Presenter<*, *> {
    return when (screen) {
      is OrdersScreen -> ordersPresenter.get()
      is OrderDetailsScreen -> ordersDetailsPresenter.create(screen.orderId)
      else -> throw IllegalStateException("Unknown screen $screen")
    }
  }
}