package com.mahozi.sayed.talabiya.payment

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

enum class PaymentStatus {
  Completed,
  Cancel;
}

val PaymentStatus.title: Int get() = when(this) {
  PaymentStatus.Completed -> R.string.completed
  PaymentStatus.Cancel -> R.string.canceled
}

val PaymentStatus.background: Color @Composable get() = when(this) {
  PaymentStatus.Completed -> AppTheme.colors.green.copy(alpha = 0.1f)
  PaymentStatus.Cancel -> AppTheme.colors.red.copy(alpha = 0.1f)
}