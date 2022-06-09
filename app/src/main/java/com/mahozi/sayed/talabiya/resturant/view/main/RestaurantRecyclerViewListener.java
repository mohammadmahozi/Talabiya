package com.mahozi.sayed.talabiya.resturant.view.main;

import com.mahozi.sayed.talabiya.resturant.store.RestaurantEntity;

public interface RestaurantRecyclerViewListener {

    void onClick(RestaurantEntity restaurantEntity);
    void onLongClick(RestaurantEntity restaurantEntity);

}
