package com.mahozi.sayed.talabiya.order.details.suborder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.core.Preview
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.ui.components.AddFab
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme
import com.mahozi.sayed.talabiya.order.details.tabs.OrderDetailsEvent.SuborderEvent
import com.mahozi.sayed.talabiya.order.details.tabs.SubordersState
import kotlinx.coroutines.launch
import user.UserEntity


private class SuborderPreviewParameter: PreviewParameterProvider<Suborder> {
  private val suborder = Suborder(
    0,
    "Customer",
    listOf(
      OrderItem(0, 1, "Item 1", 10.money),
      OrderItem(1, 575, "Item 2", 100.money),
      OrderItem(2, 12, "Item 3", 4000.money),
    ),
    6000.money,
    expanded = false
  )
  override val values: Sequence<Suborder>
    get() = sequenceOf(
      suborder,
      suborder.copy(expanded = true)
    )
}
@Preview
@Composable private fun PreviewSubordersScreen() {
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
@Composable fun SubordersScreen(
  state: SubordersState,
  onEvent: (SuborderEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  val scope = rememberCoroutineScope()
  val sheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )

  Scaffold(
    floatingActionButton = {
      AddFab {
        scope.launch {
          sheetState.show()
        }
      }
    },
  ) { paddingValues ->
    val scrollState = rememberScrollState()
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = modifier
        .verticalScroll(scrollState)
        .padding(paddingValues)

    ) {
      state.suborders.forEach {
        Suborder(it)
      }
    }
  }

  val sheetMaxHeight = LocalConfiguration.current.screenHeightDp * 0.5

  ModalBottomSheet(
    onDismissRequest = { scope.launch{ sheetState.hide() } }
  ) {
    Users(
      users = state.users,
      onUserClicked = { onEvent(SuborderEvent.UserClicked(it)) },
      modifier = Modifier
        .height(sheetMaxHeight.dp)
    )
  }

}

@Preview(showBackground = true)
@Composable private fun PreviewSuborder(
  @PreviewParameter(SuborderPreviewParameter::class) suborder: Suborder
) {
  Preview {
    Suborder(suborder)
  }
}

@Composable private fun Suborder(suborder: Suborder) {
  Column {
    Header(
      name = suborder.customer,
      onHeaderClicked = { },
      onEditClicked = { }
    )

    Divider(color = AppTheme.colors.material.primary)

    if (suborder.expanded) {
      suborder.items.forEach { orderItem ->
        OrderItem(orderItem)
        Divider(color = AppTheme.colors.mediumBackground)
      }
    }

    Footer(suborder.total)
  }
}

@Preview(showBackground = true)
@Composable private fun PreviewHeader() {
  Header(
    name = "Test",
    onHeaderClicked = {},
    onEditClicked = {}
  )
}

@Composable private fun Header(
  name: String,
  onHeaderClicked: () -> Unit,
  onEditClicked: () -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clickable(onClick = onHeaderClicked)
      .padding(horizontal = 8.dp)
  ) {
    Text(text = name)

    Spacer(Modifier.weight(1F))

    IconButton(onClick = onEditClicked) {
      Icon(
        imageVector = Icons.Default.Edit,
        contentDescription = stringResource(R.string.edit_order)
      )
    }
  }
}

@Preview(showBackground = true)
@Composable private fun PreviewOrderItem() {
  OrderItem(
    OrderItem(
      0,
      5,
      "Test",
      50.money
    )
  )
}

@Composable private fun OrderItem(item: OrderItem) {
  Row(
    modifier = Modifier
      .padding(8.dp)
  ) {
    Text(
      text = item.quantity.toString(),
      style = AppTheme.types.title,
      modifier = Modifier
        .width(32.dp)
    )

    Spacer(Modifier.width(16.dp))

    Text(
      text = item.name,
      style = AppTheme.types.title
    )

    Spacer(Modifier.weight(1F))

    Text(
      text = item.total.format(),
      style = AppTheme.types.title
    )
  }
}

@Preview(showBackground = true)
@Composable private fun PreviewFooter() {
  Preview {
    Footer(total = 10.money)
  }
}

@Composable private fun Footer(
  total: Money
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(color = AppTheme.colors.lightBackground)
      .padding(8.dp)
  ) {
    Text(
      text = total.format(),
      style = AppTheme.types.title
    )
  }
}

@Composable private fun Users(
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

@Composable private fun User(
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
      color = AppTheme.colors.primaryText,
      fontSize = 14.sp,
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = user.name,
      color = AppTheme.colors.primaryText,
      fontSize = 14.sp,
    )
  }
}

