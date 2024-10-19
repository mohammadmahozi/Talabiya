package com.mahozi.sayed.talabiya.order.details.edit

data class EditOrderPricesScreenState(
  val items: List<PricedOrderItem>,
  val editingItem: PricedOrderItem?,
  val price: String,
  val setAsDefaultPrice: Boolean
)

sealed interface EditOrderPricesScreenEvent {
  data class ItemClicked(val item: PricedOrderItem): EditOrderPricesScreenEvent
  data class PriceChange(val price: String): EditOrderPricesScreenEvent
  object ConfirmPriceChange: EditOrderPricesScreenEvent
  object CancelPriceChange: EditOrderPricesScreenEvent
  data class SetAsDefaultPriceChange(val setAsDefaultPrice: Boolean): EditOrderPricesScreenEvent
  object Back: EditOrderPricesScreenEvent
}