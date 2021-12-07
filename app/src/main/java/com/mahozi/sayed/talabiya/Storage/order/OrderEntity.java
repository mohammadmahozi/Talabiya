package com.mahozi.sayed.talabiya.Storage.order;


import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantEntity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name = "payer")
    public String payer;

    @ColumnInfo(name = "order_total")
    public double total;

    @ColumnInfo(name = "status")
    public boolean status;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "clearance_date")
    public String clearance_date;

    @ColumnInfo(name = "receipt_path")
    public String receiptPath;


    public OrderEntity(String restaurantName, String date, String time){
        this.restaurantName = restaurantName;
        this.date = date;
        this.time = time;
    }

}
