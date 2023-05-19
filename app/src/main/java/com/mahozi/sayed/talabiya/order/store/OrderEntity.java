package com.mahozi.sayed.talabiya.order.store;


import com.mahozi.sayed.talabiya.person.store.PersonEntity;
import com.mahozi.sayed.talabiya.resturant.store.RestaurantEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(foreignKeys = {

        @ForeignKey(entity = PersonEntity.class,
                parentColumns = "name",
                childColumns = "payer",
                onDelete = ForeignKey.CASCADE),


        @ForeignKey(entity = RestaurantEntity.class,
                parentColumns = "name",
                childColumns = "restaurant_name",
                onDelete = ForeignKey.CASCADE
        )})


public class OrderEntity {

    @NonNull
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id")
    public int id;

    @ColumnInfo(name = "restaurant_name")
    public String restaurantName;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "time")
    public String time;

    @Nullable
    @ColumnInfo(name = "payer")
    public String payer;

    @ColumnInfo(name = "order_total")
    public double total;

    @ColumnInfo(name = "status")
    public boolean status;

    @Nullable
    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "clearance_date")
    public String clearance_date;

    @ColumnInfo(name = "receipt_path")
    public String receiptPath;

    @ColumnInfo(name = "datetime")
    public Instant instant;


    public OrderEntity(String restaurantName, String date, String time){
        this.restaurantName = restaurantName;
        this.date = date;
        this.time = time;
    }

}
