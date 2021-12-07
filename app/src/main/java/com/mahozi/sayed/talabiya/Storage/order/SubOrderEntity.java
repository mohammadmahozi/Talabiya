package com.mahozi.sayed.talabiya.Storage.order;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;

@Entity(

        //primaryKeys = {"id", "person_name", "order_id"},

        indices = {@Index(value = {"person_name", "order_id"}, unique = true)},

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

        })

public class SubOrderEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id")
    public int id;

    @NonNull
    @ColumnInfo(name = "order_id")
    public int orderId;


    @NonNull
    @ColumnInfo (name = "person_name")
    public String personName;


    @ColumnInfo(name = "total")
    public double total;

    @NonNull
    @ColumnInfo (name = "status")
    public boolean status;

    @NonNull
    @ColumnInfo (name = "payment_date")
    public String paymentDate;

    public SubOrderEntity(int orderId, String personName){
        this.orderId = orderId;
        this.personName = personName;

        total = 0;
        status = false;
        paymentDate = "N/A";

    }
    @Ignore

    public SubOrderEntity(int id, int orderId, @NonNull String personName, double total, boolean status, @NonNull String paymentDate) {
        this.id = id;
        this.orderId = orderId;
        this.personName = personName;
        this.total = total;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "SubOrderEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", personName='" + personName + '\'' +
                ", total=" + total +
                ", status=" + status +
                ", paymentDate='" + paymentDate + '\'' +
                '}';
    }
}
