package com.mahozi.sayed.talabiya.order.list.ui;

import com.mahozi.sayed.talabiya.order.store.OrderEntity;

public interface OrderRecyclerViewListener {

    void onClick(OrderEntity orderEntity);
    void onLongClick(OrderEntity orderEntity);


}
