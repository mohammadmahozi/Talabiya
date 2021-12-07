package com.mahozi.sayed.talabiya.Storage.order;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SubOrderAndOrderItems {

    @Embedded
    public SubOrderEntity subOrderEntity;

    @Relation(parentColumn = "id", entityColumn = "suborder_id", entity = OrderItemEntity.class)
    public List<OrderItemEntity> orderItemEntities;

    @NonNull
    @Override
    public String toString() {

        String s = subOrderEntity.toString();
        for (OrderItemEntity orderItemEntity: orderItemEntities)
            s = s + "\n" + orderItemEntity.toString();

        return s;
    }
}
