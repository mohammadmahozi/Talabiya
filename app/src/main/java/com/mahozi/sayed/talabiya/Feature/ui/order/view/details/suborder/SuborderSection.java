package com.mahozi.sayed.talabiya.Feature.ui.order.view.details.suborder;

import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderItemEntity;
import com.mahozi.sayed.talabiya.Storage.order.SubOrderEntity;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class SuborderSection extends StatelessSection {


    private SubOrderEntity subOrderEntity;
    private List<OrderItemEntity> mDataList;

    SectionedRecyclerViewAdapter mAdapter;
    boolean expanded = false;

    private SubordersRecyclerViewListener mSubordersRecyclerViewListener;

    public SuborderSection(SubOrderEntity subOrderEntity, SectionedRecyclerViewAdapter adapter, SubordersRecyclerViewListener subordersRecyclerViewListener) {
        // call constructor with layout resources for this SuborderSection header and items
        super(SectionParameters.builder().itemResourceId(R.layout.order_details_section_item)
                .headerResourceId(R.layout.order_details_section_header).footerResourceId(R.layout.order_details_section_footer).build());

        this.subOrderEntity = subOrderEntity;

        mAdapter = adapter;

        mDataList = new ArrayList<>();

        mSubordersRecyclerViewListener = subordersRecyclerViewListener;
    }

    public void setDataList(List<OrderItemEntity> mDataList) {
        this.mDataList = mDataList;
    }

    public List<OrderItemEntity> getDataList() {
        return mDataList;
    }

    @Override
    public int getContentItemsTotal() {
        return expanded? mDataList.size(): 0; // number of items of this section
    }


    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        if (expanded){
            itemHolder.itemView.setVisibility(View.VISIBLE);

            itemHolder.foodQuantityTextView.setText(String.valueOf(mDataList.get(position).quantity));
            itemHolder.foodNameTextView.setText(mDataList.get(position).menuItemName);
            itemHolder.foodPriceTextView.setText(String.valueOf(mDataList.get(position).total));



        }

        else {
            itemHolder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.headerTextView.setText(subOrderEntity.personName);






    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView foodQuantityTextView;
        private TextView foodNameTextView;
        private TextView foodPriceTextView;


        public ItemViewHolder(View itemView) {
            super(itemView);
            foodQuantityTextView = itemView.findViewById(R.id.suborder_item_quantity);
            foodNameTextView = itemView.findViewById(R.id.suborder_item_name);
            foodPriceTextView = itemView.findViewById(R.id.suborder_item_price);

        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView headerTextView;
        private final ImageButton addOrderItemButton;

        HeaderViewHolder(View view) {
            super(view);

            headerTextView = view.findViewById(R.id.suborder_header);
            addOrderItemButton = view.findViewById(R.id.add_order_item_button);
            addOrderItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSubordersRecyclerViewListener.onAddOrderItemButtonClick(subOrderEntity);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expanded = !expanded;
                    mAdapter.notifyDataSetChanged();
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                   mSubordersRecyclerViewListener.onLongClick(subOrderEntity);
                    return true;
                }
            });




        }
    }

    @Override
    public RecyclerView.ViewHolder getFooterViewHolder(View view) {
        return new FooterViewHolder(view);
    }

    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        FooterViewHolder footerHolder = (FooterViewHolder) holder;
        footerHolder.footerTotal.setText(String.valueOf(subOrderEntity.total));

        footerHolder.footerCheckBox.setChecked(subOrderEntity.status);
        footerHolder.footerCheckBox.setText(subOrderEntity.paymentDate);


    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        private final TextView footerTotal;
        private final CheckBox footerCheckBox;



        FooterViewHolder(View view) {
            super(view);

            footerTotal = view.findViewById(R.id.suborder_section_footer_total);
            footerCheckBox = view.findViewById(R.id.suborder_section_footer_payment_status);

            /*footerCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSubordersRecyclerViewListener.onStatusCheckBoxClick((CheckBox) v, subOrderEntity);
                }
            });*/

            /*footerCheckBox.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        mSubordersRecyclerViewListener.onStatusCheckBoxClick((CheckBox) v, subOrderEntity);


                    return true;
                    }
                    return false;
                }
            });*/

            footerCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSubordersRecyclerViewListener.onStatusCheckBoxClick((CheckBox) v, subOrderEntity);

                }
            });


        }
    }
}
