package com.mahozi.sayed.talabiya.order.details.edit

data class EditOrderScreenState(
  val items: List<PricedOrderItem>,
  val editingItem: PricedOrderItem?,
  val price: String,
  val setAsDefaultPrice: Boolean
)

sealed interface EditOrderScreenEvent {
  data class ItemClicked(val item: PricedOrderItem): EditOrderScreenEvent
  data class PriceChange(val price: String): EditOrderScreenEvent
  object ConfirmPriceChange: EditOrderScreenEvent
  object CancelPriceChange: EditOrderScreenEvent
  data class SetAsDefaultPriceChange(val setAsDefaultPrice: Boolean): EditOrderScreenEvent
  object Back: EditOrderScreenEvent
}