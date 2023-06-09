package com.mahozi.sayed.talabiya.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.mahozi.sayed.talabiya.core.datetime.AppDateTimeFormatter
import com.mahozi.sayed.talabiya.core.datetime.LocalDateTimeFormatter
import com.mahozi.sayed.talabiya.core.extensions.locale
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

@Composable fun Preview(
  content: @Composable () -> Unit,
) {
  require(LocalInspectionMode.current) { "This is for preview only" }
  AppTheme {
    val context = LocalContext.current
    CompositionLocalProvider(LocalDateTimeFormatter provides AppDateTimeFormatter(context.locale)) {
      content()
    }
  }
}