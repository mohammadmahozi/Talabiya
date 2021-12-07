package com.mahozi.sayed.talabiya.Storage.order;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.PrimaryKey;

public class SuborderAndorderItem {
/*
    @ColumnInfo(name = "SubOrderEntity.id")
    public int id;

    @ColumnInfo(name = "order_id")
    public int orderId;


    @ColumnInfo (name = "SubOrderEntitySubOrderEntity.person_name")
    public String personName;


    @ColumnInfo(name = "SubOrderEntity.total")
    public double total;

    @ColumnInfo (name = "status")
    public boolean status;

    @ColumnInfo (name = "payment_date")
    public String paymentDate;


    @ColumnInfo (name = "id")
    public int orderItemid;


    @ColumnInfo (name = "suborder_id")
    public int suborderId;


    @ColumnInfo (name = "person_name")
    public String orderItempersonName;

    @ColumnInfo (name = "restaurant_name")
    public String restaurantName;

    @ColumnInfo (name = "menu_item_name")
    public String menuItemName;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "total")
    public double orderItemtotal;


    @ColumnInfo (name = "note")
    public String note;*/

    @Embedded(prefix = "SuborderEntity_")
    public SubOrderEntity subOrderEntity;

    @Embedded(prefix = "OrderItemEntity_")
    public OrderItemEntity orderItemEntity;

    @Override
    public String toString() {
        return "SuborderAndorderItem{" +
                "subOrderEntity=" + subOrderEntity +
                ", orderItemEntity=" + orderItemEntity +
                '}';
    }
}
