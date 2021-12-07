package com.mahozi.sayed.talabiya.Feature.ui.person.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mahozi.sayed.talabiya.Feature.ui.order.view.create.DatePickerPopUp;
import com.mahozi.sayed.talabiya.Feature.ui.person.PersonViewModel;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderAndPersonSuborder;

import java.util.List;

public class PersonInfoFragment extends Fragment {


    PersonViewModel personViewModel;

    private EditText numOfOrders;
    private EditText numOfPaidOrders;
    private EditText fromDate;
    private EditText toDate;

    private EditText totalPayed;
    private EditText totalOwned;
    private EditText grandTotal;

    private ImageButton receivePayment;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_person_info, container, false);


        personViewModel = ViewModelProviders.of(getActivity()).get(PersonViewModel.class);

        numOfOrders = view.findViewById(R.id.person_info_number_of_orders);
        numOfPaidOrders = view.findViewById(R.id.person_info_number_of_orders_paid_for);

        fromDate = view.findViewById(R.id.person_info_from_date);
        toDate = view.findViewById(R.id.person_info_to_date);

        totalPayed = view.findViewById(R.id.person_info_total_paid);
        totalOwned = view.findViewById(R.id.person_info_total_owned);

        grandTotal = view.findViewById(R.id.person_info_grand_total);

        String personName = personViewModel.getSelectedPerson().name;

        List<OrderAndPersonSuborder> orderAndPersonSuborderList = personViewModel.selectPersonInfo(personName);


        numOfOrders.setText(orderAndPersonSuborderList.size() + "");

        int numOfpaidOrdersCounter = 0;
        double totalPayedCount = 0;
        double totalOwnedCount = 0;

        if (orderAndPersonSuborderList.size() > 0){

            for (OrderAndPersonSuborder orderAndPersonSuborder : orderAndPersonSuborderList){

                if (orderAndPersonSuborder.payer != null && orderAndPersonSuborder.payer.equals(personName)){

                    numOfpaidOrdersCounter++;
                    totalPayedCount = totalPayedCount + orderAndPersonSuborder.orderTotal;

                }

                totalOwnedCount = totalOwnedCount + orderAndPersonSuborder.total;
            }

            numOfPaidOrders.setText(numOfpaidOrdersCounter+ "");


            fromDate.setText(orderAndPersonSuborderList.get(0).date.replace("\n       ", ", "));
            toDate.setText(orderAndPersonSuborderList.get(orderAndPersonSuborderList.size()-1).date.replace("\n       ", ", "));


            totalPayed.setText(totalPayedCount + "");
            totalOwned.setText(totalOwnedCount + "");

            double grandTotalCount = totalPayedCount - totalOwnedCount;

            if (grandTotalCount < 0){
                grandTotal.setText(getString(R.string.debit, Math.abs(grandTotalCount)));

            }

            else {
                grandTotal.setText(getString(R.string.credit, grandTotalCount));

            }


        }



        receivePayment = view.findViewById(R.id.person_info_receive_payment);
        receivePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderAndPersonSuborderList.size() > 0){


                        DatePickerPopUp datePick = new DatePickerPopUp(new DatePickerPopUp.OnDateReceived() {
                            @Override
                            public void getDate(String date) {

                                for (OrderAndPersonSuborder orderAndPersonSuborder: orderAndPersonSuborderList){
                                    personViewModel.updateSuborderStatus(date, 1, orderAndPersonSuborder.id);

                                    if (orderAndPersonSuborder.personName.equals(orderAndPersonSuborder.payer)){
                                        personViewModel.updateOrderStatus(date, 1, orderAndPersonSuborder.orderId);

                                    }

                                }

                                Toast.makeText(getContext(), R.string.all_orders_marked, Toast.LENGTH_LONG).show();

                            }
                        });

                        datePick.show(getActivity().getSupportFragmentManager(), "DatePicker");



                }

                else {
                    Toast.makeText(getContext(), R.string.no_unpaid_suborders, Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }


}
