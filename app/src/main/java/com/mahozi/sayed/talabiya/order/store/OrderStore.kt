package com.mahozi.sayed.talabiya.order.store

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderStore @Inject constructor(
    private val orderDao: OrderDao
) {
    fun insert(orderEntity: OrderEntity) {
        orderDao.insertOrder(orderEntity)
    }

    fun selectAllOrders(): Flow<List<OrderEntity>> {
        return orderDao.selectAllOrders()
    }

    fun getOrder(id: Int): Flow<OrderEntity> = orderDao.selectOrder(id)

    fun insertSubOrder(subOrderEntity: SubOrderEntity) {
        orderDao.insertSubOrder(subOrderEntity)
    }

    fun insertOrderItem(orderItemEntity: OrderItemEntity): Long {
        return orderDao.insertOrderItem(orderItemEntity)
    }

    fun selectOrderItemsWithOrderIdAndPerson(
        orderId: Int,
        personName: String
    ): LiveData<List<OrderItemEntity>> {
        return orderDao.selectOrderItemsWithOrderIdAndPerson(orderId, personName)
    }

    fun selectSubOrdersAndOrderItems(orderId: Int): LiveData<List<SubOrderAndOrderItems>> {
        return orderDao.selectSubOrdersAndOrderItems(orderId)
    }

    fun updateSuborder(subOrderEntity: SubOrderEntity) {
        orderDao.updateSuborder(subOrderEntity)
    }

    fun deleteSuborder(subOrderEntity: SubOrderEntity) {
        orderDao.deleteSuborder(subOrderEntity)
    }

    fun deleteOrderItem(orderItemEntity: OrderItemEntity) {
        orderDao.deleteOrderItem(orderItemEntity)
    }

    fun deleteOrder(orderEntity: OrderEntity) {
        orderDao.deleteOrder(orderEntity)
    }

    fun updateOrder(orderEntity: OrderEntity) {
        orderDao.updateOrder(orderEntity)
    }

    fun selectFullOrder(orderId: Int): List<FullOrderEntity> {
        val query = SimpleSQLiteQuery(
            """
    SELECT OrderItemEntity.menu_item_name, SUM(quantity) as quantity, note, category FROM OrderItemEntity 
    LEFT outer JOIN MenuItemEntity ON OrderItemEntity.menu_item_name = MenuItemEntity.item_name 
    AND MenuItemEntity.restaurant_name = OrderItemEntity.restaurant_name
    WHERE OrderItemEntity.order_id =  GROUP BY OrderItemEntity.menu_item_name, note ORDER BY category, menu_item_name ASC
    """.trimIndent(), arrayOf<Any>(orderId)
        )
        return orderDao.selectFullOrder(query)
    }

    fun selectPersonInfo(personName: String): List<OrderAndPersonSuborder> {
        return orderDao.selectUnpaidPersonInfo(personName)
    }

    fun updateOrderItem(orderItemEntity: OrderItemEntity) {
        orderDao.updateOrderItem(orderItemEntity)
    }

    fun updateSuborderStatus(date: String, status: Int, id: Long) {
        orderDao.updateSuborderStatus(date, status, id)
    }

    fun updateOrderStatus(date: String, status: Int, id: Long) {
        orderDao.updateOrderStatus(date, status, id)
    }

    fun selectAllPersonInfo(personName: String): List<OrderAndPersonSuborder> {
        return orderDao.selectAllPersonInfo(personName)
    }

    fun selectSubordersAndOrderItemsWithOrderIdAndName(
        menuItemName: String,
        orderId: Int
    ): List<SuborderAndorderItem> {
        return orderDao.selectSubordersAndOrderItemsWithOrderIdAndName(menuItemName, orderId)
    }
}