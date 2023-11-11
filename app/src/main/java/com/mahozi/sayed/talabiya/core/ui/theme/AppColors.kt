package com.mahozi.sayed.talabiya.core.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

data class AppColors(
  val material: Colors,
  val primaryText: Color,
  val secondaryText: Color,
  val lightBackground: Color,
  val mediumBackground: Color,
  val darkBackground: Color,
)

val lightColors = AppColors(
  material = lightColors().copy(
    primary = Color(0xFFA30505),
    primaryVariant = Color(0xFF680606),
    secondary = Color(0xFF680606),
    secondaryVariant = Color(0xFF680606),
  ),
  primaryText = Color.Black,
  secondaryText = Color(0xFF959595),
  lightBackground = Color(0xFFF7F7F7),
  mediumBackground = Color(0xFFE4E4E4),
  darkBackground = Color(0xFFA0A0A0),
)

val darkColors = AppColors(
  material = darkColors().copy(
    primary = Color(0xFFA30505),
    primaryVariant = Color(0xFF680606),
    secondary = Color(0xFF680606),
    secondaryVariant = Color(0xFF680606),
  ),
  primaryText = Color.Black,
  secondaryText = Color(0xFF959595),
  lightBackground = Color.Black,
  mediumBackground = Color(0xFFE4E4E4),
  darkBackground = Color(0xFFA0A0A0),)