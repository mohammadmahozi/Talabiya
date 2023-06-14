package com.mahozi.sayed.talabiya.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mahozi.sayed.talabiya.resturant.store.MenuItemEntity;
import com.mahozi.sayed.talabiya.resturant.store.RestaurantStore;
import com.mahozi.sayed.talabiya.order.store.FullOrderEntity;
import com.mahozi.sayed.talabiya.order.store.OrderEntity;
import com.mahozi.sayed.talabiya.order.store.OrderItemEntity;
import com.mahozi.sayed.talabiya.order.store.OrderStore;
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

    private OrderStore mOrderStore;
    private RestaurantStore mRestaurantRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);

        //mOrderStore = new OrderStore(TalabiyaDatabase.getDatabase(application).orderDao());


        mRestaurantRepository = RestaurantStore.getInstance();
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
        mOrderStore.insert(orderEntity);
    }

    public List<String> selectRestaurantsNames(){
        return mRestaurantRepository.selectRestaurantsNames();
    }

    public void insertSubOrder(SubOrderEntity subOrderEntity){

        mOrderStore.insertSubOrder(subOrderEntity);

    }

    public void setSubOrders(int orderId) {
        mSubOrders = mOrderStore.selectSubOrdersAndOrderItems(orderId);
    }

    public LiveData<List<SubOrderAndOrderItems>> getSubOrders() {
        return mSubOrders;
    }

    public void setOrderItems(int orderId) {
        mOrderItems = mOrderStore.selectOrderItemsWithOrderIdAndPerson(orderId, "");
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
        return mOrderStore.insertOrderItem(orderItemEntity);
    }

    public LiveData<List<MenuItemEntity>> selectAllMenuItemsForCurrentRestaurant(){
        return mRestaurantRepository.selectAllMenuItems(currentOrderEntity.restaurantName);
    }

    public LiveData<List<OrderItemEntity>> selectOrderItemsWithOrderIdAndPerson(int orderId, String personName){

        return mOrderStore.selectOrderItemsWithOrderIdAndPerson(orderId, personName);
    }

    public LiveData<List<SubOrderAndOrderItems>> selectSubOrdersAndOrderItems(int orderId){
        return mOrderStore.selectSubOrdersAndOrderItems(orderId);
    }

    public void updateSuborder(SubOrderEntity subOrderEntity){
        mOrderStore.updateSuborder(subOrderEntity);
    }

    public void deleteSuborder(SubOrderEntity subOrderEntity){
        mOrderStore.deleteSuborder(subOrderEntity);
    }

    public void deleteMenuItem(MenuItemEntity menuItemEntity){
        mRestaurantRepository.deleteMenuItem(menuItemEntity);
    }

    public void deleteOrderItem(OrderItemEntity orderItemEntity){
        mOrderStore.deleteOrderItem(orderItemEntity);
    }

    public void deleteOrder(OrderEntity orderEntity){
        mOrderStore.deleteOrder(orderEntity);
    }

    public void updateOrder(OrderEntity orderEntity){
        mOrderStore.updateOrder(orderEntity);
    }

    public List<FullOrderEntity> selectFullOrder(int orderId){

        return mOrderStore.selectFullOrder(orderId);

    }

    public void updateOrderItem(OrderItemEntity orderItemEntity){
        mOrderStore.updateOrderItem(orderItemEntity);
    }

    public List<SuborderAndorderItem> selectSubordersAndOrderItemsWithOrderIdAndName(String menuItemName, int orderId){
        return mOrderStore.selectSubordersAndOrderItemsWithOrderIdAndName(menuItemName, orderId);
    }
}
