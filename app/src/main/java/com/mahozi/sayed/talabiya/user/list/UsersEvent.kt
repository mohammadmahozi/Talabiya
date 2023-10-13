package com.mahozi.sayed.talabiya.user.list

import user.UserEntity

sealed interface UsersEvent {
  object CreateUserClicked: UsersEvent
  data class UserClicked(val user: UserEntity): UsersEvent
  data class DeleteUserClicked(val user: UserEntity): UsersEvent
}