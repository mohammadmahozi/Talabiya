package com.mahozi.sayed.talabiya.Storage.Restaurant;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RestaurantDao {

    @Insert
    void insert(RestaurantEntity restaurantEntity);


    @Insert
    void insert(MenuItemEntity MenuItemEntity);

    @Query("SELECT * FROM RestaurantEntity ORDER BY name")
    LiveData<List<RestaurantEntity>> selectAllRestaurants();

    @Query("SELECT * FROM MenuItemEntity WHERE restaurant_name = :restaurantName ORDER BY item_name")
    LiveData<List<MenuItemEntity>> selectAllMenuItems(String restaurantName);

    @Query("Select name FROM RestaurantEntity ORDER BY name")
    List<String> selectRestaurantsNames();

    @Delete
    void deleteRestaurant(RestaurantEntity restaurantEntity);

    @Update
    void updateRestaurant(RestaurantEntity restaurantEntity);

    @Delete
    void deleteMenuItem(MenuItemEntity menuItemEntity);

    @Update
    void updateMenuItem(MenuItemEntity menuItemEntity);
}
