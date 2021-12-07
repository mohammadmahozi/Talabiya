package com.mahozi.sayed.talabiya.Feature.ui.order.view.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mahozi.sayed.talabiya.Feature.ui.order.OrderViewModel;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateOrderFragment extends Fragment {

    private Spinner restaurantNameSpinner;
    private EditText orderDateEditText;
    private EditText orderTimeEditText;

    private OrderViewModel mOrderViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_order, container, false);

        mOrderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);



        restaurantNameSpinner = view.findViewById(R.id.restaurant_name_spinner);


        getActivity().setTitle("Create order");


        orderTimeEditText = view.findViewById(R.id.order_time_edit_text);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        orderTimeEditText.setText(timeFormat.format(Calendar.getInstance().getTime()));

        orderTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerPopUp timePicker = new TimePickerPopUp(new TimePickerPopUp.OnTimeReceived() {
                    @Override
                    public void getTime(String time) {
                        orderTimeEditText.setText(time);
                    }
                });

                timePicker.show(getActivity().getSupportFragmentManager(), "TimePicker");

            }

        });



        orderDateEditText = view.findViewById(R.id.order_date_edit_text);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, E");
        orderDateEditText.setText(dateFormat.format(new Date()));

        orderDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerPopUp datePick = new DatePickerPopUp(new DatePickerPopUp.OnDateReceived() {
                    @Override
                    public void getDate(String date) {
                       orderDateEditText.setText(date.replace("\n       ", ", "));
                    }
                });

                datePick.show(getActivity().getSupportFragmentManager(), "DatePicker");

            }
        });




        List<String> restaurantNamesList = mOrderViewModel.selectRestaurantsNames();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, restaurantNamesList);
        restaurantNameSpinner.setAdapter(dataAdapter);


        setHasOptionsMenu(true);



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

                if (restaurantNameSpinner.getSelectedItem() !=null){
                    String restaurantName = restaurantNameSpinner.getSelectedItem().toString();
                    String date = orderDateEditText.getText().toString();
                    String time = orderTimeEditText.getText().toString();

                    mOrderViewModel.insertOrder(new OrderEntity(restaurantName, date, time));
                    getActivity().getSupportFragmentManager().popBackStack();
                }

                else {
                    Toast.makeText(getContext(), R.string.select_retaurant_first, Toast.LENGTH_LONG).show();
                }


                return true;

        }

        return false;
    }
}
