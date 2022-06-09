package com.mahozi.sayed.talabiya.person.details;

import android.widget.CheckBox;

import com.mahozi.sayed.talabiya.order.store.OrderAndPersonSuborder;

public interface PersonSubordersListener {

    void onClick(OrderAndPersonSuborder orderAndPersonSuborder);
    void onStatusCheckBoxClick(CheckBox checkBox, int position, OrderAndPersonSuborder orderAndPersonSuborder);
}
