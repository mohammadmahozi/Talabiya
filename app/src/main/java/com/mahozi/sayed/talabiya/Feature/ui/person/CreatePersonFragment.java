package com.mahozi.sayed.talabiya.Feature.ui.person;

import androidx.fragment.app.Fragment;

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

import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;

import androidx.lifecycle.ViewModelProviders;



public class CreatePersonFragment extends Fragment {

    private EditText personNameEditText;

    private String action;

    private PersonViewModel personViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_person, container, false);

        personNameEditText = view.findViewById(R.id.person_name_edit_text);

        setHasOptionsMenu(true);


        personViewModel = ViewModelProviders.of(getActivity()).get(PersonViewModel.class);

        action = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            action = bundle.getString("action");


        }

        getActivity().setTitle("Create person");

        if (action.equals("edit")){
            getActivity().setTitle("Edit person");

            personNameEditText.setText(personViewModel.getSelectedPerson().name);
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

                String personName = personNameEditText.getText().toString().trim();

                try {

                if (action.equals("edit")){
                    personViewModel.getSelectedPerson().name = personName;
                    personViewModel.updatePerson(personViewModel.getSelectedPerson());
                }

                else {

                    personViewModel.insertPerson(new PersonEntity(personName));


                }


                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    getActivity().getSupportFragmentManager().popBackStack();
                }

                catch (SQLiteConstraintException e){
                    Toast.makeText(getContext(), getString(R.string.this_already_added, personName), Toast.LENGTH_LONG).show();
                }


                return true;

        }

        return false;
    }
}
