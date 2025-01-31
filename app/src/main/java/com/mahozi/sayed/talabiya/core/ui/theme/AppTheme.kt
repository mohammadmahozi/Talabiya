package com.mahozi.sayed.talabiya.core.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalColors = staticCompositionLocalOf { lightColors }

@Immutable data class AppShapes(
    val small: CornerBasedShape = RoundedCornerShape(5.dp),
    val medium: CornerBasedShape = RoundedCornerShape(10.dp),
    val circle: CornerBasedShape = CircleShape,
)

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalShapes = staticCompositionLocalOf { AppShapes() }

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

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalShapes provides AppShapes(),
        LocalTypes provides AppTypes()
    ) {
        MaterialTheme(
            content = content,
            colorScheme = colors.material,
            shapes = Shapes(
                small = RoundedCornerShape(5.dp),
                medium = RoundedCornerShape(5.dp),
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
