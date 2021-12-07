package com.mahozi.sayed.talabiya.Feature.ui.order;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mahozi.sayed.talabiya.Storage.Restaurant.MenuItemEntity;
import com.mahozi.sayed.talabiya.Storage.Restaurant.RestaurantRepository;
import com.mahozi.sayed.talabiya.Storage.order.FullOrderEntity;
import com.mahozi.sayed.talabiya.Storage.order.OrderEntity;
import com.mahozi.sayed.talabiya.Storage.order.OrderItemEntity;
import com.mahozi.sayed.talabiya.Storage.order.OrderRepository;
import com.mahozi.sayed.talabiya.Storage.order.SubOrderAndOrderItems;
import com.mahozi.sayed.talabiya.Storage.order.SubOrderEntity;
import com.mahozi.sayed.talabiya.Storage.order.SuborderAndorderItem;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private LiveData<List<OrderEntity>> mAllOrderEntities;
    private LiveData<List<SubOrderAndOrderItems>> mSubOrders;

    private LiveData<List<OrderItemEntity>> mOrderItems;

    private OrderEntity currentOrderEntity;
    private String currentRestaurantName;
    private SubOrderEntity currentSubOrderEntity;





    private OrderRepository mOrderRepository;
    private RestaurantRepository mRestaurantRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);

        mOrderRepository = OrderRepository.getInstance();
        mOrderRepository.init(application);


        mRestaurantRepository = RestaurantRepository.getInstance();
        mRestaurantRepository.init(application);

        mAllOrderEntities = mOrderRepository.selectAllOrders();
    }

    public OrderEntity getCurrentOrder() {
        return currentOrderEntity;
    }

    public void setCurrentOrderEntity(OrderEntity currentOrderEntity) {
        this.currentOrderEntity = currentOrderEntity;
    }

    public SubOrderEntity getCurrentSubOrder() {
        return currentSubOrderEntity;
    }

    public void setCurrentSubOrder(SubOrderEntity SubOrderEntity) {
        this.currentSubOrderEntity = SubOrderEntity;
    }

    public LiveData<List<OrderEntity>> getAllOrderEntities() {
        return mAllOrderEntities;
    }

    public void insertOrder(OrderEntity orderEntity){
        mOrderRepository.insert(orderEntity);
    }

    public List<String> selectRestaurantsNames(){
        return mRestaurantRepository.selectRestaurantsNames();
    }

    public void insertSubOrder(SubOrderEntity subOrderEntity){

        mOrderRepository.insertSubOrder(subOrderEntity);

    }

    public void setSubOrders(int orderId) {
        mSubOrders = mOrderRepository.selectSubOrdersAndOrderItems(orderId);
    }

    public LiveData<List<SubOrderAndOrderItems>> getSubOrders() {
        return mSubOrders;
    }

    public void setOrderItems(int orderId) {
        mOrderItems = mOrderRepository.selectOrderItemsWithOrderIdAndPerson(orderId, "");
    }

    public LiveData<List<OrderItemEntity>> getOrderItems() {
        return mOrderItems;
    }

    public void setCurrentRestaurantName(String currentRestaurantName) {
        this.currentRestaurantName = currentRestaurantName;
    }

    public String getCurrentRestaurantName() {
        return currentRestaurantName;
    }

    public long insertOrderItem(OrderItemEntity orderItemEntity){
        return mOrderRepository.insertOrderItem(orderItemEntity);
    }

    public LiveData<List<MenuItemEntity>> selectAllMenuItemsForCurrentRestaurant(){
        return mRestaurantRepository.selectAllMenuItems(currentOrderEntity.restaurantName);
    }

    public LiveData<List<OrderItemEntity>> selectOrderItemsWithOrderIdAndPerson(int orderId, String personName){

        return mOrderRepository.selectOrderItemsWithOrderIdAndPerson(orderId, personName);
    }

    public LiveData<List<SubOrderAndOrderItems>> selectSubOrdersAndOrderItems(int orderId){
        return mOrderRepository.selectSubOrdersAndOrderItems(orderId);
    }

    public void updateSuborder(SubOrderEntity subOrderEntity){
        mOrderRepository.updateSuborder(subOrderEntity);
    }

    public void deleteSuborder(SubOrderEntity subOrderEntity){
        mOrderRepository.deleteSuborder(subOrderEntity);
    }

    public void deleteMenuItem(MenuItemEntity menuItemEntity){
        mRestaurantRepository.deleteMenuItem(menuItemEntity);
    }

    public void deleteOrderItem(OrderItemEntity orderItemEntity){
        mOrderRepository.deleteOrderItem(orderItemEntity);
    }

    public void deleteOrder(OrderEntity orderEntity){
        mOrderRepository.deleteOrder(orderEntity);
    }

    public void updateOrder(OrderEntity orderEntity){
        mOrderRepository.updateOrder(orderEntity);
    }

    public List<FullOrderEntity> selectFullOrder(int orderId){

        return mOrderRepository.selectFullOrder(orderId);

    }

    public void updateOrderItem(OrderItemEntity orderItemEntity){
        mOrderRepository.updateOrderItem(orderItemEntity);
    }

    public List<SuborderAndorderItem> selectSubordersAndOrderItemsWithOrderIdAndName(String menuItemName, int orderId){
        return mOrderRepository.selectSubordersAndOrderItemsWithOrderIdAndName(menuItemName, orderId);
    }
}
