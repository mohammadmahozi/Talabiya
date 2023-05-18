package com.mahozi.sayed.talabiya.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mahozi.sayed.talabiya.order.store.OrderDao
import com.mahozi.sayed.talabiya.order.store.OrderEntity
import com.mahozi.sayed.talabiya.order.store.OrderItemEntity
import com.mahozi.sayed.talabiya.order.store.SubOrderEntity
import com.mahozi.sayed.talabiya.person.store.PersonDao
import com.mahozi.sayed.talabiya.person.store.PersonEntity
import com.mahozi.sayed.talabiya.resturant.store.MenuItemEntity
import com.mahozi.sayed.talabiya.resturant.store.RestaurantDao
import com.mahozi.sayed.talabiya.resturant.store.RestaurantEntity

@Database(
  entities = [
    OrderEntity::class,
    OrderItemEntity::class,
    PersonEntity::class,
    SubOrderEntity::class,
    MenuItemEntity::class,
    RestaurantEntity::class
  ],
  version = 1
)
abstract class TalabiyaDatabase : RoomDatabase() {

  abstract fun orderDao(): OrderDao
  abstract fun personDao(): PersonDao
  abstract fun restaurantDao(): RestaurantDao

  companion object {
    @Volatile
    private var INSTANCE: TalabiyaDatabase? = null

    @JvmStatic
    fun getDatabase(context: Context): TalabiyaDatabase {
      if (INSTANCE == null) {
        synchronized(TalabiyaDatabase::class.java) {
          if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
              context.applicationContext,
              TalabiyaDatabase::class.java,
              "talabiya_database"
            )
              .createFromAsset("talabiya_database").allowMainThreadQueries().build()
          }
        }
      }
      return INSTANCE!!
    }
  }
}