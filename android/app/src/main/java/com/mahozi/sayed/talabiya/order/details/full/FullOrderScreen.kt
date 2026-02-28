package com.mahozi.sayed.talabiya.order.details.full

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FullOrderScreen(
  items: List<FullOrderItem>,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(4.dp),
    modifier = modifier
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    items(items) { item ->
      Text("${item.quantity}  ${item.name}")
    }
  }
}