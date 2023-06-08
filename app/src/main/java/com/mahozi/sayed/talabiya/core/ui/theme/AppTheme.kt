package com.mahozi.sayed.talabiya.core.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

@SuppressLint("ComposeCompositionLocalUsage")
private val LocalColors = staticCompositionLocalOf { lightColors }


@Composable fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) darkColors else lightColors

    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            content = content,
            colors = colors.material
        )
    }
}

object AppTheme {
    val colors @Composable get() = LocalColors.current
}
