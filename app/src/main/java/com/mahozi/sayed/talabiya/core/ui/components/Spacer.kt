package com.mahozi.sayed.talabiya.core.ui.components


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@SuppressLint("ComposeModifierMissing")
@Composable
fun RowScope.HorizontalSpacer(
  width: Dp
) {
  Spacer(modifier = Modifier.width(width))
}

@SuppressLint("ComposeModifierMissing")
@Composable
fun RowScope.HorizontalSpacer(
  weight: Float,
  fill: Boolean = true
) {
  Spacer(modifier = Modifier.weight(weight, fill))
}

@SuppressLint("ComposeModifierMissing")
@Composable
fun ColumnScope.VerticalSpacer(
  height: Dp
) {
  Spacer(modifier = Modifier.height(height))
}

@SuppressLint("ComposeModifierMissing")
@Composable
fun ColumnScope.VerticalSpacer(
  weight: Float,
  fill: Boolean = true
) {
  Spacer(modifier = Modifier.weight(weight, fill))
}

