package com.mahozi.sayed.talabiya.Storage.order;


import android.util.Log;

import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;
import com.mahozi.sayed.talabiya.Storage.Restaurant.MenuItemEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(

        indices = {@Index(value = {"person_name", "order_id", "restaurant_name", "menu_item_name"}, unique = true)},

        //primaryKeys = {"person_name", "order_id", "restaurant_name", "menu_item_name"},

        foreignKeys = {

        /*@ForeignKey(entity = PersonEntity.class,
                parentColumns = "name",
                childColumns = "person_name",
                onDelete = ForeignKey.CASCADE
        ),*/


        @ForeignKey(entity = OrderEntity.class,
                parentColumns = "id",
                childColumns = "order_id",
                onDelete = ForeignKey.CASCADE
        ),

        /*@ForeignKey(entity = MenuItemEntity.class,
                parentColumns = {"restaurant_name", "item_name"},
                childColumns = {"restaurant_name", "menu_item_name"},
                onDelete = ForeignKey.CASCADE
        ),*/

        @ForeignKey(entity = SubOrderEntity.class,
                parentColumns = "id",
                childColumns = "suborder_id",
                onDelete = ForeignKey.CASCADE
        )



})


public class OrderItemEntity {


    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id")
    public int id;

    @NonNull
    @ColumnInfo (name = "order_id")
    public int orderId;

    @NonNull
    @ColumnInfo (name = "suborder_id")
    public int suborderId;


    @NonNull
    @ColumnInfo (name = "person_name")
    public String personName;

    @NonNull
    @ColumnInfo (name = "restaurant_name")
    public String restaurantName;

    @NonNull
    @ColumnInfo (name = "menu_item_name")
    public String menuItemName;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "total")
    public double total;


    @ColumnInfo (name = "note")
    public String note;





    public OrderItemEntity(int orderId, int suborderId, String personName, String restaurantName, String menuItemName, int quantity, double total){

        this.orderId = orderId;
        this.suborderId = suborderId;
        this.personName = personName;
        this.restaurantName = restaurantName;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.total = total;

    }

    @Ignore
    public OrderItemEntity(int id, int orderId, int suborderId, @NonNull String personName, @NonNull String restaurantName, @NonNull String menuItemName, int quantity, double total, String note) {
        this.id = id;
        this.orderId = orderId;
        this.suborderId = suborderId;
        this.personName = personName;
        this.restaurantName = restaurantName;
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.total = total;
        this.note = note;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        return ((OrderItemEntity) obj).menuItemName.equals(menuItemName);
    }

    @Override
    public String toString() {
        return "OrderItemEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", suborderId=" + suborderId +
                ", personName='" + personName + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", menuItemName='" + menuItemName + '\'' +
                ", quantity=" + quantity +
                ", total=" + total +
                ", note='" + note + '\'' +
                '}';
    }


    public void updateQuantityAndTotal(int newQuantity){

        double priceOfOnePiece = total/quantity;

        quantity = newQuantity;

        total = priceOfOnePiece*quantity;

    }
}
