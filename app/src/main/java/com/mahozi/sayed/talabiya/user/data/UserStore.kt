package com.mahozi.sayed.talabiya.user.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import user.UserEntity
import user.UserQueries
import javax.inject.Inject

class UserStore @Inject constructor(
  private val userQueries: UserQueries,
  private val dispatcher: CoroutineDispatcher,
  ) {

  val users: Flow<List<UserEntity>> = userQueries
    .selectAll()
    .asFlow()
    .mapToList(dispatcher)

  suspend fun create(name: String) {
    withContext(dispatcher) {
      userQueries.insert(name)
    }
  }

  suspend fun delete(id: Long) {
    withContext(dispatcher) {
      delete(id)
    }
  }
}