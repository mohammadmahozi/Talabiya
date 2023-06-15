package com.mahozi.sayed.talabiya.resturant.store

import android.app.Application
import androidx.lifecycle.LiveData
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase.Companion.getDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import restaurant.RestaurantQueries
import javax.inject.Inject

class RestaurantStore @Inject constructor(
  private val restaurantQueries: RestaurantQueries,
  private val dispatcher: CoroutineDispatcher,
) {
  private var mRestaurantDao: RestaurantDao? = null

  val restaurants: Flow<List<restaurant.RestaurantEntity>> = restaurantQueries
    .selectAll()
    .asFlow()
    .mapToList(dispatcher)

  suspend fun createRestaurant(name: String) {
    withContext(dispatcher) {
      restaurantQueries.insert(name)
    }
  }

  fun init(application: Application?) {
    val talabiyaDatabase = getDatabase(application!!)
    mRestaurantDao = talabiyaDatabase.restaurantDao()
  }

  fun insertRestaurant(restaurantEntity: RestaurantEntity?) {
    mRestaurantDao!!.insert(restaurantEntity)
  }

  fun selectAllRestaurants(): LiveData<List<RestaurantEntity>> {
    return mRestaurantDao!!.selectAllRestaurants()
  }

  fun insertFood(menuItemEntity: MenuItemEntity?) {
    mRestaurantDao!!.insert(menuItemEntity)
  }

  fun selectAllMenuItems(restaurantName: String?): LiveData<List<MenuItemEntity>> {
    return mRestaurantDao!!.selectAllMenuItems(restaurantName)
  }

  fun selectRestaurantsNames(): List<String> {
    return mRestaurantDao!!.selectRestaurantsNames()
  }

  fun deleteRestaurant(restaurantEntity: RestaurantEntity?) {
    mRestaurantDao!!.deleteRestaurant(restaurantEntity)
  }

  fun deleteMenuItem(menuItemEntity: MenuItemEntity?) {
    mRestaurantDao!!.deleteMenuItem(menuItemEntity)
  }

  fun updateRestaurant(restaurantEntity: RestaurantEntity?) {
    mRestaurantDao!!.updateRestaurant(restaurantEntity)
  }

  fun updateMenuItem(menuItemEntity: MenuItemEntity?) {
    mRestaurantDao!!.updateMenuItem(menuItemEntity)
  }

  companion object {
    @Volatile
    private var mRestaurantRepository: RestaurantStore? = null
    @JvmStatic
    val instance: RestaurantStore?
      get() {
        return null
      }
  }
}