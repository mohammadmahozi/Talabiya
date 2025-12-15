package com.mahozi.sayed.talabiya.core.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp


data class TlbShapes(
  val extraSmall: CornerBasedShape,
  val small: CornerBasedShape,
  val medium: CornerBasedShape,
  val large: CornerBasedShape,
  val extraLarge: CornerBasedShape,
)

val defaultTlbShapes = TlbShapes(
  extraSmall = RoundedCornerShape(2.dp),
  small = RoundedCornerShape(5.dp),
  medium = RoundedCornerShape(10.dp),
  large = RoundedCornerShape(15.dp),
  extraLarge = RoundedCornerShape(20.dp),
)