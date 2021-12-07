package com.mahozi.sayed.talabiya.Feature.ui.resturant.view;

import android.os.Bundle;

import com.mahozi.sayed.talabiya.Feature.ui.BaseActivity;
import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.detail.CreateFoodFragment;
import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.main.RestaurantFragment;
import com.mahozi.sayed.talabiya.R;

public class RestaurantActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        super.onCreateDrawer();

        //getSupportActionBar().setTitle("Hello world App");

        if (findViewById(R.id.restaurant_container) != null) {

            if (savedInstanceState != null) {

                return;
            }

        }



        if (getIntent().getBooleanExtra("createMenuItem", false) || getIntent().getBooleanExtra("editMenuItem", false)){

            CreateFoodFragment createFoodFragment = new CreateFoodFragment();
            createFoodFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().replace(R.id.restaurant_container, createFoodFragment).commit();
        }


        else {

            RestaurantFragment restaurantFragment = new RestaurantFragment();
            restaurantFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().replace(R.id.restaurant_container, restaurantFragment).commit();
        }


    }
}
