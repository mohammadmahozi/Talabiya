package com.mahozi.sayed.talabiya.order.suborder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.navigation.Screen
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaCenterAlignedTopBar
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaOutlinedTextField
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaSearchBar
import com.mahozi.sayed.talabiya.core.ui.components.TalabiyaTopBarDefaults
import com.mahozi.sayed.talabiya.core.ui.components.TlbButton
import com.mahozi.sayed.talabiya.core.ui.components.zero
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.core.ui.theme.onSurfaceVariant
import com.mahozi.sayed.talabiya.order.details.suborder.OrderItem
import com.mahozi.sayed.talabiya.resturant.menu.MenuItem
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateSuborderScreen(
  val orderId: Long,
  val userid: Long,
) : Screen

@Preview
@Composable
private fun PreviewCreateSuborderScreen() {
  val menu = listOf(
    MenuItemState(
      id = 0,
      name = "Item 1",
      category = "Pastry",
      priceId = 0L,
      price = 14.money,
      quantity = 1,
      note = "Note",
    )
  )
  AppTheme {
    CreateSuborderScreen(
      state = CreateSuborderState(
        query = "",
        menuItems = menu,
        addedItems = listOf(),
        openedOrderItemState = null
      ),
      onEvent = {},
      onBack = {}
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSuborderScreen(
  state: CreateSuborderState,
  onEvent: (CreateSuborderEvent) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val scope = rememberCoroutineScope()

  val quantitySheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true,
  )
  if (state.openedOrderItemState != null) {
    ModalBottomSheet(
      onDismissRequest = { onEvent(CreateSuborderEvent.OnCancelAddingMenuItem) },
      sheetState = quantitySheetState,
    ) {
      AddOrderItem(
        state = state.openedOrderItemState,
        onQuantityChanged = { onEvent(CreateSuborderEvent.QuantityChanged(it)) },
        onDelete = { onEvent(CreateSuborderEvent.DeleteItem) },
        onNoteChanged = { onEvent(CreateSuborderEvent.ChangeNote(it))},
        onConfirm = { onEvent(CreateSuborderEvent.OnSaveMenuItemClicked) }
      )
    }
  }

  val orderSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true,
  )
  var orderSheetExpanded by remember { mutableStateOf(false) }
  if (orderSheetExpanded) {
    ModalBottomSheet(
      onDismissRequest = { orderSheetExpanded = false },
      sheetState = orderSheetState,
      dragHandle = {
        TalabiyaCenterAlignedTopBar(
          title = { Text(text = stringResource(id = R.string.current_order)) },
          windowInsets = WindowInsets.zero
        )
      },
    ) {
      UserOrderItems(
        items = state.addedItems,
        onClick = { }
      )
    }
  }

  Scaffold(
    topBar = {
      TalabiyaSearchBar(
        title = { Text(stringResource(R.string.menu)) },
        query = state.query,
        onQueryChanged = { onEvent(CreateSuborderEvent.QueryChanged(it)) },
        navigationIcon = {
          TalabiyaTopBarDefaults.BackIcon(onClick = onBack)
        },
        actions = {
          IconButton(onClick = { onEvent(CreateSuborderEvent.AddMenuItemClicked) }) {
            Icon(
              painterResource(R.drawable.ic_add_white_24dp),
              stringResource(R.string.add_new_item)
            )
          }

          if (state.addedItems.isNotEmpty()) {
            IconButton(onClick = { orderSheetExpanded = true }) {
              Icon(
                painter = painterResource(id = R.drawable.ic_order),
                contentDescription = stringResource(R.string.view_selected_items),
              )
            }
          }
        }
      )
    }
  ) { paddingValues ->
    Row(
      modifier = modifier
        .padding(paddingValues)
    ) {
      val lazyState = rememberLazyListState()
      LazyColumn(
        state = lazyState,
        modifier = Modifier
          .weight(1F)
      ) {
        items(state.menuItems) { menuItem ->
          MenuItem(
            item = menuItem,
            onItemClicked = { onEvent(CreateSuborderEvent.MenuItemClicked(it)) }
          )
          HorizontalDivider()
        }
      }

      VerticalDivider()

      //TODO A big is causing state value inside the lambda to always be the first value (empty menu items)
      // So we will only show the index when it is no empty. Investigate later
      if (state.menuItems.isNotEmpty()) {
        AlphabetIndex(
          onLetterSelected = { letter ->
            val index = state.menuItems.map { item -> item.name.first().toString() }.indexOf(letter)
            if (index != -1) {
              scope.launch {
                lazyState.animateScrollToItem(index)
              }
            }
          }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewUserOrderItems() {
  AppTheme {
    UserOrderItems(
      listOf(
        OrderItem(
          id = 0L,
          menuItemId = 0L,
          name = "Name",
          quantity = 1,
          total = 20.money,
          note = "Note"
        ),
        OrderItem(
          id = 0L,
          menuItemId = 0L,
          name = "Name",
          quantity = 1,
          total = 20.money,
          note = ""
        ),
        OrderItem(
          id = 0L,
          menuItemId = 0L,
          name = "Name",
          quantity = 1,
          total = 20.money,
          note = ""
        ),
      ),
      onClick = {}
    )
  }
}

@Composable
private fun UserOrderItems(
  items: List<OrderItem>,
  onClick: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxHeight(.5F)
  ) {
    items.forEach { item ->
      Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
          .clickable { onClick() }
          .padding(vertical = 12.dp, horizontal = 16.dp)
      ) {
        Text(
          text = item.quantity.toString(),
          style = AppTheme.type.titleMedium
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = item.name,
            style = AppTheme.type.titleMedium
          )

          if (item.note.isNotEmpty()) {
            Text(
              text = item.note,
              style = AppTheme.type.bodyMedium.onSurfaceVariant
            )
          }
        }

        Spacer(Modifier.weight(1F))

        Text(
          text = item.total.format(),
          style = AppTheme.type.title
        )
      }
    }
  }
}

@Composable
private fun MenuItem(
  item: MenuItemState,
  onItemClicked: (MenuItemState) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier
      .clickable { onItemClicked(item) }
      .padding(vertical = 12.dp, horizontal = 16.dp)
  ) {
    if (item.quantity > 0) {
      Text(
        text = "${item.quantity}x",
        style = AppTheme.type.title,
        color = AppTheme.colors.primary,
        modifier = Modifier.alignByBaseline()
      )
    }

    Column(
      verticalArrangement = Arrangement.spacedBy(4.dp),
      modifier = Modifier.alignByBaseline()
    ) {
      Text(
        text = item.name,
        style = AppTheme.type.titleMedium,

      )

      if (item.note.isNotEmpty()) {
        Text(
          text = item.note,
          style = AppTheme.type.bodyMedium.onSurfaceVariant
        )
      }
    }

    Spacer(Modifier.weight(1F))

    Text(
      text = item.price.format(),
      style = AppTheme.type.title
    )
  }
}

@Composable
private fun AlphabetIndex(
  onLetterSelected: (letter: String) -> Unit
) {
  val letters = stringResource(R.string.alphabet).split(" ")
  var selectedLetter by remember { mutableStateOf("") }
  var itemHeight by remember { mutableIntStateOf(0) }

  fun updateSelectedLetter(offset: Offset) {
    val indexOfTouchedLetter = (offset.y / itemHeight).toInt()
    selectedLetter = letters.getOrNull(indexOfTouchedLetter) ?: ""
    if (selectedLetter.isNotEmpty()) {
      onLetterSelected(selectedLetter)
    }
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxHeight()
      .onGloballyPositioned { coordinates ->
        val columnHeight = coordinates.size.height
        itemHeight = columnHeight / letters.size
      }
      .pointerInput(Unit) {
        detectTapGestures(
          onPress = { offset ->
            updateSelectedLetter(offset)
            awaitRelease()
            selectedLetter = ""
          }
        )
      }
      .pointerInput(Unit) {
        detectDragGestures(
          onDrag = { change, _ ->
            updateSelectedLetter(change.position)
          },
          onDragEnd = {
            selectedLetter = ""
          }
        )
      }
  ) {
    letters.forEach {
      Text(
        text = it,
        fontSize = 11.sp,
        modifier = Modifier
          .weight(1F)
          .padding(horizontal = 2.dp)
      )
    }
  }

  if (selectedLetter.isNotEmpty()) {
    Popup(
      alignment = Alignment.Center
    ) {
      Text(text = selectedLetter)
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAddOrderItem() {
  AppTheme {
    AddOrderItem(
      state = OpenedOrderItemState(
        menuItemId = 1,
        quantity = 13,
        price = 15.money,
      ),
      onQuantityChanged = {},
      onDelete = {},
      onNoteChanged = {},
      onConfirm = {}
    )
  }
}

@Composable
private fun AddOrderItem(
  state: OpenedOrderItemState,
  onQuantityChanged: (newQuantity: Int) -> Unit,
  onDelete: () -> Unit,
  onNoteChanged: (String) -> Unit,
  onConfirm: () -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(20.dp),
    modifier = Modifier
      .padding(16.dp),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = stringResource(R.string.quantity),
        style = AppTheme.type.title
      )

      Spacer(Modifier.weight(1F))

      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        val iconModifier = Modifier
          .padding(2.dp)
          .background(AppTheme.colors.surfaceContainerHigh, CircleShape)

        if (state.showDelete) {
          Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.delete),
            tint = AppTheme.colors.error,
            modifier = Modifier
              .clickable { onDelete() }
              .then(iconModifier)
          )
        } else {
          Icon(
            painter = painterResource(R.drawable.ic_remove),
            contentDescription = stringResource(R.string.decrease_quantity),
            modifier = Modifier
              .clickable { onQuantityChanged(state.quantity - 1) }
              .then(iconModifier)
          )
        }

        Text(
          text = state.quantity.toString(),
          style = AppTheme.type.title,
          modifier = Modifier
            .padding(horizontal = 4.dp)
        )

        Icon(
          painter = painterResource(R.drawable.ic_add_white_24dp),
          contentDescription = stringResource(R.string.increase_quantity),
          modifier = Modifier
            .clickable { onQuantityChanged(state.quantity + 1) }
            .then(iconModifier)
        )
      }
    }

    val focusManager = LocalFocusManager.current

    TalabiyaOutlinedTextField(
      value = state.note,
      onValueChange = onNoteChanged,
      placeholder = { Text(stringResource(R.string.note)) },
      shape = AppTheme.shapes.medium,
      singleLine = true,
      maxLines = 1,
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions(
        onDone = { focusManager.clearFocus() }
      ),
      modifier = Modifier.fillMaxWidth()
    )

    TlbButton(
      text = stringResource(R.string.confirm),
      onClick = { onConfirm() },
      modifier = Modifier.fillMaxWidth()
    )
  }
}

