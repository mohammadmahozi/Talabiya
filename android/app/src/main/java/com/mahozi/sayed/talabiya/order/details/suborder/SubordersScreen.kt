package com.mahozi.sayed.talabiya.order.details.suborder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Preview
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.ui.ConfirmDialog
import com.mahozi.sayed.talabiya.core.ui.ConfirmState
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.components.HorizontalSpacer
import com.mahozi.sayed.talabiya.core.ui.components.TlbCard
import com.mahozi.sayed.talabiya.core.ui.components.TlbIcon
import com.mahozi.sayed.talabiya.core.ui.localString
import com.mahozi.sayed.talabiya.core.ui.localize
import com.mahozi.sayed.talabiya.core.ui.rememberConfirmState
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.core.ui.theme.onSurfaceVariant
import com.mahozi.sayed.talabiya.order.details.OrderDetailsEvent.SuborderEvent
import com.mahozi.sayed.talabiya.order.details.SubordersState
import kotlinx.coroutines.launch
import user.UserEntity


private class SuborderPreviewParameter : PreviewParameterProvider<Suborder> {
  private val suborder = Suborder(
    id = 0,
    userId = 0,
    user = "Customer",
    items = listOf(
      OrderItem(
        id = 0,
        menuItemId = 0,
        name = "Item 1",
        quantity = 1,
        total = 10.money,
        note = "Note"
      ),
      OrderItem(
        id = 1,
        menuItemId = 1,
        name = "Item 2",
        quantity = 575,
        total = 100.money,
        note = ""
      ),
      OrderItem(
        id = 2,
        menuItemId = 2,
        name = "Item 3",
        quantity = 12,
        total = 4000.money,
        note = ""
      ),
    ),
    total = 6000.money,
  )
  override val values: Sequence<Suborder>
    get() = sequenceOf(
      suborder,
    )
}

@Preview
@Composable
private fun PreviewSubordersScreen() {
  val suborders = SuborderPreviewParameter()
  val state = SubordersState(
    suborders.values.toList(),
    emptyList()
  )
  AppTheme {
    SubordersScreen(
      state = state,
      onEvent = {}
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubordersScreen(
  state: SubordersState,
  onEvent: (SuborderEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()
  val sheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )
  val sheetMaxHeight = LocalConfiguration.current.screenHeightDp * 0.5
  var showUsersSheetState by remember { mutableStateOf(false) }

  if (showUsersSheetState) {
    ModalBottomSheet(
      sheetState = sheetState,
      onDismissRequest = { showUsersSheetState = false }
    ) {
      Users(
        users = state.users,
        onUserClicked = { onEvent(SuborderEvent.UserClicked(it)) },
        modifier = Modifier
          .height(sheetMaxHeight.dp)
      )
    }
  }

  Scaffold(
    floatingActionButton = {
      AddFab {
        scope.launch {
          showUsersSheetState = true
        }
      }
    },
  ) { paddingValues ->
    val scrollState = rememberScrollState()

    var confirmState by rememberConfirmState()
    if (confirmState != null) ConfirmDialog(confirmState!!)

    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = modifier
        .verticalScroll(scrollState)
        .padding(paddingValues)
        .padding(16.dp)
    ) {
      state.suborders.forEach {
        Suborder(
          suborder = it,
          onPayClicked = { onEvent(SuborderEvent.Pay(it)) },
          onEditClicked = { onEvent(SuborderEvent.EditSuborderClicked(it)) },
          onDeleteOrderClicked = {
            confirmState = ConfirmState(
              text = localString(R.string.are_you_sure_you_want_to_delete_order, it.user),
              onDismiss = { confirmState = null },
              onConfirm = { onEvent(SuborderEvent.Delete(it)); confirmState = null }
            )
          }
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSuborder(
  @PreviewParameter(SuborderPreviewParameter::class) suborder: Suborder
) {
  Preview {
    Suborder(
      suborder = suborder,
      onPayClicked = {},
      onEditClicked = {},
      onDeleteOrderClicked = {}
    )
  }
}

@Composable
private fun Suborder(
  suborder: Suborder,
  onPayClicked: () -> Unit,
  onEditClicked: () -> Unit,
  onDeleteOrderClicked: () -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }
  var showMenu by remember { mutableStateOf(false) }

  TlbCard(
    title = {
      if (showMenu) {
        DropdownMenu(
          expanded = showMenu,
          onDismissRequest = { showMenu = false }
        ) {
          DropdownMenuItem(
            text = { Text(stringResource(R.string.delete))},
            onClick = { onDeleteOrderClicked(); showMenu = false }
          )
        }
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable(onClick = { expanded = !expanded })
          .combinedClickable(
            onClick = { expanded = !expanded },
            onLongClick = { showMenu = true }
          ).padding(start = 8.dp)
      ) {
        Column(
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          TlbCardTitle(suborder.user)
          Text(
            text = suborder.total.format(),
            style = AppTheme.type.titleSmall.onSurfaceVariant,
          )
        }

        HorizontalSpacer(1F)

        IconButton(onClick = onPayClicked) {
          TlbIcon(
            painter = painterResource(R.drawable.ic_payer),
            contentDescription = stringResource(R.string.pay),
            tint = AppTheme.colors.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }

        IconButton(onClick = onEditClicked) {
          TlbIcon(
            painter = painterResource(R.drawable.edit),
            contentDescription = stringResource(R.string.edit_order),
            tint = AppTheme.colors.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  ) {
    AnimatedVisibility(
      visible = expanded,
      enter = slideInVertically(),
      exit = slideOutVertically()
    ) {
      Column {
        suborder.items.forEach { orderItem ->
          OrderItem(orderItem)
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewOrderItem() {
  OrderItem(
    OrderItem(
      id = 0,
      menuItemId = 0,
      name = "Test",
      quantity = 5,
      total = 50.money,
      note = "",
    )
  )
}

@Composable
private fun OrderItem(item: OrderItem) {
  Row(
    modifier = Modifier
      .padding(vertical = 12.dp, horizontal = 8.dp)
  ) {
    Text(
      text = item.quantity.toString(),
      style = AppTheme.type.bodyMedium.onSurfaceVariant,
      modifier = Modifier
        .width(32.dp)
    )

    Spacer(Modifier.width(16.dp))

    Text(
      text = item.name,
      style = AppTheme.type.bodyMedium.onSurfaceVariant
    )

    Spacer(Modifier.weight(1F))

    Text(
      text = item.total.format(),
      style = AppTheme.type.bodyMedium.onSurfaceVariant
    )
  }
}

@Composable
private fun Users(
  users: List<UserEntity>,
  onUserClicked: (UserEntity) -> Unit,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier,
  ) {
    items(users) { user ->
      User(
        user = user,
        onClick = onUserClicked,
      )
      Divider()
    }
  }
}

@Composable
private fun User(
  user: UserEntity,
  onClick: (UserEntity) -> Unit,
) {

  Row(
    modifier = Modifier
      .clickable {
        onClick(user)
      }
      .padding(16.dp)
      .fillMaxWidth()
  ) {
    Text(
      text = user.id.toString(),
      color = AppTheme.colors.onSurface,
      fontSize = 14.sp,
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = user.name,
      color = AppTheme.colors.onSurface,
      fontSize = 14.sp,
    )
  }
}

