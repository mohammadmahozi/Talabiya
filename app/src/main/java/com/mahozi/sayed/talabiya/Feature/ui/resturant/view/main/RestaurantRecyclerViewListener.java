package com.mahozi.sayed.talabiya.Feature.ui.resturant.view.main;

import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantEntity;

public interface RestaurantRecyclerViewListener {

    void onClick(RestaurantEntity restaurantEntity);
    void onLongClick(RestaurantEntity restaurantEntity);

}
