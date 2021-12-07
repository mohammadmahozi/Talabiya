package com.mahozi.sayed.talabiya.Storage.Restaurant;

import android.app.Application;

import com.mahozi.sayed.talabiya.Storage.TalabiyaDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RestaurantRepository {

    private static volatile RestaurantRepository mRestaurantRepository;

    private RestaurantDao mRestaurantDao;


    private RestaurantRepository(){



    }

    public static RestaurantRepository getInstance(){

        if(mRestaurantRepository == null)
            mRestaurantRepository = new RestaurantRepository();

        return mRestaurantRepository;
    }

    public void init(Application application){
        TalabiyaDatabase talabiyaDatabase = TalabiyaDatabase.getDatabase(application);
        mRestaurantDao = talabiyaDatabase.restaurantDao();

    }

    public void insertRestaurant(RestaurantEntity restaurantEntity){
        mRestaurantDao.insert(restaurantEntity);
    }

    public LiveData<List<RestaurantEntity>> selectAllRestaurants(){
        return mRestaurantDao.selectAllRestaurants();
    }

    public void insertFood(MenuItemEntity menuItemEntity){
        mRestaurantDao.insert(menuItemEntity);
    }

    public LiveData<List<MenuItemEntity>> selectAllMenuItems(String restaurantName){

        return mRestaurantDao.selectAllMenuItems(restaurantName);

    }

    public List<String> selectRestaurantsNames(){
        return mRestaurantDao.selectRestaurantsNames();
    }


    public void deleteRestaurant(RestaurantEntity restaurantEntity){
        mRestaurantDao.deleteRestaurant(restaurantEntity);
    }

    public void deleteMenuItem(MenuItemEntity menuItemEntity){
        mRestaurantDao.deleteMenuItem(menuItemEntity);
    }

    public void updateRestaurant(RestaurantEntity restaurantEntity){
        mRestaurantDao.updateRestaurant(restaurantEntity);
    }

    public void updateMenuItem(MenuItemEntity menuItemEntity){
        mRestaurantDao.updateMenuItem(menuItemEntity);
    }

}
