package com.mahozi.sayed.talabiya.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private object Palette {
  val Primary = Color(0xFFA30505)
  val Neutral10 = Color(0xFF1F1B1B)
  val Neutral30 = Color(red = 73, green = 70, blue = 70, alpha = 255)
  val Neutral60 = Color(red = 148, green = 144, blue = 144, alpha = 255)
  val Neutral80 = Color(red = 202, green = 197, blue = 197, alpha = 255)
  val Neutral90 = Color(red = 230, green = 225, blue = 225, alpha = 255)
  val Neutral92 = Color(red = 236, green = 231, blue = 231, alpha = 255)
  val Neutral94 = Color(red = 241, green = 236, blue = 236, alpha = 255)
  val Neutral95 = Color(red = 244, green = 239, blue = 239, alpha = 255)
  val Neutral96 = Color(red = 247, green = 242, blue = 242, alpha = 255)
  val Neutral98 = Color(red = 253, green = 248, blue = 248, alpha = 255)
  val Neutral99 = Color(red = 255, green = 251, blue = 251, alpha = 255)
  val Neutral100 = Color(red = 255, green = 255, blue = 255)
  val Red30 = Color(0xFF680606)
  val Red50 = Color(0xFFE30000)
  val Red90 = Color(0xFFffd9d6)
  val Green50 = Color(0xFF008000)
  val Green90 = Color(0x19008000)
}

data class TalabiyaColorScheme(
  val material: ColorScheme,
  val green: Color,
  val greenContainer: Color,
  val red: Color,
  val redContainer: Color,
) {
  val primary get() = material.primary
  val onPrimary get() = material.onPrimary
  val primaryContainer get() = material.primaryContainer
  val onPrimaryContainer get() = material.onPrimaryContainer
  val background get() = material.background
  val onBackground get() = material.onBackground
  val surface get() = material.surface
  val onSurface get() = material.onSurface
  val surfaceContainer get() = material.surfaceContainer
  val surfaceContainerLow get() = material.surfaceContainerLow
  val surfaceContainerLowest get() = material.surfaceContainerLowest
  val surfaceContainerHigh get() = material.surfaceContainerHigh
  val surfaceContainerHighest get() = material.surfaceContainerHighest
  val onSurfaceVariant get() = material.onSurfaceVariant
  val error get() = material.error
  val outline get() = material.outline
  val outlineVariant get() = material.outlineVariant
}

val lightColors = TalabiyaColorScheme(
  material = lightColorScheme(
    primary = Palette.Primary,
    onPrimary = Palette.Neutral100,
    primaryContainer = Palette.Red90,
    onPrimaryContainer = Palette.Primary,
    background = Palette.Neutral100,
    onBackground = Palette.Neutral10,
    surface = Palette.Neutral100,
    onSurface = Palette.Neutral10,
    surfaceVariant = Palette.Neutral96,
    onSurfaceVariant = Palette.Neutral30,
    surfaceContainer = Palette.Neutral96,
    surfaceContainerLow = Palette.Neutral99,
    surfaceContainerLowest = Palette.Neutral100,
    surfaceContainerHigh = Palette.Neutral95,
    surfaceContainerHighest = Palette.Neutral92,
    error = Palette.Red50,
    errorContainer = Palette.Red90,
    outline = Palette.Neutral60,
    outlineVariant = Palette.Neutral80,
  ),
  green = Palette.Green50,
  greenContainer = Palette.Green90,
  red = Palette.Red30,
  redContainer = Palette.Red90,
)

val darkColors = TalabiyaColorScheme(
  material = darkColorScheme(
    primary = Color(0xFFA30505),
    secondary = Color(0xFF680606),
  ),
  green = Palette.Green50,
  greenContainer = Palette.Green90,
  red = Palette.Red30,
  redContainer = Palette.Red90,
)