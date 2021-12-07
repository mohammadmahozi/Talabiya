package com.mahozi.sayed.talabiya.Feature.ui.resturant.view.detail;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mahozi.sayed.talabiya.Feature.ui.BaseRecyclerAdapter;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Restaurant.MenuItemEntity;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RestaurantDetailAdapter extends BaseRecyclerAdapter<MenuItemEntity> {


    private TextView foodNameTextView;
    private TextView foodPriceTextView;
    private TextView foodCategoryTextView;


    RestaurantDetailRecyclerViewListener mRestaurantDetailRecyclerViewListener;


    private String[] categories;

    public RestaurantDetailAdapter(String[] categories, RestaurantDetailRecyclerViewListener restaurantDetailRecyclerViewListener){

        this.categories = categories;


        mRecyclerItemLayoutResourceId = R.layout.resturant_detail_recycler_item;

        mRestaurantDetailRecyclerViewListener = restaurantDetailRecyclerViewListener;

    }

    /*@Override
    public void mapViews(View itemView) {

        foodNameTextView = bind(holder.itemView, R.id.restaurant_detail_recycler_item_name_text_view);
        foodPriceTextView = bind(holder.itemView, R.id.restaurant_detail_recycler_item_price_text_view);
        foodCategoryTextView = bind(holder.itemView, R.id.restaurant_detail_recycler_item_category_text_view);

    }*/


    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.BaseViewHolder holder, MenuItemEntity model) {

        foodNameTextView = bind(holder.itemView, R.id.restaurant_detail_recycler_item_name_text_view);
        foodPriceTextView = bind(holder.itemView, R.id.restaurant_detail_recycler_item_price_text_view);
        foodCategoryTextView = bind(holder.itemView, R.id.restaurant_detail_recycler_item_category_text_view);

        foodNameTextView.setText(model.itemName);
        foodPriceTextView.setText(String.valueOf(model.price));

        foodCategoryTextView.setText(categories[model.category]);


    }

    @Override
    public void onItemClick(View v) {


        int position = (Integer) v.getTag();

        mRestaurantDetailRecyclerViewListener.onClick(mDataList.get(position));

    }


    @Override
    public void onItemLongClick(View view) {

        int position = (Integer) view.getTag();

        mRestaurantDetailRecyclerViewListener.onLongClick(mDataList.get(position));

    }
}
