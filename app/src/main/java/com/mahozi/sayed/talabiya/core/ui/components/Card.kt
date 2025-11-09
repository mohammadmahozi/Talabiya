package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

class TlbCardScope internal constructor() {
  @Composable
  fun TlbCardTitle(
    text: String,
  ) {
    Text(
      text = text,
      style = AppTheme.type.titleSmall
    )
  }

  @Composable
  fun TlbCardTitle(
    text: Int,
  ) {
    TlbCardTitle(stringResource(text))
  }
}

@Composable
fun TlbCard(
  modifier: Modifier = Modifier,
  title: (@Composable TlbCardScope.() -> Unit)? = null,
  border: BorderStroke? = null,
  content: @Composable (ColumnScope.() -> Unit),
) {
  Card(
    modifier = modifier,
    border = border,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      val scope = remember { TlbCardScope() }
      if (title != null) scope.title()
      VerticalSpacer(8.dp)
      content()
    }
  }
}