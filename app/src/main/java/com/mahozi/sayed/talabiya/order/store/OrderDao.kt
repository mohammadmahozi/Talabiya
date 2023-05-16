package com.mahozi.sayed.talabiya.order.store

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
  @Insert
  fun insertOrder(orderEntities: OrderEntity)

  @Query("SELECT * FROM OrderEntity WHERE id = :id")
  fun selectOrder(id: Int): Flow<OrderEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertOrderItem(orderItemEntity: OrderItemEntity): Long

  @Query("SELECT * FROM OrderEntity ORDER BY date DESC")
  fun selectAllOrders(): Flow<List<OrderEntity>>

  @Insert
  fun insertSubOrder(subOrderEntity: SubOrderEntity)

  @Query("SELECT * FROM OrderItemEntity WHERE order_id = :orderId AND person_name = :personName ORDER BY menu_item_name")
  fun selectOrderItemsWithOrderIdAndPerson(
    orderId: Int,
    personName: String
  ): LiveData<List<OrderItemEntity>>

  @Query("SELECT * FROM SubOrderEntity WHERE order_id = :orderId ORDER BY id DESC")
  fun selectSubOrdersAndOrderItems(orderId: Int): LiveData<List<SubOrderAndOrderItems>>

  @Update
  fun updateSuborder(subOrderEntity: SubOrderEntity)

  @Delete
  fun deleteSuborder(subOrderEntity: SubOrderEntity)

  @Delete
  fun deleteOrderItem(orderItemEntity: OrderItemEntity)

  @Update
  fun updateOrderItem(orderItemEntity: OrderItemEntity)

  @Update
  fun updateOrder(orderEntity: OrderEntity)

  @Delete
  fun deleteOrder(orderEntity: OrderEntity)

  @RawQuery
  fun selectFullOrder(query: SupportSQLiteQuery): List<FullOrderEntity>

  @Query(
    """SELECT OrderItemEntity.id as OrderItemEntity_id, OrderItemEntity.order_id as OrderItemEntity_order_id,
        OrderItemEntity.person_name as OrderItemEntity_person_name, 
        OrderItemEntity.menu_item_name as OrderItemEntity_menu_item_name, 
        OrderItemEntity.restaurant_name as OrderItemEntity_restaurant_name, 
        OrderItemEntity.total as OrderItemEntity_total, OrderItemEntity.note as OrderItemEntity_note, 
        OrderItemEntity.quantity as OrderItemEntity_quantity, OrderItemEntity.suborder_id as OrderItemEntity_suborder_id,
        SubOrderEntity.id as SuborderEntity_id, SuborderEntity.order_id as SuborderEntity_order_id, 
        SuborderEntity.person_name as SuborderEntity_person_name, SuborderEntity.total as SuborderEntity_total, 
        SuborderEntity.payment_date as SuborderEntity_payment_date , SuborderEntity.status as SuborderEntity_status 
        FROM OrderItemEntity LEFT JOIN SubOrderEntity ON OrderItemEntity.suborder_id = SubOrderEntity.id 
        WHERE OrderItemEntity.menu_item_name = :menuItemName AND OrderItemEntity.order_id = :orderId"""
  )
  fun selectSubordersAndOrderItemsWithOrderIdAndName(
    menuItemName: String,
    orderId: Int
  ): List<SuborderAndorderItem>

  @Query(
    """SELECT SubOrderEntity.*, payer,date, restaurant_name, order_total 
        FROM SubOrderEntity LEFT JOIN OrderEntity ON SubOrderEntity.order_id = OrderEntity.id 
        WHERE SubOrderEntity.status = 0 and SubOrderEntity.person_name = :personName
        UNION SELECT null,OrderEntity.id, 0, 0, 0, 0, OrderEntity.date, OrderEntity.restaurant_name, OrderEntity.payer, 
        OrderEntity.order_total 
        FROM OrderEntity 
        where NOT EXISTS (SELECT 1 FROM SubOrderEntity WHERE  OrderEntity.status = 0 and SubOrderEntity.order_id = OrderEntity.id 
        AND SubOrderEntity.person_name = :personName) AND payer = :personName ORDER BY date ASC"""
  )
  fun selectUnpaidPersonInfo(personName: String): List<OrderAndPersonSuborder>

  @Query(
    """SELECT SubOrderEntity.*, payer,date, restaurant_name, order_total FROM SubOrderEntity 
        LEFT JOIN OrderEntity ON SubOrderEntity.order_id = OrderEntity.id WHERE SubOrderEntity.person_name = :personName
        UNION
        SELECT null,OrderEntity.id, 0, 0, 0, 0, OrderEntity.date, OrderEntity.restaurant_name, OrderEntity.payer, OrderEntity.order_total FROM OrderEntity 
        where NOT EXISTS (SELECT 1 FROM SubOrderEntity WHERE  SubOrderEntity.order_id = OrderEntity.id 
        AND SubOrderEntity.person_name = :personName) AND payer = :personName ORDER BY date DESC"""
  )
  fun selectAllPersonInfo(personName: String): List<OrderAndPersonSuborder>

  @Query("UPDATE SubOrderEntity SET status = :status, payment_date = :date where id = :id")
  fun updateSuborderStatus(date: String, status: Int, id: Long)

  @Query("UPDATE OrderEntity SET status = :status, clearance_date = :date where id = :id")
  fun updateOrderStatus(date: String, status: Int, id: Long)
}