package com.mahozi.sayed.talabiya.Feature.ui.person;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mahozi.sayed.talabiya.Feature.ui.BaseRecyclerAdapter;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.Person.PersonEntity;

public class PersonAdapter extends BaseRecyclerAdapter<PersonEntity> {


    private TextView personNameTextView;
    private TextView numOfUnpaidOrderTextView;

    private PersonRecyclerViewListener mPersonRecyclerViewListener;

    public PersonAdapter(PersonRecyclerViewListener personRecyclerViewListener) {

        mRecyclerItemLayoutResourceId = R.layout.person_recycler_item;

        mPersonRecyclerViewListener = personRecyclerViewListener;

    }

    /*@Override
    public void mapViews(View itemView) {

        //personNameTextView =  bind(holder.itemView, R.id.person_recycler_item_person_name_text_view);
        numOfUnpaidOrderTextView = bind(itemView,R.id.person_recycler_item_num_of_unpaid_order_text_view);
    }*/




    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.BaseViewHolder holder, PersonEntity model) {

        personNameTextView =  bind(holder.itemView, R.id.person_recycler_item_person_name_text_view);
        personNameTextView.setText(model.name);

    }

    @Override
    public void onItemClick(View view) {

        int position = (Integer) view.getTag();
        mPersonRecyclerViewListener.onClick(mDataList.get(position));
    }

    @Override
    public void onItemLongClick(View view) {
        int position = (Integer) view.getTag();
        mPersonRecyclerViewListener.onLongClick(mDataList.get(position));
    }
}
