package com.mahozi.sayed.talabiya.resturant.view.detail;

import com.mahozi.sayed.talabiya.resturant.store.MenuItemEntity;

public interface RestaurantDetailRecyclerViewListener {


    void onClick(MenuItemEntity menuItemEntity);
    void onLongClick(MenuItemEntity menuItemEntity);
}
