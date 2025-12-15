package com.mahozi.sayed.talabiya.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.room.util.copy

private val defaultType = Typography()

@Immutable
data class TlbType(
  /**
   * Used by time picker.
   */
  val displayLarge: TextStyle = defaultType.displayLarge.copy(
    fontSize = 57.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Not used by material components.
   */
  val displayMedium: TextStyle = defaultType.displayMedium.copy(
    fontSize = 45.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Not used by material components.
   */
  val displaySmall: TextStyle = defaultType.displaySmall.copy(
    fontSize = 36.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Used by date picker.
   */
  val headlineLarge: TextStyle = defaultType.headlineLarge.copy(
    fontSize = 32.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Used by large top app bar.
   */
  val headlineMedium: TextStyle = defaultType.headlineMedium.copy(
    fontSize = 28.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Used by medium top app bar.
   */
  val headlineSmall: TextStyle = defaultType.headlineSmall.copy(
    fontSize = 24.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Used by small and centered top app bars.
   */
  val titleLarge: TextStyle = defaultType.titleLarge.copy(
    fontSize = 20.sp,
    fontWeight = FontWeight.Medium
  ),
  /**
   * Used for cards main title with more emphasis than body large.
   */
  val titleMedium: TextStyle = defaultType.titleMedium.copy(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium
  ),
  /**
   * Used by tabs.
   */
  val titleSmall: TextStyle = defaultType.titleSmall.copy(
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium
  ),
  /**
   * The default text style. Used by text fields, menus, and date picker.
   * Typically used as a title for less emphasis than title medium or as a body text for
   * more emphasis than body medium.
   */
  val bodyLarge: TextStyle = defaultType.bodyLarge.copy(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Typically used for sub title and supporting text. Used by dialogs body.
   */
  val bodyMedium: TextStyle = defaultType.bodyMedium.copy(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Used by text fields.
   */
  val bodySmall: TextStyle = defaultType.bodySmall.copy(
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal
  ),
  /**
   * Used by button, chips, and date picker.
   */
  val labelLarge: TextStyle = defaultType.labelLarge.copy(
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium
  ),
  /**
   * Used by bottom navigation.
   */
  val labelMedium: TextStyle = defaultType.labelMedium.copy(
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium
  ),
  /**
   * Not used by material components.
   */
  val labelSmall: TextStyle = defaultType.labelSmall.copy(
    fontSize = 11.sp,
    fontWeight = FontWeight.Medium
  ),
  val title: TextStyle = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.1.sp
  ),
  val subtitle: TextStyle = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    letterSpacing = 0.1.sp,
  )
) {
  private val material: Typography = Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall
  )
}

val TextStyle.onSurfaceVariant: TextStyle
  @Composable get() =
    copy(color = AppTheme.colors.onSurfaceVariant)


