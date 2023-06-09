package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R


@Preview
@Composable
fun PreviewIconText() {
  IconText(text = "Test", painter = painterResource(R.drawable.ic_date), contentDescription = null)
}

@Composable fun IconText(
  text: String,
  painter: Painter,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  iconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      painter = painter,
      contentDescription = contentDescription,
      tint = iconTint,
      modifier = Modifier
          .size(16.dp)
    )

    Spacer(modifier = Modifier.width(4.dp))

    Text(
      text = text,
    )
  }
}