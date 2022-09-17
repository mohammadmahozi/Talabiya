package com.mahozi.sayed.talabiya.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mahozi.sayed.talabiya.core.data.TalabiyaDatabase;
import com.mahozi.sayed.talabiya.resturant.store.MenuItemEntity;
import com.mahozi.sayed.talabiya.resturant.store.RestaurantRepository;
import com.mahozi.sayed.talabiya.order.store.FullOrderEntity;
import com.mahozi.sayed.talabiya.order.store.OrderEntity;
import com.mahozi.sayed.talabiya.order.store.OrderItemEntity;
import com.mahozi.sayed.talabiya.order.store.OrderRepository;
import com.mahozi.sayed.talabiya.order.store.SubOrderAndOrderItems;
import com.mahozi.sayed.talabiya.order.store.SubOrderEntity;
import com.mahozi.sayed.talabiya.order.store.SuborderAndorderItem;

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

        mOrderRepository = new OrderRepository(TalabiyaDatabase.getDatabase(application).orderDao());


        mRestaurantRepository = RestaurantRepository.getInstance();
        mRestaurantRepository.init(application);

        //mAllOrderEntities = mOrderRepository.selectAllOrders();
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
