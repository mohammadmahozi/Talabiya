package com.mahozi.sayed.talabiya.Feature.ui.order.view.orderitem;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahozi.sayed.talabiya.Domain.Order.Order;
import com.mahozi.sayed.talabiya.Domain.Order.OrderItem;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Restaurant.MenuItemEntity;
import com.mahozi.sayed.talabiya.Storage.order.OrderItemEntity;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class OrderItemSection<T> extends StatelessSection {

    String headerName;
    List<T> mDataList;

    List<T> mDataListCopy;

    T selectedItem;


    private SectionedRecyclerViewAdapter mAdapter;
    CreateOrderItemRecyclerViewListener mCreateOrderItemRecyclerViewListener;

    public OrderItemSection(String header, SectionedRecyclerViewAdapter adapter, CreateOrderItemRecyclerViewListener createOrderItemRecyclerViewListener) {
        // call constructor with layout resources for this SuborderSection header and items
        super(SectionParameters.builder().itemResourceId(R.layout.order_details_section_item)
                .headerResourceId(R.layout.recycler_view_order_item_header).build());

        this.headerName = header;
        mDataList = new ArrayList<>();
        mDataListCopy = new ArrayList<>();




        mAdapter = adapter;
        mCreateOrderItemRecyclerViewListener = createOrderItemRecyclerViewListener;
    }

    public T getItemAt(int pos){
        return mDataList.get(pos);
    }


    public void setDataList(List<T> mDataList) {
        this.mDataList = mDataList;
        mDataListCopy.addAll(mDataList);

    }

    public void add(T item){
        mDataList.add(item);
        mDataListCopy.add(item);

    }

    public void updateSelectedItem(T item){
        mDataList.add(item);
        mDataListCopy.add(item);

    }

    public void removeItemAt(int pos){
        mDataList.remove(pos);

    }

    public List<T> getDataList() {
        return mDataList;
    }

    @Override
    public int getContentItemsTotal() {
        return mDataList.size(); // number of items of this section
    }


    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new OrderItemSection.ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderItemSection.ItemViewHolder itemHolder = (OrderItemSection.ItemViewHolder) holder;


        holder.itemView.setTag(position);

        if (mDataList.get(position) instanceof OrderItemEntity){

            OrderItemEntity orderItemEntity = (OrderItemEntity) mDataList.get(position);
            itemHolder.foodQuantityTextView.setText(String.valueOf(orderItemEntity.quantity));
            itemHolder.foodNameTextView.setText(orderItemEntity.menuItemName);
            itemHolder.foodPriceTextView.setText(String.valueOf(orderItemEntity.total));
            itemHolder.foodNoteTextView.setText(orderItemEntity.note);


        }

        else {


            MenuItemEntity menuItemEntity = (MenuItemEntity) mDataList.get(position);

            itemHolder.foodNameTextView.setText(menuItemEntity.itemName);
            itemHolder.foodPriceTextView.setText(String.valueOf(menuItemEntity.price));
        }



    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new OrderItemSection.HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        OrderItemSection.HeaderViewHolder headerHolder = (OrderItemSection.HeaderViewHolder) holder;

        headerHolder.headerTextView.setText(headerName);



    }



    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView foodQuantityTextView;
        private TextView foodNameTextView;
        private TextView foodPriceTextView;
        private TextView foodNoteTextView;


        public ItemViewHolder(View itemView) {
            super(itemView);
            foodQuantityTextView = itemView.findViewById(R.id.suborder_item_quantity);
            foodNameTextView = itemView.findViewById(R.id.suborder_item_name);
            foodPriceTextView = itemView.findViewById(R.id.suborder_item_price);

            foodNoteTextView = itemView.findViewById(R.id.suborder_item_note);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedItem = mDataList.get((int) v.getTag());

                    mCreateOrderItemRecyclerViewListener.onClick(v);


                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCreateOrderItemRecyclerViewListener.onLongClick((Integer) itemView.getTag());
                    return true;
                }
            });


        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView headerTextView;

        HeaderViewHolder(View view) {
            super(view);

            headerTextView = view.findViewById(R.id.recycler_view_order_item_header);




        }
    }

    public void updateList(List<T> list){
        mDataList = list;
        mAdapter.notifyAllItemsChangedInSection(this);
    }


    public void filter(String text){

        mDataList.clear();
        if(text.isEmpty()){
            mDataList.addAll(mDataListCopy);
        } else{
            text = text.toLowerCase();
            for(T item: mDataListCopy){
                MenuItemEntity menuItemEntity = (MenuItemEntity) item;

                if(menuItemEntity.itemName.toLowerCase().contains(text)){
                    mDataList.add(item);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    public int indexOf(T item){

        if (item instanceof OrderItemEntity)
            return mDataList.indexOf(item);

        else {
            return -1;
        }
    }


    public T getByName(String name)  {

        for (T item: mDataList){
            if (item instanceof OrderItemEntity && ((OrderItemEntity) item).menuItemName.equals(name)){
                return item;
            }

            else if(item instanceof MenuItemEntity && ((MenuItemEntity) item).itemName.equals(name)){
                return item;
            }
        }

        return null;
    }






}
