package com.mahozi.sayed.talabiya.resturant

import com.mahozi.sayed.talabiya.core.di.AppScope
import com.mahozi.talabiya.Database
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import restaurant.MenuItemQueries
import restaurant.RestaurantQueries

@Module
@ContributesTo(AppScope::class)
object RestaurantsModule {

  @Provides fun provideRestaurantQueries(database: Database): RestaurantQueries = database.restaurantQueries

  @Provides fun provideMenuItemQueries(database: Database): MenuItemQueries = database.menuItemQueries
}