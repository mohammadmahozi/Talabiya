package com.mahozi.sayed.talabiya.core.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalColors = staticCompositionLocalOf { lightColors }

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalShapes = staticCompositionLocalOf { defaultTlbShapes }

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalTypes = staticCompositionLocalOf { TlbType() }

@Composable
fun AppTheme(
  darkTheme: Boolean = false,//isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colors = if (darkTheme) darkColors else lightColors
  val shapes = defaultTlbShapes
  val type = LocalTypes.current

  CompositionLocalProvider(
    LocalColors provides colors,
    LocalShapes provides shapes,
    LocalTypes provides TlbType()
  ) {
    MaterialTheme(
      content = content,
      colorScheme = colors.material,
      shapes = Shapes(
        extraSmall = shapes.extraSmall,
        small = shapes.small,
        medium = shapes.medium,
        large = shapes.large,
        extraLarge = shapes.extraLarge
      ),
      typography = Typography(
        displayLarge = type.displayLarge,
        displayMedium = type.displayMedium,
        displaySmall = type.displaySmall,
        headlineLarge = type.headlineLarge,
        headlineMedium = type.headlineMedium,
        headlineSmall = type.headlineSmall,
        titleLarge = type.titleLarge,
        titleMedium = type.titleMedium,
        titleSmall = type.titleSmall,
        bodyLarge = type.bodyLarge,
        bodyMedium = type.bodyMedium,
        bodySmall = type.bodySmall,
        labelLarge = type.labelLarge,
        labelMedium = type.labelMedium,
        labelSmall = type.labelSmall
      )
    )
  }
}

object AppTheme {
  val colors @Composable get() = LocalColors.current
  val shapes @Composable get() = LocalShapes.current
  val type @Composable get() = LocalTypes.current
}
