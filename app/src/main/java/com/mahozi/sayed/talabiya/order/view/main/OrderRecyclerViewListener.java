package com.mahozi.sayed.talabiya.order.view.main;

import com.mahozi.sayed.talabiya.order.store.OrderEntity;

public interface OrderRecyclerViewListener {

    void onClick(OrderEntity orderEntity);
    void onLongClick(OrderEntity orderEntity);


}
