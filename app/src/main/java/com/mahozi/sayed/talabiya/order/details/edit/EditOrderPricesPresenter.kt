package com.mahozi.sayed.talabiya.order.details.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.mahozi.sayed.talabiya.core.CollectEvents
import com.mahozi.sayed.talabiya.core.Presenter
import com.mahozi.sayed.talabiya.core.SettingsStore
import com.mahozi.sayed.talabiya.core.isDecimal
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Navigator
import com.mahozi.sayed.talabiya.order.store.OrderStore
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class EditOrderPricesPresenter @AssistedInject constructor(
  @Assisted private val orderId: Long,
  private val orderStore: OrderStore,
  private val settingsStore: SettingsStore,
  private val navigator: Navigator,
): Presenter<EditOrderPricesScreenEvent, EditOrderPricesScreenState> {

  @Composable
  override fun start(events: Flow<EditOrderPricesScreenEvent>): EditOrderPricesScreenState {
    val items by remember { orderStore.getPricedOrderItems(orderId) }.collectAsState(listOf())
    var editingItem by remember { mutableStateOf(null as PricedOrderItem?) }
    var price by remember { mutableStateOf("") }

    val setNewPriceAsDefault by settingsStore.setNewPriceAsDefault.collectAsState(false)

    CollectEvents(events) { event ->
      when(event) {
        is EditOrderPricesScreenEvent.ItemClicked -> editingItem = event.item
        EditOrderPricesScreenEvent.CancelPriceChange -> {
          editingItem = null
          price = ""
        }
        EditOrderPricesScreenEvent.ConfirmPriceChange -> {
          launch {
            orderStore.updateOrderItemPrice(
              orderId = orderId,
              itemId = editingItem!!.menuItemId,
              price = price.money,
              setNewPriceAsDefault = setNewPriceAsDefault
            )
            price = ""
            editingItem = null
          }
        }
        is EditOrderPricesScreenEvent.PriceChange -> {
          if (event.price.isDecimal()) price = event.price
        }
        is EditOrderPricesScreenEvent.SetAsDefaultPriceChange -> {
          launch {
            settingsStore.setNewPriceAsDefault(event.setAsDefaultPrice)
          }
        }
        is EditOrderPricesScreenEvent.Back -> navigator.back()
      }
    }

    return EditOrderPricesScreenState(
      items = items,
      editingItem = editingItem,
      price = price,
      setNewPriceAsDefault = setNewPriceAsDefault
    )
  }

  @AssistedFactory
  interface Factory {
    fun create(orderId: Long): EditOrderPricesPresenter
  }
}