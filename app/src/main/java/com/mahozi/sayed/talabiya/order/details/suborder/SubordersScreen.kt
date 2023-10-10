package com.mahozi.sayed.talabiya.order.details.suborder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.core.Preview
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme


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

@Composable fun SubordersScreen(
  suborders: List<Suborder>,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier
      .verticalScroll(scrollState)
  ) {
    suborders.forEach {
      Suborder(it)
    }
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
        Divider(color = AppTheme.colors.lightBorder)
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
      .padding(vertical = 8.dp)
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
      .padding(vertical = 8.dp)
      .background(color = AppTheme.colors.backgroundSecondary)
  ) {
    Text(
      text = total.format(),
      style = AppTheme.types.title
    )
  }
}
