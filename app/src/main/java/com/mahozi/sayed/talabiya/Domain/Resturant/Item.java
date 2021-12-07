package com.mahozi.sayed.talabiya.Domain.Resturant;

public class Item {

    protected final int PASTRY = 0;
    protected final int PLATE = 1;
    protected final int SANDWICH = 2;
    protected final int BURGER = 3;
    protected final int APPETIZER = 4;
    protected final int DRINKS = 5;


    private String mName;
    private double mPrice;
    private int mCategory;


    Item(String name, double price, int category){

        mName = name;
        mPrice = price;
        mCategory = category;
    }


}
