package com.mahozi.sayed.talabiya.Feature.ui.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mahozi.sayed.talabiya.Feature.ui.BaseActivity;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.details.info.OrderInfoFragment;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.main.OrderFragment;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.orderitem.CreateSubOrderItemFragment;
import com.mahozi.sayed.talabiya.R;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

public class OrderActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        super.onCreateDrawer();


        if (findViewById(R.id.order_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            OrderFragment orderFragment = new OrderFragment();

            orderFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.order_container, orderFragment).commit();


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        CreateSubOrderItemFragment f = (CreateSubOrderItemFragment) getSupportFragmentManager().findFragmentByTag("CreateSubOrderItemFragment");


        if(f != null && f.onBackPressed()){


        }



        else {
            super.onBackPressed();
        }



    }




}
