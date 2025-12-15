package com.mahozi.sayed.talabiya.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.mahozi.sayed.talabiya.R

class TlbTextScope internal constructor() {
  @Composable
  fun TlbTextIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
  ) {
    Icon(
      painter = painter,
      contentDescription = contentDescription,
      modifier = modifier
        .size(12.dp)
    )
  }
}

@Composable
fun TlbText(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = Color.Unspecified,
  fontSize: TextUnit = TextUnit.Unspecified,
  fontStyle: FontStyle? = null,
  fontWeight: FontWeight? = null,
  fontFamily: FontFamily? = null,
  letterSpacing: TextUnit = TextUnit.Unspecified,
  textDecoration: TextDecoration? = null,
  textAlign: TextAlign? = null,
  lineHeight: TextUnit = TextUnit.Unspecified,
  overflow: TextOverflow = TextOverflow.Clip,
  softWrap: Boolean = true,
  maxLines: Int = Int.MAX_VALUE,
  minLines: Int = 1,
  onTextLayout: ((TextLayoutResult) -> Unit)? = null,
  style: TextStyle = LocalTextStyle.current,
  overLine: (@Composable TlbTextScope.() -> Unit)? = null,
  leadingIcon: (@Composable TlbTextScope.() -> Unit)? = null,
  trailingIcon: (@Composable TlbTextScope.() -> Unit)? = null,
  prefix: (@Composable TlbTextScope.() -> Unit)? = null,
  suffix: (@Composable TlbTextScope.() -> Unit)? = null,
) {
  Column {
    val scope = remember { TlbTextScope() }
    if (overLine != null) {
      scope.overLine()
      VerticalSpacer(4.dp)
    }
    Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (leadingIcon != null) {
        scope.leadingIcon()
        HorizontalSpacer(4.dp)
      }
      if (prefix != null) {
        scope.prefix()
        HorizontalSpacer(4.dp)
      }
      Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style
      )
      if (suffix != null) {
        HorizontalSpacer(4.dp)
        scope.suffix()
      }
      if (trailingIcon != null) {
        HorizontalSpacer(4.dp)
        scope.trailingIcon()
      }
    }
  }
}

@Preview
@Composable
private fun PreviewIconText() {
  IconText(text = "Test", painter = painterResource(R.drawable.ic_date), contentDescription = null)
}

@Composable
fun IconText(
  text: String,
  painter: Painter,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  iconTint: Color = LocalContentColor.current
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