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
): Presenter<EditOrderScreenEvent, EditOrderScreenState> {

  @Composable
  override fun start(events: Flow<EditOrderScreenEvent>): EditOrderScreenState {
    val items by remember { orderStore.getPricedOrderItems(orderId) }.collectAsState(listOf())
    var editingItem by remember { mutableStateOf(null as PricedOrderItem?) }
    var price by remember { mutableStateOf("") }

    //TODO use datastore to read setAsDefault price
    var setAsDefaultPrice by remember { mutableStateOf(false) }

    CollectEvents(events) { event ->
      when(event) {
        is EditOrderScreenEvent.ItemClicked -> editingItem = event.item
        EditOrderScreenEvent.CancelPriceChange -> editingItem = null
        EditOrderScreenEvent.ConfirmPriceChange -> {

          editingItem = null
        }
        is EditOrderScreenEvent.PriceChange -> price = event.price
        is EditOrderScreenEvent.SetAsDefaultPriceChange -> { setAsDefaultPrice = event.setAsDefaultPrice }
        is EditOrderScreenEvent.Back -> navigator.back()
      }
    }

    return EditOrderScreenState(
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