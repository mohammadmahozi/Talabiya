package com.mahozi.sayed.talabiya.Feature.ui.order.view.create;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DatePickerPopUp extends DialogFragment implements DatePickerDialog.OnDateSetListener{



    private OnDateReceived onDateReceived;

    public DatePickerPopUp(OnDateReceived onDateReceived) {
        this.onDateReceived = onDateReceived;

    }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            // Do something with the date chosen by the user
            String stringDate = year + "/" + (month+1) + "/" + day;

            Date date;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

            try {

                date = dateFormat.parse(stringDate);

                dateFormat = new SimpleDateFormat("yyyy/MM/dd\n       E");

                onDateReceived.getDate(dateFormat.format(date));
            }

            catch (ParseException e){


            }


        }

    public interface OnDateReceived {

        void getDate(String date);
    }
}
