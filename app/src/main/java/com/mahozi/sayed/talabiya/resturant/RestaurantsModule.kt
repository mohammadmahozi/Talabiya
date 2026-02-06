package com.mahozi.sayed.talabiya.resturant

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.TalabiyaDatabase
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import restaurant.MenuItemQueries
import restaurant.RestaurantQueries

@ContributesTo(AppScope::class)
interface RestaurantsModule {

  @Provides
  fun provideRestaurantQueries(database: TalabiyaDatabase): RestaurantQueries = database.restaurantQueries

  @Provides
  fun provideMenuItemQueries(database: TalabiyaDatabase): MenuItemQueries = database.menuItemQueries
}