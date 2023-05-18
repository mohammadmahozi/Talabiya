package com.mahozi.sayed.talabiya.core.data;


import android.content.Context;

import com.mahozi.sayed.talabiya.person.store.PersonDao;
import com.mahozi.sayed.talabiya.person.store.PersonEntity;
import com.mahozi.sayed.talabiya.resturant.store.MenuItemEntity;
import com.mahozi.sayed.talabiya.resturant.store.RestaurantDao;
import com.mahozi.sayed.talabiya.resturant.store.RestaurantEntity;
import com.mahozi.sayed.talabiya.order.store.OrderDao;
import com.mahozi.sayed.talabiya.order.store.OrderEntity;
import com.mahozi.sayed.talabiya.order.store.OrderItemEntity;
import com.mahozi.sayed.talabiya.order.store.SubOrderEntity;

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
