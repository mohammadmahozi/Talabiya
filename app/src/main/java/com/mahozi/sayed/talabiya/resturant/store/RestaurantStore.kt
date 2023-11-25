package com.mahozi.sayed.talabiya.resturant.store

import android.app.Application
import androidx.lifecycle.LiveData
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mahozi.sayed.talabiya.core.Cent
import com.mahozi.sayed.talabiya.core.Money
import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase.Companion.getDatabase
import com.mahozi.sayed.talabiya.core.money
import com.mahozi.sayed.talabiya.resturant.menu.Category
import com.mahozi.sayed.talabiya.resturant.menu.MenuItem
import com.mahozi.sayed.talabiya.resturant.option.FoodOption
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import restaurant.MenuItemQueries
import restaurant.RestaurantQueries
import java.time.Instant
import javax.inject.Inject

class RestaurantStore @Inject constructor(
  private val restaurantQueries: RestaurantQueries,
  private val menuItemQueries: MenuItemQueries,
  private val dispatcher: CoroutineDispatcher,
) {
  private var mRestaurantDao: RestaurantDao? = null

  val restaurants: Flow<List<restaurant.RestaurantEntity>> = restaurantQueries
    .selectAll()
    .asFlow()
    .mapToList(dispatcher)

  fun menuItems(restaurantId: Long): Flow<List<MenuItem>> = menuItemQueries
    .menuItemsEntity(
      restaurantId = restaurantId,
      mapper = { id, name, price, _, category ->
        MenuItem(
          id = id,
          name = name,
          category = category,
          price = Cent(price).money
        )
      }
    ).asFlow()
    .mapToList(dispatcher)

  suspend fun createRestaurant(name: String) {
    withContext(dispatcher) {
      restaurantQueries.insert(name)
    }
  }

  suspend fun createMenuItem(
    restaurantId: Long,
    name: String,
    price: Money,
    category: String
  ) {
    withContext(dispatcher) {
      menuItemQueries.transaction {
        menuItemQueries.insert(restaurantId, name, category)
        val menuItemId = menuItemQueries.lastInsertRowId().executeAsOne()
        menuItemQueries.insertPrice(menuItemId, Instant.now(), price.toLong())
      }
    }
  }

  suspend fun getCategories(): List<String> {
    return withContext(dispatcher) {
      menuItemQueries.selectAllCategories().executeAsList()
    }
  }

  fun getOptions(restaurantId: Long): Flow<List<FoodOption>> = menuItemQueries.selectAllOptions(restaurantId)
    .asFlow()
    .map { query ->
      query.executeAsList()
        .groupBy { option -> option.id }
        .map { (id, options) ->
          FoodOption(
            id,
            options.first().name,
            options.map { FoodOption.Category(it.category, true)
            }
          )
        }
    }

  suspend fun createOption(
    restaurantId: Long,
    name: String,
    categories: List<String>,
  ) {
    withContext(dispatcher) {
      menuItemQueries.transaction {
        menuItemQueries.insertOption(restaurantId, name)
        val optionId = menuItemQueries.lastInsertRowId().executeAsOne()
        categories.forEach { category ->
          menuItemQueries.insertOptionCategory(optionId, category)
        }
      }
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

  fun deleteRestaurant(restaurantId: Long) {
    restaurantQueries.delete(restaurantId)
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