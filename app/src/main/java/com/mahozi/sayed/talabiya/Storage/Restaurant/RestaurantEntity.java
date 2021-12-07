package com.mahozi.sayed.talabiya.Storage.Restaurant;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class RestaurantEntity {


    @NonNull
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    public RestaurantEntity(String name){
        this.name = name;
    }
}
