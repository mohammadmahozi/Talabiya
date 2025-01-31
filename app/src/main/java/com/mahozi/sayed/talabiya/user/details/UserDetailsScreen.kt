package com.mahozi.sayed.talabiya.user.details

import com.mahozi.sayed.talabiya.core.navigation.Screen
import kotlinx.parcelize.Parcelize

@Parcelize data class UserDetailsScreen(
  val userId: Long,
): Screen

