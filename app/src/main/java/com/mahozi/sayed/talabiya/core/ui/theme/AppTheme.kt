package com.mahozi.sayed.talabiya.core.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
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
            colors = colors.material,
            shapes = Shapes(
                small = RoundedCornerShape(5.dp),
                medium = RoundedCornerShape(5.dp),
            )
        )
    }
}

object AppTheme {
    val colors @Composable get() = LocalColors.current

    val shapes @Composable get() = LocalShapes.current

    val types @Composable get() = LocalTypes.current
}
