package com.mahozi.sayed.talabiya.resturant.view;

import android.app.Application;

import com.mahozi.sayed.talabiya.resturant.store.MenuItemEntity;
import com.mahozi.sayed.talabiya.resturant.store.RestaurantEntity;
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RestaurantViewModel extends AndroidViewModel {


    private RestaurantEntity currentRestaurantEntity;

    private LiveData<List<RestaurantEntity>> mAllRestaurantEntities;

    private LiveData<List<MenuItemEntity>> mAllMenuItemEntities;


    private MenuItemEntity currentMenuItemEntity;

    private RestaurantStore mRestaurantRepository;

    public RestaurantViewModel(Application application){
        super(application);

        mRestaurantRepository = RestaurantStore.getInstance();
        mRestaurantRepository.init(application);

        mAllRestaurantEntities = mRestaurantRepository.selectAllRestaurants();



    }

    public void populateMenuItems(){

        mAllMenuItemEntities = mRestaurantRepository.selectAllMenuItems(getCurrentRestaurantEntity().name);



    }

    public void insertFood(MenuItemEntity menuItemEntity){
        mRestaurantRepository.insertFood(menuItemEntity);
    }

    public LiveData<List<MenuItemEntity>> getAllMenuItemEntities(){
        return mAllMenuItemEntities;
    }

    public RestaurantEntity getCurrentRestaurantEntity(){
        return currentRestaurantEntity;
    }

    public void deleteMenuItem(MenuItemEntity menuItemEntity){
        mRestaurantRepository.deleteMenuItem(menuItemEntity);
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
