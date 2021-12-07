package com.mahozi.sayed.talabiya.Feature.ui.person.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.mahozi.sayed.talabiya.Feature.ui.person.PersonViewModel;
import com.mahozi.sayed.talabiya.R;

public class PersonDetailsFragment extends Fragment{


    private PersonViewModel personViewModel;

    private final int INFO_TAB = 0;
    private final int SUBORDERS_TAB = 1;


    private SharedPreferences.Editor editor;


    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_person_details, container, false);


        personViewModel = ViewModelProviders.of(getActivity()).get(PersonViewModel.class);


        tabLayout = view.findViewById(R.id.person_details_tabs);



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
        TabLayout.Tab tab = tabLayout.getTabAt(prefs.getInt("personTab", 0));
        tab.select();


        getActivity().setTitle(personViewModel.getSelectedPerson().name  + " details");
    }

    public void setTab(TabLayout.Tab tab){

        Fragment fragment;
        String tag = "";

        int tabPos = tab.getPosition();
        if (tabPos == INFO_TAB){

            fragment = new PersonInfoFragment();
            tag = "PersonInfoFragment";

            editor.putInt("personTab", INFO_TAB);
            editor.apply();
        }

        else {//(tabPos == SUBORDERS_TAB)

            fragment = new PersonSubordersFragment();
            tag = "PersonSubordersFragment";

            editor.putInt("personTab", SUBORDERS_TAB);
            editor.apply();
        }



        fragment.setArguments(getActivity().getIntent().getExtras());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.person_details_container, fragment).commit();

    }
}
