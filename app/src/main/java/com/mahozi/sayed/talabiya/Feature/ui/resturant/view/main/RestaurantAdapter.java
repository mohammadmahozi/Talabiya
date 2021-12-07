package com.mahozi.sayed.talabiya.Feature.ui.resturant.view.main;

import android.view.View;
import android.widget.TextView;

import com.mahozi.sayed.talabiya.Feature.ui.BaseRecyclerAdapter;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantEntity;

import androidx.annotation.NonNull;

public class RestaurantAdapter extends BaseRecyclerAdapter<RestaurantEntity> {



    private TextView restaurantNameTextView;
    private RestaurantRecyclerViewListener mRestaurantRecyclerViewListener;



    public RestaurantAdapter(RestaurantRecyclerViewListener restaurantRecyclerViewListener){

        mRecyclerItemLayoutResourceId = R.layout.restaurant_recycler_view_item;
        mRestaurantRecyclerViewListener = restaurantRecyclerViewListener;

    }

    /*@Override
    public void mapViews(View itemView) {

        restaurantNameTextView = bind(holder.itemView, R.id.restaurant_recycler_item_restaurant_name_text_view);
    }*/



    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.BaseViewHolder holder, RestaurantEntity model) {
        restaurantNameTextView = bind(holder.itemView, R.id.restaurant_recycler_item_restaurant_name_text_view);

        restaurantNameTextView.setText(model.name);
    }

    @Override
    public void onItemClick(View view) {

        int position = (Integer) view.getTag();
        mRestaurantRecyclerViewListener.onClick(mDataList.get(position));

    }

    @Override
    public void onItemLongClick(View view) {

        int position = (Integer) view.getTag();
        mRestaurantRecyclerViewListener.onLongClick(mDataList.get(position));

    }
}
