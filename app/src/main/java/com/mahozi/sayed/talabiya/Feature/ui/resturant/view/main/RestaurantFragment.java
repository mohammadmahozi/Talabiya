package com.mahozi.sayed.talabiya.Feature.ui.resturant.view.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.RestaurantActionMode;
import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.RestaurantViewModel;
import com.mahozi.sayed.talabiya.Feature.ui.resturant.view.detail.RestaurantDetailsFragment;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RestaurantFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private RecyclerView.LayoutManager layoutManager;

    RestaurantViewModel restaurantViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);


        restaurantViewModel = ViewModelProviders.of(getActivity()).get(RestaurantViewModel.class);


        mRecyclerView = view.findViewById(R.id.fragment_restaurant_recycler_view);

        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        //this add a separator
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mRestaurantAdapter = new RestaurantAdapter(new RestaurantRecyclerViewListener() {
            @Override
            public void onClick(RestaurantEntity restaurantEntity) {

                restaurantViewModel.setSelectedRestaurant(restaurantEntity);
                startRestaurantDetailFragment();

            }

            @Override
            public void onLongClick(RestaurantEntity restaurantEntity){

                RestaurantActionMode restaurantActionMode = new RestaurantActionMode(new RestaurantActionMode.OnSelectionActionMode() {
                    @Override
                    public void finished() {
                        mRestaurantAdapter.finishSelectionSession();
                    }

                    @Override
                    public void delete(ActionMode actionMode) {


                        deleteRestaurant(actionMode);
                    }

                    @Override
                    public void edit(ActionMode actionMode) {

                        editRestaurant(actionMode);
                    }
                });

                getActivity().startActionMode(restaurantActionMode);
            }
        });
        mRecyclerView.setAdapter(mRestaurantAdapter);



        restaurantViewModel.getAllRestaurantEntities().observe(this, new Observer<List<RestaurantEntity>>() {
            @Override
            public void onChanged(List<RestaurantEntity> restaurantEntities) {
                mRestaurantAdapter.setDataList(restaurantEntities);

            }
        });


        FloatingActionButton addRestaurantFab = view.findViewById(R.id.add_restaurant_fab);
        addRestaurantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mRestaurantAdapter.getIsActionModeOn())startCreateRestaurantFragment("create");
            }
        });



        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Restaurants");

    }


    public void startCreateRestaurantFragment(String action){
        CreateRestaurantFragment createRestaurantFragment = new CreateRestaurantFragment();

        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        createRestaurantFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.restaurant_container, createRestaurantFragment, "CreateRestaurantFragment")
                .addToBackStack(null)
                .commit();
    }


    public void startRestaurantDetailFragment(){

        RestaurantDetailsFragment restaurantDetailsFragment = new RestaurantDetailsFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.restaurant_container, restaurantDetailsFragment, "RestaurantDetailsFragment")
                .addToBackStack(null)
                .commit();

    }

    public void deleteRestaurant(ActionMode actionMode){

        ArrayList<Integer> selectedItemsPositions = new ArrayList<>(mRestaurantAdapter.getSelectedItems());


        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm).setMessage(getString(R.string.delete_all_these, selectedItemsPositions.size() + ""))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        for (int pos: selectedItemsPositions){


                            restaurantViewModel.deleteRestaurant(mRestaurantAdapter.get(pos));

                        }
                        actionMode.finish();
                        mRestaurantAdapter.finishSelectionSession();



                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void editRestaurant(ActionMode actionMode){

        ArrayList<Integer> selectedItemsPositions = new ArrayList<>(mRestaurantAdapter.getSelectedItems());


        if (selectedItemsPositions.size() > 1 ){
            Toast.makeText(getContext(), R.string.edit_only_one, Toast.LENGTH_LONG).show();
        }

        else if (selectedItemsPositions.size() != 0){

            restaurantViewModel.setSelectedRestaurant(mRestaurantAdapter.get(selectedItemsPositions.get(0)));
            startCreateRestaurantFragment("edit");
            actionMode.finish();
        }
    }


}
