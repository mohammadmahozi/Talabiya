package com.mahozi.sayed.talabiya.Storage.order;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.mahozi.sayed.talabiya.Storage.TalabiyaDatabase;

import java.util.List;

public class OrderRepository {

    private static volatile OrderRepository morderRepository;

    private OrderDao morderDao;


    private OrderRepository(){



    }

    public static OrderRepository getInstance(){

        if(morderRepository == null)
            morderRepository = new OrderRepository();

        return morderRepository;
    }

    public void init(Application application){
        TalabiyaDatabase talabiyaDatabase = TalabiyaDatabase.getDatabase(application);
        morderDao = talabiyaDatabase.orderDao();

    }

    public void insert(OrderEntity orderEntity){
        morderDao.insertOrder(orderEntity);
    }

    public LiveData<List<OrderEntity>> selectAllOrders(){
        return morderDao.selectAllOrders();
    }

    public void insertSubOrder(SubOrderEntity subOrderEntity){
        morderDao.insertSubOrder(subOrderEntity);
    }



    public long insertOrderItem(OrderItemEntity orderItemEntity){
        return morderDao.insertOrderItem(orderItemEntity);
    }


    public LiveData<List<OrderItemEntity>> selectOrderItemsWithOrderIdAndPerson(int orderId, String personName){

        return morderDao.selectOrderItemsWithOrderIdAndPerson(orderId, personName);
    }

    public LiveData<List<SubOrderAndOrderItems>> selectSubOrdersAndOrderItems(int orderId){
        return morderDao.selectSubOrdersAndOrderItems(orderId);
    }

    public void updateSuborder(SubOrderEntity subOrderEntity){
        morderDao.updateSuborder(subOrderEntity);
    }

    public void deleteSuborder(SubOrderEntity subOrderEntity){
        morderDao.deleteSuborder(subOrderEntity);
    }


    public void deleteOrderItem(OrderItemEntity orderItemEntity){
        morderDao.deleteOrderItem(orderItemEntity);
    }

    public void deleteOrder(OrderEntity orderEntity){
        morderDao.deleteOrder(orderEntity);
    }

    public void updateOrder(OrderEntity orderEntity){
        morderDao.updateOrder(orderEntity);
    }


    public List<FullOrderEntity> selectFullOrder(int orderId){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT OrderItemEntity.menu_item_name, SUM(quantity) as quantity, note, category FROM OrderItemEntity \n" +
                "LEFT outer JOIN MenuItemEntity ON OrderItemEntity.menu_item_name = MenuItemEntity.item_name \n" +
                "AND MenuItemEntity.restaurant_name = OrderItemEntity.restaurant_name\n" +
                "WHERE OrderItemEntity.order_id = ? GROUP BY OrderItemEntity.menu_item_name, note ORDER BY category, menu_item_name ASC",
                new Object[]{orderId});

        return morderDao.selectFullOrder(query);
    }

    public List<OrderAndPersonSuborder> selectPersonInfo(String personName){
        return morderDao.selectUnpaidPersonInfo(personName);
    }

    public void updateOrderItem(OrderItemEntity orderItemEntity){
        morderDao.updateOrderItem(orderItemEntity);
    }



    public void updateSuborderStatus(String date, int status, long id){

        morderDao.updateSuborderStatus(date, status, id);
    }

    public void updateOrderStatus(String date, int status, long id){

        morderDao.updateOrderStatus(date, status, id);
    }


    public List<OrderAndPersonSuborder>  selectAllPersonInfo(String personName){

        return morderDao.selectAllPersonInfo(personName);
    }

    public List<SuborderAndorderItem> selectSubordersAndOrderItemsWithOrderIdAndName(String menuItemName, int orderId){

        return morderDao.selectSubordersAndOrderItemsWithOrderIdAndName(menuItemName, orderId);
    }




}
