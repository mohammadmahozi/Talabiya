package com.mahozi.sayed.talabiya.Feature.ui.order.view.main;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mahozi.sayed.talabiya.Feature.ui.BaseRecyclerAdapter;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderEntity;

import androidx.annotation.NonNull;

public class OrderAdapter extends BaseRecyclerAdapter<OrderEntity> {


    private TextView idTextView;
    private TextView restaurantNameTextView;
    private TextView dateTextView;
    //private TextView payerTextView;
    //private TextView totalTextView;

    //private TextView statusTextView;

    private CheckBox statusCheckBox;


    private OrderRecyclerViewListener mOrderRecyclerViewListener;

    public OrderAdapter(OrderRecyclerViewListener orderRecyclerViewListener){

        mRecyclerItemLayoutResourceId = R.layout.order_recycler_view_item;
        mOrderRecyclerViewListener = orderRecyclerViewListener;
    }


    /*@Override
    public void mapViews(View itemView) {

        idTextView = bind(itemView, R.id.order_recycler_item_id_text_view);
        restaurantNameTextView =  bind(itemView, R.id.order_recycler_item_restaurant_name_text_view);
        dateTextView =  bind(itemView, R.id.order_recycler_item_date_text_view);
        payerTextView =  bind(itemView, R.id.order_recycler_item_payer_text_view);
        totalTextView = bind(itemView, R.id.order_recycler_item_total_text_view);

        statusCheckBox = bind(itemView, R.id.order_recycler_item_status_check_box);
    }*/



    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.BaseViewHolder holder, OrderEntity model) {

        idTextView = bind(holder.itemView, R.id.order_recycler_item_id_text_view);
        restaurantNameTextView =  bind(holder.itemView, R.id.order_recycler_item_restaurant_name_text_view);
        dateTextView =  bind(holder.itemView, R.id.order_recycler_item_date_text_view);

        //statusTextView = bind(holder.itemView, R.id.order_recycler_item_restaurant_status_text_view);
        //payerTextView =  bind(holder.itemView, R.id.order_recycler_item_payer_text_view);
        //totalTextView = bind(holder.itemView, R.id.order_recycler_item_total_text_view);

        statusCheckBox = bind(holder.itemView, R.id.order_recycler_item_status_check_box);




        idTextView.setText(String.valueOf(model.id));
        restaurantNameTextView.setText(model.restaurantName);
        dateTextView.setText(model.date.replace("\n       ", ", "));
        //statusTextView.setText(model.clearance_date);
        //payerTextView.setText(model.payer == null? "Not Selected": model.payer);
        //totalTextView.setText(String.valueOf(model.total));

        statusCheckBox.setChecked(model.status);
    }



    @Override
    public void onItemClick(View view) {

        int position = (Integer) view.getTag();
        mOrderRecyclerViewListener.onClick(mDataList.get(position));
    }



    @Override
    public void onItemLongClick(View view) {
        int position = (Integer) view.getTag();
        mOrderRecyclerViewListener.onLongClick(mDataList.get(position));
    }
}
