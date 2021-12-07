package com.mahozi.sayed.talabiya.Storage.order;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    void insertOrder(OrderEntity orderEntities);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insertOrderItem(OrderItemEntity orderItemEntity);

    @Query("SELECT * FROM OrderEntity ORDER BY date DESC")
    LiveData<List<OrderEntity>> selectAllOrders();




    @Insert
    void insertSubOrder(SubOrderEntity subOrderEntity);


    @Query("SELECT * FROM OrderItemEntity WHERE order_id = :orderId AND person_name = :personName ORDER BY menu_item_name")
    LiveData<List<OrderItemEntity>> selectOrderItemsWithOrderIdAndPerson(int orderId, String personName);

    @Query("SELECT * FROM SubOrderEntity WHERE order_id = :orderId ORDER BY id DESC")
    LiveData<List<SubOrderAndOrderItems>> selectSubOrdersAndOrderItems(int orderId);





    @Update
    void updateSuborder(SubOrderEntity subOrderEntity);

    @Delete
    void deleteSuborder(SubOrderEntity subOrderEntity);


    @Delete
    void deleteOrderItem(OrderItemEntity orderItemEntity);

    @Update
    void updateOrderItem(OrderItemEntity orderItemEntity);


    @Update
    void updateOrder(OrderEntity orderEntity);

    @Delete
    void deleteOrder(OrderEntity orderEntity);

    @RawQuery
    List<FullOrderEntity> selectFullOrder(SupportSQLiteQuery query);

    @Query("SELECT OrderItemEntity.id as OrderItemEntity_id, OrderItemEntity.order_id as OrderItemEntity_order_id, OrderItemEntity.person_name" +
            " as OrderItemEntity_person_name, OrderItemEntity.menu_item_name as OrderItemEntity_menu_item_name, OrderItemEntity.restaurant_name" +
            " as OrderItemEntity_restaurant_name, OrderItemEntity.total as OrderItemEntity_total, OrderItemEntity.note as OrderItemEntity_note, " +
            " OrderItemEntity.quantity as OrderItemEntity_quantity, OrderItemEntity.suborder_id as OrderItemEntity_suborder_id " +
            "" +
            ",SubOrderEntity.id as SuborderEntity_id, SuborderEntity.order_id as SuborderEntity_order_id, SuborderEntity.person_name as" +
            " SuborderEntity_person_name, SuborderEntity.total as SuborderEntity_total, SuborderEntity.payment_date as SuborderEntity_payment_date" +
            " , SuborderEntity.status as SuborderEntity_status  FROM OrderItemEntity " +
            "LEFT JOIN SubOrderEntity ON OrderItemEntity.suborder_id = SubOrderEntity.id " +
            "WHERE OrderItemEntity.menu_item_name = :menuItemName AND OrderItemEntity.order_id = :orderId")
    List<SuborderAndorderItem> selectSubordersAndOrderItemsWithOrderIdAndName(String menuItemName, int orderId);




    @Query("SELECT SubOrderEntity.*, payer,date, restaurant_name, order_total FROM SubOrderEntity \n" +
            "LEFT JOIN OrderEntity ON SubOrderEntity.order_id = OrderEntity.id WHERE  SubOrderEntity.status = 0 and SubOrderEntity.person_name = :personName\n" +
            "UNION\n" +
            "SELECT null,OrderEntity.id, 0, 0, 0, 0, OrderEntity.date, OrderEntity.restaurant_name, OrderEntity.payer, OrderEntity.order_total FROM OrderEntity \n" +
            "where NOT EXISTS (SELECT 1 FROM SubOrderEntity WHERE  OrderEntity.status = 0 and SubOrderEntity.order_id = OrderEntity.id \n" +
            "AND SubOrderEntity.person_name = :personName) AND payer = :personName ORDER BY date ASC")
    List<OrderAndPersonSuborder> selectUnpaidPersonInfo(String personName);


    @Query("SELECT SubOrderEntity.*, payer,date, restaurant_name, order_total FROM SubOrderEntity \n" +
            "LEFT JOIN OrderEntity ON SubOrderEntity.order_id = OrderEntity.id WHERE SubOrderEntity.person_name = :personName\n" +
            "UNION\n" +
            "SELECT null,OrderEntity.id, 0, 0, 0, 0, OrderEntity.date, OrderEntity.restaurant_name, OrderEntity.payer, OrderEntity.order_total FROM OrderEntity \n" +
            "where NOT EXISTS (SELECT 1 FROM SubOrderEntity WHERE  SubOrderEntity.order_id = OrderEntity.id \n" +
            "AND SubOrderEntity.person_name = :personName) AND payer = :personName ORDER BY date DESC")
    List<OrderAndPersonSuborder> selectAllPersonInfo(String personName);


    @Query("UPDATE SubOrderEntity SET status = :status, payment_date = :date where id = :id")
    void updateSuborderStatus(String date, int status, long id);

    @Query("UPDATE OrderEntity SET status = :status, clearance_date = :date where id = :id")
    void updateOrderStatus(String date, int status, long id);
}
