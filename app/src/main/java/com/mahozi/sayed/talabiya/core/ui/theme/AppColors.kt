package com.mahozi.sayed.talabiya.core.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

data class AppColors(
    val material: Colors,
    val primary: Color,
    val primaryDark: Color,
    val accent: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val rowBackground: Color,
    val lightBorder: Color
)

val lightColors = AppColors(
    material = lightColors(),
    primary = Color(0xFFA30505),
    primaryDark = Color(0xFF680606),
    accent = Color(0xFFD81B60),
    primaryText = Color.Black,
    secondaryText = Color(0xFF959595),
    rowBackground = Color.White,
    lightBorder = Color(0xFFE4E4E4),
)

val darkColors = AppColors(
    material = darkColors(),
    primary = Color(0xFFA30505),
    primaryDark = Color(0xFF680606),
    accent = Color(0xFFD81B60),
    primaryText = Color.Black,
    secondaryText = Color(0xFF959595),
    rowBackground = Color.Black,
    lightBorder = Color(0xFFE4E4E4),
)