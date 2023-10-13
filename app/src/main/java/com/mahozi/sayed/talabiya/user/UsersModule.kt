package com.mahozi.sayed.talabiya.user

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.Database
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import restaurant.MenuItemQueries
import restaurant.RestaurantQueries
import user.UserQueries

@Module
@ContributesTo(AppScope::class)
object UsersModule {

  @Provides fun provideUserQueries(database: Database): UserQueries = database.userQueries
}