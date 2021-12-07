package com.mahozi.sayed.talabiya.Feature.ui.resturant.view.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.RestaurantViewModel;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Restaurant.MenuItemEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class CreateFoodFragment extends Fragment {

    protected final int PASTRY = 0;
    protected final int GRILLS = 1;
    protected final int SANDWICH = 2;
    protected final int BURGER = 3;
    protected final int APPETIZER = 4;
    protected final int DRINKS = 5;

    private EditText mNameEditText;
    private EditText mPriceEditText;
    private Spinner mCategorySpinner;

    private String action;

    private MenuItemEntity menuItemEntity;


    private RestaurantViewModel viewModel;

    private String oldMenuItemName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_food, container, false);


        getActivity().setTitle("Create menu item");

        viewModel = ViewModelProviders.of(getActivity()).get(RestaurantViewModel.class);

        mNameEditText = view.findViewById(R.id.fragment_create_food_name_edit_text);

        mPriceEditText = view.findViewById(R.id.fragment_create_food_price_edit_text);


        mCategorySpinner = view.findViewById(R.id.fragment_create_food_category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.food_categories,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapter);



        setHasOptionsMenu(true);



        action = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            action = bundle.getString("action", "");


        }

        if (action.equals("edit")){

            menuItemEntity = viewModel.getCurrentMenuItemEntity();

            mNameEditText.setText(menuItemEntity.itemName);
            mPriceEditText.setText(String.valueOf(menuItemEntity.price));

            mCategorySpinner.setSelection(menuItemEntity.category);

        }

        else if (getActivity().getIntent().getBooleanExtra("editMenuItem", false)){

            oldMenuItemName = getActivity().getIntent().getStringExtra("menuItem");

            mNameEditText.setText(getActivity().getIntent().getStringExtra("menuItem"));
            mPriceEditText.setText(String.valueOf(getActivity().getIntent().getDoubleExtra("price", 1)));

            mCategorySpinner.setSelection(getActivity().getIntent().getIntExtra("category", 0));

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


                String restaurantName;
                if (getActivity().getIntent().hasExtra("restaurantName")){

                    restaurantName = getActivity().getIntent().getStringExtra("restaurantName");
                }

                else {

                    restaurantName = viewModel.getCurrentRestaurantEntity().name;
                }

                String foodName = mNameEditText.getText().toString().trim();


                try {
                    double foodPrice = Double.parseDouble(mPriceEditText.getText().toString().trim());

                    if (action.equals("edit")){

                        menuItemEntity.itemName = foodName;
                        menuItemEntity.price = foodPrice;
                        menuItemEntity.category = mCategorySpinner.getSelectedItemPosition();


                        viewModel.updateMenuItem(menuItemEntity);

                    }

                    else if (getActivity().getIntent().getBooleanExtra("editMenuItem", false)){

                        menuItemEntity = new MenuItemEntity(restaurantName, foodName, foodPrice, mCategorySpinner.getSelectedItemPosition());
                        menuItemEntity.id = getActivity().getIntent().getIntExtra("id", 0);

                        viewModel.updateMenuItem(menuItemEntity);
                    }

                    else {

                        viewModel.insertFood(new MenuItemEntity(restaurantName, foodName, foodPrice, mCategorySpinner.getSelectedItemPosition()));


                    }

                    boolean createMenuItem = getActivity().getIntent().getBooleanExtra("createMenuItem", false)
                            || getActivity().getIntent().getBooleanExtra("editMenuItem", false);

                    if (createMenuItem){

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("menuItemName", foodName);
                        returnIntent.putExtra("menuItemPrice", foodPrice);
                        returnIntent.putExtra("oldMenuItemName", oldMenuItemName);

                        getActivity().setResult(Activity.RESULT_OK, returnIntent);

                        getActivity().finish();


                    }

                    else {
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                }

                catch (NumberFormatException e){
                    Toast.makeText(getContext(), getString(R.string.incorrect_number), Toast.LENGTH_LONG).show();

                }

                catch (SQLiteConstraintException e1){

                    Toast.makeText(getContext(), getString(R.string.this_already_added, foodName), Toast.LENGTH_LONG).show();

                }


                return true;

        }

        return false;
    }
}
