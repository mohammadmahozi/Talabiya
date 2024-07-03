package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun TalabiyaIconButton(
  painter: Painter,
  contentDescription: String?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  tint: Color = LocalContentColor.current
) {
  IconButton(
    onClick = onClick,
    modifier = modifier,
  ) {
    Icon(
      painter = painter,
      contentDescription = contentDescription,
      tint = tint
    )
  }
}

@Composable
fun TalabiyaIconButton(
  imageVector: ImageVector,
  contentDescription: String?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  tint: Color = LocalContentColor.current
) {
  IconButton(
    onClick = onClick,
    modifier = modifier,
  ) {
    Icon(
      imageVector = imageVector,
      contentDescription = contentDescription,
      tint = tint
    )
  }
}