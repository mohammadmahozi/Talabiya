package com.mahozi.sayed.talabiya.Feature.ui.order.view.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.mahozi.sayed.talabiya.Feature.ui.order.OrderViewModel;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.details.full.FullOrderFragment;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.details.info.OrderInfoFragment;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.details.suborder.SubordersFragment;
import com.mahozi.sayed.talabiya.R;


public class OrderDetailsFragment extends Fragment {

    private OrderViewModel orderViewModel;

    private final int INFO_TAB = 0;
    private final int SUBORDERS_TAB = 1;
    private final int FULL_ORDER_TAB = 2;


    private SharedPreferences.Editor editor;


    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_order_details, container, false);


        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);


        tabLayout = view.findViewById(R.id.order_details_tabs);



        orderViewModel.setSubOrders(orderViewModel.getCurrentOrder().id);


        editor = getActivity().getSharedPreferences("simple", Context.MODE_PRIVATE).edit();




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                setTab(tab);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                setTab(tab);
            }
        });





        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        SharedPreferences prefs = getActivity().getSharedPreferences("simple", Context.MODE_PRIVATE);
        TabLayout.Tab tab = tabLayout.getTabAt(prefs.getInt("orderTab", 0));
        tab.select();

        getActivity().setTitle("Order#" + orderViewModel.getCurrentOrder().id);

    }

    public void setTab(TabLayout.Tab tab){

        Fragment fragment;
        String tag = "";

        int tabPos = tab.getPosition();
        if (tabPos == INFO_TAB){

            fragment = new OrderInfoFragment();
            tag = "OrderInfoFragment";

            editor.putInt("orderTab", INFO_TAB);
            editor.apply();
        }

        else if (tabPos == SUBORDERS_TAB){

            fragment = new SubordersFragment();
            tag = "SubordersFragment";

            editor.putInt("orderTab", SUBORDERS_TAB);
            editor.apply();
        }

        else {//FULL_ORDERS_TAB

            fragment = new FullOrderFragment();
            tag = "FullOrderFragment";

            editor.putInt("orderTab", FULL_ORDER_TAB);
            editor.apply();
        }


        fragment.setArguments(getActivity().getIntent().getExtras());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.order_details_container, fragment).commit();

    }
}