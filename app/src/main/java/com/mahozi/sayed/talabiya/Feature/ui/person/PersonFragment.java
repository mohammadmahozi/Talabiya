package com.mahozi.sayed.talabiya.Feature.ui.person;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahozi.sayed.talabiya.Domain.Person.Person;
import com.mahozi.sayed.talabiya.Feature.ui.person.details.PersonDetailsFragment;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;

import java.util.ArrayList;
import java.util.List;

public class PersonFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private PersonAdapter mPersonAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private PersonViewModel personViewModel;

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("People");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);


        setHasOptionsMenu(true);


        boolean getPerson = getActivity().getIntent().getExtras().getBoolean("getPerson", false);

        personViewModel = ViewModelProviders.of(getActivity()).get(PersonViewModel.class);

        mRecyclerView = view.findViewById(R.id.fragment_person_recycler_view);
        //this add a separator
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));



        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);




        mPersonAdapter = new PersonAdapter(new PersonRecyclerViewListener() {
            @Override
            public void onClick(PersonEntity personEntity) {

                if (getPerson){

                    //personViewModel.setSelectedPerson(personEntity);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("personName", personEntity.name);
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                }

                else {

                    personViewModel.setSelectedPerson(personEntity);
                    startPersonDetailsFragment();
                }

            }

            @Override
            public void onLongClick(PersonEntity personEntity) {

                PersonActionMode personActionMode = new PersonActionMode(new PersonActionMode.OnSelectionActionMode() {
                    @Override
                    public void finished() {
                        mPersonAdapter.finishSelectionSession();
                    }

                    @Override
                    public void delete(ActionMode actionMode) {

                        deletePerson(actionMode);
                    }

                    @Override
                    public void edit(ActionMode actionMode) {

                        editPerson(actionMode);
                    }
                });

                getActivity().startActionMode(personActionMode);
            }
        });

        personViewModel.getAllPersonEntities().observe(this, new Observer<List<PersonEntity>>() {
            @Override
            public void onChanged(List<PersonEntity> personEntities) {
                mPersonAdapter.setDataList(personEntities);

            }
        });

        mRecyclerView.setAdapter(mPersonAdapter);



        FloatingActionButton addRestaurantFab = view.findViewById(R.id.add_person_fab);
        addRestaurantFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mPersonAdapter.getIsActionModeOn())startCreatePersonFragment("create");
            }
        });

        return view;
    }


    public void startCreatePersonFragment(String action){

        CreatePersonFragment createPersonFragment = new CreatePersonFragment();

        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        createPersonFragment.setArguments(bundle);


        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.person_container, createPersonFragment, "CreatePersonFragment")
                .addToBackStack(null)
                .commit();

    }


    public void deletePerson(ActionMode actionMode){

        ArrayList<Integer> selectedItemsPositions = new ArrayList<>(mPersonAdapter.getSelectedItems());


        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm).setMessage(getString(R.string.delete_all_these, selectedItemsPositions.size() + ""))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        for (int pos: selectedItemsPositions){


                            personViewModel.deletePerson(mPersonAdapter.get(pos));

                        }
                        actionMode.finish();
                        mPersonAdapter.finishSelectionSession();



                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void editPerson(ActionMode actionMode){

        ArrayList<Integer> selectedItemsPositions = new ArrayList<>(mPersonAdapter.getSelectedItems());


        if (selectedItemsPositions.size() > 1 ){
            Toast.makeText(getContext(), R.string.edit_only_one, Toast.LENGTH_LONG).show();
        }

        else if (selectedItemsPositions.size() != 0){

            personViewModel.setSelectedPerson(mPersonAdapter.get(selectedItemsPositions.get(0)));
            startCreatePersonFragment("edit");
            actionMode.finish();
        }
    }

    public void startPersonDetailsFragment(){


        PersonDetailsFragment personDetailsFragment = new PersonDetailsFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.person_container, personDetailsFragment, "PersonDetailsFragment")
                .addToBackStack(null)
                .commit();



    }

}
