package com.mahozi.sayed.talabiya.core.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

data class AppColors(
  val material: Colors,
  val primaryText: Color,
  val secondaryText: Color,
  val backgroundSecondary: Color,
  val lightBorder: Color
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
  backgroundSecondary = Color(0xFFF1F1F1),
  lightBorder = Color(0xFFE4E4E4),
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
  backgroundSecondary = Color.Black,
  lightBorder = Color(0xFFE4E4E4),
)