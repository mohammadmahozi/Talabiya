package com.mahozi.sayed.talabiya.payment

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mahozi.sayed.talabiya.R
import com.mahozi.sayed.talabiya.core.ui.theme.AppTheme

enum class PaymentDirection {
  Credit,
  Debit;

  companion object {
    fun from(key: String): PaymentDirection = when (key) {
      "Credit" -> Credit
      "Debit" -> Debit
      else -> throw IllegalArgumentException("Invalid key: $key")
    }
  }
}

val PaymentDirection.key: String
  get() = when (this) {
    PaymentDirection.Credit -> "Credit"
    PaymentDirection.Debit -> "Debit"
  }

val PaymentDirection.title: Int
  get() = when (this) {
    PaymentDirection.Credit -> R.string.credit
    PaymentDirection.Debit -> R.string.debit
  }

val PaymentDirection.background: Color
  @Composable get() = when (this) {
    PaymentDirection.Credit -> AppTheme.colors.green.copy(alpha = 0.1f)
    PaymentDirection.Debit -> AppTheme.colors.red.copy(alpha = 0.1f)
  }