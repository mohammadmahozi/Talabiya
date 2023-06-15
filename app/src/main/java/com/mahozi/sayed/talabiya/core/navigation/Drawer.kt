package com.mahozi.sayed.talabiya.core.navigation

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.string
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
private fun PreviewDrawer() {
  AppTheme {
    Column(
      modifier = Modifier
        .fillMaxHeight()
    ) {
      Drawer(
        onOrdersClicked = {},
        onRestaurantsClicked = {}
      )
    }
  }
}
@SuppressLint("ComposeModifierMissing")
@Composable fun ColumnScope.Drawer(
  onOrdersClicked: () -> Unit,
  onRestaurantsClicked: () -> Unit,
) {
    Column(
      modifier = Modifier
        .background(AppTheme.colors.material.primary)
        .fillMaxWidth()
        .padding(16.dp)
        .height(160.dp),
    ) {
      Icon(painter = painterResource(R.drawable.ic_payer), contentDescription = null)

      Spacer(modifier = Modifier.height(8.dp))

      Text(text = string(R.string.app_name), color = AppTheme.colors.material.onPrimary)
    }

    Column(
      modifier = Modifier
        .padding(vertical = 16.dp)
    ) {

      DrawerItem(
        title = R.string.orders,
        onClick = onOrdersClicked
      )

      DrawerItem(
        title = R.string.restaurants,
        onClick = onRestaurantsClicked
      )
    }
  }

@Composable
private fun DrawerItem(
  @StringRes title: Int,
  onClick: () -> Unit
) {
  Text(
    text = string(title),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }

      .padding(16.dp)
  )
}
