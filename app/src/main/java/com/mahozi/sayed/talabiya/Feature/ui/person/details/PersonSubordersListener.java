package com.mahozi.sayed.talabiya.Feature.ui.person.details;

import android.widget.CheckBox;

import com.mahozi.sayed.talabiya.Storage.order.OrderAndPersonSuborder;

public interface PersonSubordersListener {

    void onClick(OrderAndPersonSuborder orderAndPersonSuborder);
    void onStatusCheckBoxClick(CheckBox checkBox, int position, OrderAndPersonSuborder orderAndPersonSuborder);
}
