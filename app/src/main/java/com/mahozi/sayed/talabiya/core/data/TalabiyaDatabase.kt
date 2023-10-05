package com.mahozi.sayed.talabiya.core.data

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import com.mahozi.sayed.talabiya.order.store.OrderDao
import com.mahozi.sayed.talabiya.order.store.OrderEntity
import com.mahozi.sayed.talabiya.order.store.OrderItemEntity
import com.mahozi.sayed.talabiya.order.store.SubOrderEntity
import com.mahozi.sayed.talabiya.person.store.PersonDao
import com.mahozi.sayed.talabiya.person.store.PersonEntity
import com.mahozi.sayed.talabiya.resturant.store.MenuItemEntity
import com.mahozi.sayed.talabiya.resturant.store.RestaurantDao
import com.mahozi.sayed.talabiya.resturant.store.RestaurantEntity
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Database(
  entities = [
    OrderEntity::class,
    OrderItemEntity::class,
    PersonEntity::class,
    SubOrderEntity::class,
    MenuItemEntity::class,
    RestaurantEntity::class
  ],
  version = 2
)

@TypeConverters(InstantConverter::class)
abstract class TalabiyaDatabase : RoomDatabase() {

  abstract fun orderDao(): OrderDao
  abstract fun personDao(): PersonDao
  abstract fun restaurantDao(): RestaurantDao

  companion object {

    //Migrate string date and time columns to single epoch data time column
    val MIGRATION_1_2 = Migration(1, 2) { db ->
      val tableName = "OrderEntity"
      val datetimeColumnName = "datetime"
      val dateColumnName = "date"
      val timeColumnName = "time"

      try {
        db.execSQL("ALTER TABLE $tableName ADD COLUMN $datetimeColumnName INTEGER")
      } catch (e: SQLiteException) {

        //Todo find a better way to check if column exists
      }

      val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd, E HH:mm", Locale.ENGLISH)

      val query = "SELECT id, date, time FROM $tableName"
      val cursor = db.query(query)

      val idIndex = cursor.getColumnIndex("id")
      val dateIndex = cursor.getColumnIndex(dateColumnName)
      val timeIndex = cursor.getColumnIndex(timeColumnName)

      val ids = mutableListOf<Long>()
      val instants = mutableListOf<Long>()

      while (cursor.moveToNext()) {
        val id = cursor.getLong(idIndex)
        ids.add(id)

        val date = cursor.getString(dateIndex)
        val time = cursor.getString(timeIndex)

        val dateTimeString = "$date $time"

        val instant = LocalDateTime.parse(dateTimeString, formatter)
          .atZone(ZoneId.of("Asia/Riyadh"))
          .toInstant()
          .toEpochMilli()

        instants.add(instant)
      }

      db.beginTransaction()
      try {
        instants.forEachIndexed { index, datetime ->
          val updateQuery = "update $tableName set $datetimeColumnName = $datetime where id = ${ids[index]}"
          db.execSQL(updateQuery)
        }
        db.setTransactionSuccessful()
      } finally {
        db.endTransaction()
      }

// TODO drop these columns when usages of date and time are removed
//      db.execSQL("ALTER TABLE $tableName DROP COLUMN $dateColumnName")
//      db.execSQL("ALTER TABLE $tableName DROP COLUMN $timeColumnName")
    }

    @Volatile
    private var INSTANCE: TalabiyaDatabase? = null

    @JvmStatic
    fun getDatabase(context: Context): TalabiyaDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          TalabiyaDatabase::class.java,
          "talabiya_database"
        )
          .allowMainThreadQueries()
          .addMigrations(MIGRATION_1_2)
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
