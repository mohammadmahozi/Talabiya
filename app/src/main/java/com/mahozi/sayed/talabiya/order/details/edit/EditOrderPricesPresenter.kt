package com.mahozi.sayed.talabiya.order.details.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.order.store.OrderStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow


class EditOrderPricesPresenter @AssistedInject constructor(
  @Assisted private val orderId: Long,
  private val orderStore: OrderStore,
  private val navigator: Navigator,
): Presenter<EditOrderPricesScreenEvent, EditOrderPricesScreenState> {

  @Composable
  override fun start(events: Flow<EditOrderPricesScreenEvent>): EditOrderPricesScreenState {
    val items by remember { orderStore.getPricedOrderItems(orderId) }.collectAsState(listOf())
    var editingItem by remember { mutableStateOf(null as PricedOrderItem?) }
    var price by remember { mutableStateOf("") }

    //TODO use datastore to read setAsDefault price
    var setAsDefaultPrice by remember { mutableStateOf(false) }

    CollectEvents(events) { event ->
      when(event) {
        is EditOrderPricesScreenEvent.ItemClicked -> editingItem = event.item
        EditOrderPricesScreenEvent.CancelPriceChange -> editingItem = null
        EditOrderPricesScreenEvent.ConfirmPriceChange -> {

          editingItem = null
        }
        is EditOrderPricesScreenEvent.PriceChange -> price = event.price
        is EditOrderPricesScreenEvent.SetAsDefaultPriceChange -> { setAsDefaultPrice = event.setAsDefaultPrice }
        is EditOrderPricesScreenEvent.Back -> navigator.back()
      }
    }

    return EditOrderPricesScreenState(
      items = items,
      editingItem = editingItem,
      price = price,
      setAsDefaultPrice = setAsDefaultPrice
    )
  }

  @AssistedFactory
  interface Factory {
    fun create(orderId: Long): EditOrderPricesPresenter
  }
}