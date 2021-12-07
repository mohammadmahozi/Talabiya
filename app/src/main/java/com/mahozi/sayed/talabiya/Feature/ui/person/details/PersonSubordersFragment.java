package com.mahozi.sayed.talabiya.Feature.ui.person.details;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahozi.sayed.talabiya.Feature.ui.order.view.create.DatePickerPopUp;
import com.mahozi.sayed.talabiya.Feature.ui.person.PersonViewModel;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderAndPersonSuborder;

import java.util.List;

public class PersonSubordersFragment extends Fragment {

    private PersonViewModel personViewModel;


    private RecyclerView mRecyclerView;
    private FragmentPersonSubordersAdapter fragmentPersonSubordersAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_person_suborder, container, false);


        personViewModel = ViewModelProviders.of(getActivity()).get(PersonViewModel.class);


        mRecyclerView = view.findViewById(R.id.fragment_person_suborders_recycler_view);



        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);


        fragmentPersonSubordersAdapter = new FragmentPersonSubordersAdapter(getContext(), new PersonSubordersListener() {
            @Override
            public void onClick(OrderAndPersonSuborder orderAndPersonSuborder) {

            }

            @Override
            public void onStatusCheckBoxClick(CheckBox checkBox, int position, OrderAndPersonSuborder orderAndPersonSuborder) {
            //if it was checked and got unchecked. the condition happens after the click and the chnage
                if (!checkBox.isChecked()){

                    checkBox.setChecked(true);

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm").setMessage("Change status to not payed? This will delete the payment date.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {


                                    personViewModel.updateSuborderStatus(getString(R.string.not_payed), 0, orderAndPersonSuborder.id);

                                    if (orderAndPersonSuborder.personName.equals(orderAndPersonSuborder.payer)){
                                        personViewModel.updateOrderStatus(getString(R.string.not_payed), 0, orderAndPersonSuborder.orderId);



                                    }

                                    fragmentPersonSubordersAdapter.get(position).paymentDate = getString(R.string.not_payed);
                                    fragmentPersonSubordersAdapter.get(position).status = false;


                                    checkBox.setText("Not payed");
                                    checkBox.setChecked(false);



                                }})
                            .setNegativeButton(android.R.string.no, null).show();




                }

                else {
                    checkBox.setChecked(false);
                    DatePickerPopUp datePick = new DatePickerPopUp(new DatePickerPopUp.OnDateReceived() {
                        @Override
                        public void getDate(String date) {


                            personViewModel.updateSuborderStatus(date, 1, orderAndPersonSuborder.id);

                            if (orderAndPersonSuborder.personName.equals(orderAndPersonSuborder.payer)){
                                personViewModel.updateOrderStatus(date, 1, orderAndPersonSuborder.orderId);



                            }

                            fragmentPersonSubordersAdapter.get(position).paymentDate = date;
                            fragmentPersonSubordersAdapter.get(position).status = true;

                            checkBox.setChecked(true);
                            checkBox.setText(date);


                        }
                    });

                    datePick.show(getActivity().getSupportFragmentManager(), "DatePicker");


                }
            }
        });

        mRecyclerView.setAdapter(fragmentPersonSubordersAdapter);

        fragmentPersonSubordersAdapter.setDataList(personViewModel.selectAllPersonInfo(personViewModel.getSelectedPerson().name));


        return view;
    }


    }
