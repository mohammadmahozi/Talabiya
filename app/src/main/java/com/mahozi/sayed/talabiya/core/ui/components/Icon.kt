package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme


private val LocalIconSize = staticCompositionLocalOf {
  DpSize(Dp.Unspecified, Dp.Unspecified)
}

@Preview
@Composable
private fun PreviewTlbIcon() {
  AppTheme {
    TlbIcon(
      painter = painterResource(R.drawable.ic_payer),
      contentDescription = null,
    )
  }
}
@Composable
fun TlbIcon(
  painter: Painter,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  tint: Color = LocalContentColor.current,
) {
  Icon(
    painter = painter,
    contentDescription = contentDescription,
    modifier = modifier.size(LocalIconSize.current),
    tint = tint,
  )
}

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