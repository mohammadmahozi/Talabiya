package com.mahozi.sayed.talabiya.order.details.suborder;

import android.widget.CheckBox;

import com.mahozi.sayed.talabiya.order.store.SubOrderEntity;

public interface SubordersRecyclerViewListener {

    void onAddOrderItemButtonClick(SubOrderEntity subOrderEntity);
    void onLongClick(SubOrderEntity subOrderEntity);
    void onClick();

    void onStatusCheckBoxClick(CheckBox checkBox, SubOrderEntity subOrderEntity);
}
