package com.mahozi.sayed.talabiya.Storage.order;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class FullOrderEntity {

    @ColumnInfo(name = "menu_item_name")
    public String menuItemName;

    @ColumnInfo(name = "quantity")
    public int quantity;


    @ColumnInfo (name = "note")
    public String note;

    @ColumnInfo(name = "category")
    public int category;


    @NonNull
    @Override
    public String toString() {

        if (note == null || note.equals("")){


            return  quantity  + " " + menuItemName;

        }
        Log.v("vvvvn", menuItemName + " " + quantity);

        return quantity  + " " + menuItemName  + " (" + note + ")";
    }
}
