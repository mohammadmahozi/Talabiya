package com.mahozi.sayed.talabiya.Feature.ui.person.details;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mahozi.sayed.talabiya.Feature.ui.BaseRecyclerAdapter;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderAndPersonSuborder;

public class FragmentPersonSubordersAdapter extends BaseRecyclerAdapter<OrderAndPersonSuborder> {


    private TextView orderNumberTextView;
    private TextView restaurantNameTextView;
    private TextView orderDateTextView;
    private TextView totalPaidTextView;
    private TextView suborderTotalTextView;
    private TextView grandTotalTextView;

    private CheckBox statusCheckBox;

    private Context context;

    private PersonSubordersListener mPersonSubordersListener;


    public FragmentPersonSubordersAdapter(Context context, PersonSubordersListener personSubordersListener){

        mRecyclerItemLayoutResourceId = R.layout.person_suborder_fragment_recycler_item;
        mPersonSubordersListener = personSubordersListener;


        if (statusCheckBox != null){


        }
        this.context = context;
    }




    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.BaseViewHolder holder, OrderAndPersonSuborder model) {


        orderNumberTextView =  bind(holder.itemView, R.id.person_suborder_fragment_order_number);
        restaurantNameTextView =  bind(holder.itemView, R.id.person_suborder_fragment_restaurant_name);
        orderDateTextView =  bind(holder.itemView, R.id.person_suborder_fragment_order_date);
        totalPaidTextView =  bind(holder.itemView, R.id.person_suborder_fragment_total_paid);
        suborderTotalTextView =  bind(holder.itemView, R.id.person_suborder_fragment_suborder_total);
        grandTotalTextView =  bind(holder.itemView, R.id.person_suborder_fragment_grand_total);
        statusCheckBox = bind(holder.itemView, R.id.person_suborder_fragment_status_checkbox);

        orderNumberTextView.setText(String.valueOf(model.orderId));
        restaurantNameTextView .setText(model.restaurantName);
        orderDateTextView.setText(model.date.replace("\n       ", ", "));

        suborderTotalTextView.setText(String.valueOf(model.total));

        statusCheckBox.setChecked(model.status);
        statusCheckBox.setText(model.paymentDate);
        ///statusCheckBox.setTag(holder.itemView.getTag());
        statusCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =  (Integer) holder.itemView.getTag();

                mPersonSubordersListener.onStatusCheckBoxClick((CheckBox) v, position, model);
            }
        });


        double grandTotal;
        if (model.payer != null && model.payer.equals(model.personName)){
            totalPaidTextView.setText(String.valueOf(model.orderTotal));

            grandTotal = model.orderTotal - model.total;
            grandTotalTextView.setText(grandTotal < 0? context.getString(R.string.debit, grandTotal): context.getString(R.string.credit, grandTotal));

        }

        else {
            totalPaidTextView.setText("0");
            grandTotalTextView.setText(context.getString(R.string.debit, model.total));

        }









    }

    @Override
    public void onItemClick(View view) {

        int position = (Integer) view.getTag();
        mPersonSubordersListener.onClick(mDataList.get(position));
    }


    @Override
    public void onItemLongClick(View view) {
        int position = (Integer) view.getTag();
    }
}
