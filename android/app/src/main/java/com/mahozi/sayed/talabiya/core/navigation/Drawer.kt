package com.mahozi.sayed.talabiya.core.navigation

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        onRestaurantsClicked = {},
        onUsersClicked = {},
        creatingBackup = true,
        onCreateBackupClicked = {}
      )
    }
  }
}

@SuppressLint("ComposeModifierMissing")
@Composable
fun ColumnScope.Drawer(
  onOrdersClicked: () -> Unit,
  onRestaurantsClicked: () -> Unit,
  onUsersClicked: () -> Unit,
  creatingBackup: Boolean,
  onCreateBackupClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .background(AppTheme.colors.material.primary)
      .fillMaxWidth()
      .padding(16.dp)
      .height(160.dp),
  ) {
    Text(text = string(R.string.app_name), color = AppTheme.colors.material.onPrimary)
  }

  Column(
    modifier = Modifier
      .padding(16.dp)
  ) {
    DrawerItem(
      title = R.string.orders,
      onClick = onOrdersClicked,
      modifier = Modifier.fillMaxWidth(),
    )

    DrawerItem(
      title = R.string.restaurants,
      onClick = onRestaurantsClicked,
      modifier = Modifier.fillMaxWidth(),
    )

    DrawerItem(
      title = R.string.users,
      onClick = onUsersClicked,
      modifier = Modifier.fillMaxWidth(),
    )

    HorizontalDivider()

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      DrawerItem(
        title = R.string.create_backup,
        onClick = onCreateBackupClicked,
        enabled = !creatingBackup
      )

      if (creatingBackup) {
        CircularProgressIndicator(
          strokeWidth = 2.dp,
          modifier = Modifier.size(16.dp)
        )
      }
    }
  }
}

@Composable
private fun DrawerItem(
  @StringRes title: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true
) {
  Text(
    text = string(title),
    modifier = modifier
      .clickable(
        enabled = enabled,
        onClick = onClick
      ).padding(vertical = 16.dp)
  )
}
