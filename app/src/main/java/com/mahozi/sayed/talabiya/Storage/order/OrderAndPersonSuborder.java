package com.mahozi.sayed.talabiya.Storage.order;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class OrderAndPersonSuborder {

    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "order_id")
    public int orderId;


    @ColumnInfo (name = "person_name")
    public String personName;


    @ColumnInfo(name = "total")
    public double total;

    @ColumnInfo (name = "status")
    public boolean status;

    @ColumnInfo (name = "payment_date")
    public String paymentDate;

    @ColumnInfo(name = "date")
    public String date;


    @ColumnInfo(name = "payer")
    public String payer;

    @ColumnInfo(name = "order_total")
    public double orderTotal;

    @ColumnInfo(name = "restaurant_name")
    public String restaurantName;

}
