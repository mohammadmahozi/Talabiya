package com.mahozi.sayed.talabiya.Feature.ui.resturant.view.detail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.RestaurantActionMode;
import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.RestaurantViewModel;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Restaurant.MenuItemEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RestaurantDetailsFragment extends Fragment {

    private RestaurantViewModel restaurantViewModel;


    private RestaurantDetailAdapter mRestaurantDetailAdapter;
    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);



        mRecyclerView = view.findViewById(R.id.fragment_restaurant_details_recycler_view);

        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        //this add a separator
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mRestaurantDetailAdapter = new RestaurantDetailAdapter(getResources().getStringArray(R.array.food_categories), new RestaurantDetailRecyclerViewListener() {
            @Override
            public void onClick(MenuItemEntity menuItemEntity) {
                restaurantViewModel.setCurrentMenuItemEntity(menuItemEntity);
                startCreateFoodFragment("edit");
            }

            @Override
            public void onLongClick(MenuItemEntity menuItemEntity) {

                RestaurantActionMode restaurantActionMode = new RestaurantActionMode(new RestaurantActionMode.OnSelectionActionMode() {
                    @Override
                    public void finished() {
                        mRestaurantDetailAdapter.finishSelectionSession();
                    }

                    @Override
                    public void delete(ActionMode actionMode) {

                        deleteMenuItem(actionMode);
                    }

                    @Override
                    public void edit(ActionMode actionMode) {

                        editMenuItem(actionMode);
                    }
                });

                getActivity().startActionMode(restaurantActionMode);

            }
        });

        mRecyclerView.setAdapter(mRestaurantDetailAdapter);



        restaurantViewModel = ViewModelProviders.of(getActivity()).get(RestaurantViewModel.class);
        restaurantViewModel.populateMenuItems();

        restaurantViewModel.getAllMenuItemEntities().observe(this, new Observer<List<MenuItemEntity>>() {
            @Override
            public void onChanged(List<MenuItemEntity> menuItemEntity) {
                mRestaurantDetailAdapter.setDataList(menuItemEntity);



            }
        });





        FloatingActionButton addRestaurantFab = view.findViewById(R.id.add_restaurant_details_fab);
        addRestaurantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mRestaurantDetailAdapter.getIsActionModeOn())startCreateFoodFragment("create");
            }
        });


        return view;
    }





    @Override
    public void onResume() {
        super.onResume();


        getActivity().setTitle(restaurantViewModel.getCurrentRestaurantEntity().name + " " +getString(R.string.menu));
    }


    public void startCreateFoodFragment(String action){

        CreateFoodFragment createFoodFragment = new CreateFoodFragment();

        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        createFoodFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.restaurant_container, createFoodFragment, "CreateFoodFragment")
                .addToBackStack(null)
                .commit();

    }


    public void deleteMenuItem(ActionMode actionMode){

        ArrayList<Integer> selectedItemsPositions = new ArrayList<>(mRestaurantDetailAdapter.getSelectedItems());


        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm).setMessage(getString(R.string.delete_all_these, selectedItemsPositions.size() + ""))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        for (int pos: selectedItemsPositions){


                            restaurantViewModel.deleteMenuItem(mRestaurantDetailAdapter.get(pos));

                        }
                        actionMode.finish();
                        mRestaurantDetailAdapter.finishSelectionSession();



                    }})
                .setNegativeButton(android.R.string.no, null).show();



    }

    public void editMenuItem(ActionMode actionMode){


        ArrayList<Integer> selectedItemsPositions = new ArrayList<>(mRestaurantDetailAdapter.getSelectedItems());


        if (selectedItemsPositions.size() > 1 ){
            Toast.makeText(getContext(), R.string.edit_only_one, Toast.LENGTH_LONG).show();
        }

        else if (selectedItemsPositions.size() != 0){

            restaurantViewModel.setCurrentMenuItemEntity(mRestaurantDetailAdapter.get(selectedItemsPositions.get(0)));
            startCreateFoodFragment("edit");
            actionMode.finish();
        }
    }
    }





