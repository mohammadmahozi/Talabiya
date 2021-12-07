package com.mahozi.sayed.talabiya.Feature.ui.order.view.details.suborder;

import android.widget.CheckBox;

import com.mahozi.sayed.talabiya.Storage.order.SubOrderEntity;

public interface SubordersRecyclerViewListener {

    void onAddOrderItemButtonClick(SubOrderEntity subOrderEntity);
    void onLongClick(SubOrderEntity subOrderEntity);
    void onClick();

    void onStatusCheckBoxClick(CheckBox checkBox, SubOrderEntity subOrderEntity);
}
