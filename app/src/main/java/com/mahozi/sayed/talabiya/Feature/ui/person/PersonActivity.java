package com.mahozi.sayed.talabiya.Feature.ui.person;

import android.os.Bundle;
import android.util.Log;

import com.mahozi.sayed.talabiya.Feature.ui.BaseActivity;
import com.mahozi.sayed.talabiya.R;

public class PersonActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        super.onCreateDrawer();


        if (findViewById(R.id.restaurant_container) != null) {

            if (savedInstanceState != null) {

                return;
            }


        }



        PersonFragment personFragment = new PersonFragment();
        personFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.person_container, personFragment).commit();

    }
}
