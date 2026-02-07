package com.mahozi.sayed.talabiya.payment

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

enum class PaymentStatus {
  Completed,
  Canceled;
}

val PaymentStatus.key: String get() = when(this) {
  PaymentStatus.Completed -> "Completed"
  PaymentStatus.Canceled -> "Canceled"
}

val PaymentStatus.title: Int get() = when(this) {
  PaymentStatus.Completed -> R.string.completed
  PaymentStatus.Canceled -> R.string.canceled
}

val PaymentStatus.background: Color @Composable get() = when(this) {
  PaymentStatus.Completed -> AppTheme.colors.green.copy(alpha = 0.1f)
  PaymentStatus.Canceled -> AppTheme.colors.red.copy(alpha = 0.1f)
}