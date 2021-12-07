package com.mahozi.sayed.talabiya.Storage;


import android.content.Context;

import com.mahozi.sayed.talabiya.Domain.Order.OrderItem;
import com.mahozi.sayed.talabiya.Domain.Person.Person;
import com.mahozi.sayed.talabiya.Storage.Person.PersonDao;
import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;
import com.mahozi.sayed.talabiya.Storage.Restaurant.MenuItemEntity;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantDao;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantEntity;
import com.mahozi.sayed.talabiya.Storage.order.OrderDao;
import com.mahozi.sayed.talabiya.Storage.order.OrderEntity;
import com.mahozi.sayed.talabiya.Storage.order.OrderItemEntity;
import com.mahozi.sayed.talabiya.Storage.order.SubOrderEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {OrderEntity.class, OrderItemEntity.class, PersonEntity.class,
        SubOrderEntity.class, MenuItemEntity.class, RestaurantEntity.class}, version = 1)

public abstract class TalabiyaDatabase extends RoomDatabase {

        public abstract OrderDao orderDao();
        public abstract PersonDao personDao();
        public abstract RestaurantDao restaurantDao();




        private static volatile TalabiyaDatabase INSTANCE;

        public static TalabiyaDatabase getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (TalabiyaDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TalabiyaDatabase.class, "talabiya_database")
                                .createFromAsset("talabiya_database").allowMainThreadQueries().build();
                    }
                }
            }

            return INSTANCE;
        }

}
