package com.mahozi.sayed.talabiya.core.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalColors = staticCompositionLocalOf { lightColors }

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalShapes = staticCompositionLocalOf { defaultTlbShapes }

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalTypes = staticCompositionLocalOf { AppTypes() }

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalTypography = staticCompositionLocalOf {
    Typography()
}

@Composable fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) darkColors else lightColors
    val shapes = defaultTlbShapes

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalShapes provides shapes,
        LocalTypes provides AppTypes()
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
            typography = LocalTypography.current
        )
    }
}

object AppTheme {
    val colors @Composable get() = LocalColors.current

    val shapes @Composable get() = LocalShapes.current

    val types @Composable get() = LocalTypes.current

    val typography @Composable get() = LocalTypography.current
}
