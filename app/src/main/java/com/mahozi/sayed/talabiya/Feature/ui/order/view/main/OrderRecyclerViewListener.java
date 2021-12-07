package com.mahozi.sayed.talabiya.Feature.ui.order.view.main;

import com.mahozi.sayed.talabiya.Storage.order.OrderEntity;

public interface OrderRecyclerViewListener {

    void onClick(OrderEntity orderEntity);
    void onLongClick(OrderEntity orderEntity);


}
