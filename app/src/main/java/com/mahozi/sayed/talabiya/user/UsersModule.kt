package com.mahozi.sayed.talabiya.user

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.TalabiyaDatabase
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import user.UserQueries

@ContributesTo(AppScope::class)
interface UsersModule {

  @Provides
  fun provideUserQueries(database: TalabiyaDatabase): UserQueries = database.userQueries
}