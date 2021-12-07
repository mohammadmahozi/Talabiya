package com.mahozi.sayed.talabiya.Feature.ui.resturant.view;

import android.app.Application;
import android.util.Log;

import com.mahozi.sayed.talabiya.Domain.Resturant.Resturant;
import com.mahozi.sayed.talabiya.Storage.Restaurant.MenuItemEntity;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantEntity;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantRepository;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RestaurantViewModel extends AndroidViewModel {


    private RestaurantEntity currentRestaurantEntity;

    private LiveData<List<RestaurantEntity>> mAllRestaurantEntities;

    private LiveData<List<MenuItemEntity>> mAllMenuItemEntities;


    private MenuItemEntity currentMenuItemEntity;

    private RestaurantRepository mRestaurantRepository;

    public RestaurantViewModel(Application application){
        super(application);

        mRestaurantRepository = RestaurantRepository.getInstance();
        mRestaurantRepository.init(application);

        mAllRestaurantEntities = mRestaurantRepository.selectAllRestaurants();



    }

    public void populateMenuItems(){

        mAllMenuItemEntities = mRestaurantRepository.selectAllMenuItems(getCurrentRestaurantEntity().name);



    }

    public void insertRestaurant(RestaurantEntity restaurantEntity){
        mRestaurantRepository.insertRestaurant(restaurantEntity);
    }

    public void insertFood(MenuItemEntity menuItemEntity){
        mRestaurantRepository.insertFood(menuItemEntity);
    }

    public LiveData<List<RestaurantEntity>> getAllRestaurantEntities(){
        return mAllRestaurantEntities;
    }

    public LiveData<List<MenuItemEntity>> getAllMenuItemEntities(){
        return mAllMenuItemEntities;
    }



    public void setSelectedRestaurant(RestaurantEntity restaurantEntity){
        currentRestaurantEntity = restaurantEntity;
    }

    public RestaurantEntity getCurrentRestaurantEntity(){
        return currentRestaurantEntity;
    }


    public void deleteRestaurant(RestaurantEntity restaurantEntity){
        mRestaurantRepository.deleteRestaurant(restaurantEntity);
    }

    public void deleteMenuItem(MenuItemEntity menuItemEntity){
        mRestaurantRepository.deleteMenuItem(menuItemEntity);
    }

    public void updateRestaurant(RestaurantEntity restaurantEntity){
        mRestaurantRepository.updateRestaurant(restaurantEntity);
    }

    public void updateMenuItem(MenuItemEntity menuItemEntity){
        mRestaurantRepository.updateMenuItem(menuItemEntity);
    }

    public void setCurrentMenuItemEntity(MenuItemEntity currentMenuItemEntity) {
        this.currentMenuItemEntity = currentMenuItemEntity;
    }

    public MenuItemEntity getCurrentMenuItemEntity() {
        return currentMenuItemEntity;
    }
}
