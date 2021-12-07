package com.mahozi.sayed.talabiya.Storage.Restaurant;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        //primaryKeys = {"restaurant_name", "item_name"},

        indices = {@Index(value = {"restaurant_name", "item_name"}, unique = true)},

        foreignKeys = {

        @ForeignKey(entity = RestaurantEntity.class,
                parentColumns = "name",
                childColumns = "restaurant_name",
                onDelete = ForeignKey.CASCADE
        )

})
public class MenuItemEntity{


    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id")
    public int id;


    @NonNull
    @ColumnInfo (name = "restaurant_name")
    public String restaurantName;

    @NonNull
    @ColumnInfo (name = "item_name")
    public String itemName;

    @ColumnInfo (name = "price")
    public double price;

    @ColumnInfo (name = "category")
    public int category;



    public MenuItemEntity(String restaurantName, String itemName, double price, int category){

        this.restaurantName = restaurantName;
        this.category = category;
        this.itemName = itemName;
        this.price = price;

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((MenuItemEntity) obj).itemName.equals(itemName);
    }
}
