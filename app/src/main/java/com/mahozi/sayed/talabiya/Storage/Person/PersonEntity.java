package com.mahozi.sayed.talabiya.Storage.Person;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(    indices = {@Index(value = {"name"}, unique = true)}
)
public class PersonEntity {



    @NonNull
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @NonNull
    @ColumnInfo
    public String name;

    public PersonEntity(String name){
        this.name = name;
    }
}
