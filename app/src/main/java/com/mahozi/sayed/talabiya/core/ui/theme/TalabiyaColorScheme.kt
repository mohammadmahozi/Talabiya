package com.mahozi.sayed.talabiya.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private object AppColor {
  val Primary = Color(0xFFA30505)
  val White = Color(red = 255, green = 251, blue = 251, alpha = 255)
  val Black = Color(red = 31, green = 27, blue = 27, alpha = 255)
  val DarkRed = Color(0xFF680606)
  val Red = Color(0xFFE30000)
  val LightRed = Color(0xFFD0CBCB)
  val LightGray = Color(red = 236, green = 224, blue = 224, alpha = 255)
}

data class TalabiyaColorScheme(
  val material: ColorScheme,
  val primaryText: Color,
  val secondaryText: Color,
  val lightBackground: Color,
  val mediumBackground: Color,
  val darkBackground: Color,
)

val lightColors = TalabiyaColorScheme(
  material = lightColorScheme(
    primary = AppColor.Primary,
    onPrimary = AppColor.White,
    primaryContainer = AppColor.LightRed,
    onPrimaryContainer = AppColor.White,
    background = AppColor.White,
    onBackground = AppColor.Black,
    surface = AppColor.White,
    surfaceVariant = AppColor.LightGray,
    error = AppColor.Red,

  ),
  primaryText = Color.Black,
  secondaryText = Color(0xFF959595),
  lightBackground = Color(0xFFF7F7F7),
  mediumBackground = Color(0xFFE4E4E4),
  darkBackground = Color(0xFFA0A0A0),
)

val darkColors = TalabiyaColorScheme(
  material = darkColorScheme(
    primary = Color(0xFFA30505),
    secondary = Color(0xFF680606),
  ),
  primaryText = Color.Black,
  secondaryText = Color(0xFF959595),
  lightBackground = Color.Black,
  mediumBackground = Color(0xFFE4E4E4),
  darkBackground = Color(0xFFA0A0A0),)