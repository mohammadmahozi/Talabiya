package com.mahozi.sayed.talabiya.order.details.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Ui
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.HorizontalSpacer
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaBar
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaCheckbox
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaOutlinedTextField
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaTextButton
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaTopBarDefaults
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditOrderPricesScreen(
  val orderId: Long,
) : Screen

class EditOrderPricesScreenUi : Ui<EditOrderPricesScreenState, EditOrderPricesScreenEvent> {

  @Composable
  override fun Content(
    state: EditOrderPricesScreenState,
    onEvent: (EditOrderPricesScreenEvent) -> Unit
  ) {
    EditOrderScreen(
      state = state,
      onEvent = onEvent
    )
  }
}

@Composable
fun EditOrderScreen(
  state: EditOrderPricesScreenState,
  onEvent: (EditOrderPricesScreenEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      TalabiyaBar(
        title = R.string.edit_prices,
        navigationIcon = {
          TalabiyaTopBarDefaults.BackIcon({ onEvent(EditOrderPricesScreenEvent.Back) })
        }
      )
    }
  ) { paddingValues ->

    if (state.editingItem != null) {
      PriceDialog(
        state.editingItem,
        state.price,
        onPriceChange = { onEvent(EditOrderPricesScreenEvent.PriceChange(it)) },
        setAsDefaultPrice = state.setAsDefaultPrice,
        onSetAsDefaultPriceChange = { onEvent(EditOrderPricesScreenEvent.SetAsDefaultPriceChange(it)) },
        onDismissRequest = { onEvent(EditOrderPricesScreenEvent.CancelPriceChange) },
        onConfirmPriceChange = { onEvent(EditOrderPricesScreenEvent.ConfirmPriceChange) }
      )
    }
    LazyColumn(
      modifier = modifier
        .padding(paddingValues)
    ) {
      items(state.items) { item ->
        Item(
          item = item,
          onClick = { onEvent(EditOrderPricesScreenEvent.ItemClicked(item)) }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewItem() {
  AppTheme {
    Item(
      item = PricedOrderItem(
        orderItemId = 1L,
        menuItemId = 1L,
        name = "Sample Item",
        price = 12.99.money
      ),
      onClick = {}
    )
  }
}

@Composable
private fun Item(
  item: PricedOrderItem,
  onClick: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(16.dp)
      .fillMaxWidth()
  ) {
    Text(text = item.name)

    Spacer(modifier = Modifier.weight(1F))

    Text(text = item.price.format())
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PriceDialogPreview() {
  AppTheme {
    PriceDialog(
      item = PricedOrderItem(
        orderItemId = 1L,
        menuItemId = 1L,
        name = "Sample Item",
        price = 12.99.money
      ),
      price = "15",
      onPriceChange = {},
      setAsDefaultPrice = true,
      onSetAsDefaultPriceChange = {},
      onConfirmPriceChange = {},
      onDismissRequest = {},
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriceDialog(
  item: PricedOrderItem,
  price: String,
  onPriceChange: (String) -> Unit,
  setAsDefaultPrice: Boolean,
  onSetAsDefaultPriceChange: (Boolean) -> Unit,
  onConfirmPriceChange: () -> Unit,
  onDismissRequest: () -> Unit
) {

  Dialog (
    onDismissRequest = onDismissRequest,
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .background(AppTheme.colors.material.background, shape = AppTheme.shapes.medium)
        .padding(8.dp)
    ) {
      Text(stringResource(R.string.change_price_of, item.name))

      val focusRequester = remember { FocusRequester() }
      LaunchedEffect(Unit) {
        focusRequester.requestFocus()
      }

      TalabiyaOutlinedTextField(
        price,
        onPriceChange,
        label = { Text(stringResource(R.string.new_price)) },
        keyboardOptions = KeyboardOptions.Default.copy(
          keyboardType = KeyboardType.Decimal
        ),
        modifier = Modifier
          .focusRequester(focusRequester)
      )

      TalabiyaCheckbox(
        text = stringResource(R.string.set_as_default_price),
        checked = setAsDefaultPrice,
        onCheckedChange = onSetAsDefaultPriceChange
      )

      Row(
        modifier = Modifier
          .align(Alignment.End)
      ) {
        TalabiyaTextButton(
          stringResource(R.string.cancel),
          onClick = onDismissRequest
        )

        HorizontalSpacer(16.dp)

        TalabiyaTextButton(
          stringResource(R.string.confirm),
          onClick = onConfirmPriceChange
        )
      }
    }
  }
}
