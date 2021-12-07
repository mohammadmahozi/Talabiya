package com.mahozi.sayed.talabiya.Feature.ui.resturant.view.main;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.RestaurantViewModel;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantEntity;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;



public class CreateRestaurantFragment extends Fragment {

    private EditText restaurantNameEditText;
    private RestaurantViewModel restaurantViewModel;

    private String action;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_restaurant, container, false);

        restaurantNameEditText = view.findViewById(R.id.restaurant_name_edit_text);

        setHasOptionsMenu(true);

        getActivity().setTitle("Create restaurant");

        restaurantViewModel = ViewModelProviders.of(getActivity()).get(RestaurantViewModel.class);


        action = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            action = bundle.getString("action");


        }

        if (action.equals("edit")){
            restaurantNameEditText.setText(restaurantViewModel.getCurrentRestaurantEntity().name);
        }



        return view;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.confirm_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.confirm:

                String restaurantName = restaurantNameEditText.getText().toString().trim();

                try {

                    if (action.equals("edit")){


                        RestaurantEntity restaurantEntity = restaurantViewModel.getCurrentRestaurantEntity();
                        restaurantEntity.name = restaurantName;

                        restaurantViewModel.updateRestaurant(restaurantEntity);


                    }


                    else {

                            restaurantViewModel.insertRestaurant(new RestaurantEntity(restaurantName));
                        }

                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        getActivity().getSupportFragmentManager().popBackStack();

                    }

                catch (SQLiteConstraintException e){
                    Toast.makeText(getContext(), getString(R.string.this_already_added, restaurantName), Toast.LENGTH_LONG).show();
                }


                return true;

        }

        return false;
    }
}
