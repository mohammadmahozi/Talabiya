package com.mahozi.sayed.talabiya.Feature.ui.order.view.details.suborder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahozi.sayed.talabiya.Feature.ui.order.OrderViewModel;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.create.DatePickerPopUp;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.orderitem.CreateSubOrderItemFragment;
import com.mahozi.sayed.talabiya.Feature.ui.person.PersonActivity;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderEntity;
import com.mahozi.sayed.talabiya.Storage.order.SubOrderAndOrderItems;
import com.mahozi.sayed.talabiya.Storage.order.SubOrderEntity;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class SubordersFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private OrderViewModel orderViewModel;



    private SectionedRecyclerViewAdapter orderDetailsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_suborders, container, false);


        mRecyclerView = view.findViewById(R.id.fragment_order_details_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);


        orderDetailsAdapter = new SectionedRecyclerViewAdapter();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(orderDetailsAdapter);


        orderViewModel.setSubOrders(orderViewModel.getCurrentOrder().id);
        orderViewModel.getSubOrders().observe(this, new Observer<List<SubOrderAndOrderItems>>() {
            @Override
            public void onChanged(List<SubOrderAndOrderItems> subOrderAndOrderItems) {
                for (SubOrderAndOrderItems subOrderAndOrderItem : subOrderAndOrderItems){
                    if (orderDetailsAdapter.getSection(subOrderAndOrderItem.subOrderEntity.personName) == null){

                        addNewSection(subOrderAndOrderItem.subOrderEntity);

                    }

                    ((SuborderSection) orderDetailsAdapter.getSection(subOrderAndOrderItem.subOrderEntity.personName)).setDataList(subOrderAndOrderItem.orderItemEntities);


                }
            }
        });



        FloatingActionButton addSubOrderFab = view.findViewById(R.id.add_suborder_fab);
        addSubOrderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPersonFragment();
            }
        });




        return view;
    }

    private void addNewSection(SubOrderEntity subOrderEntity){

        orderDetailsAdapter.addSection(subOrderEntity.personName, new SuborderSection(subOrderEntity, orderDetailsAdapter,
                new SubordersRecyclerViewListener() {
            @Override
            public void onAddOrderItemButtonClick(SubOrderEntity subOrderEntity) {
                startCreateSubOrderItemFragment(subOrderEntity);
            }

            @Override
            public void onLongClick(SubOrderEntity subOrderEntity) {

                showContextMenu(subOrderEntity);


            }


            @Override
            public void onClick() {

            }

            @Override
            public void onStatusCheckBoxClick(CheckBox statusCheckBox, SubOrderEntity subOrderEntity1){

                //if it was checked and got unchecked. the condition happens after the click and the chnage
                if (!statusCheckBox.isChecked()){

                    statusCheckBox.setChecked(true);

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm").setMessage("Change status to not payed? This will delete the payment date.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    subOrderEntity.status = false;
                                    subOrderEntity.paymentDate = "Not payed";
                                    orderViewModel.updateSuborder(subOrderEntity);



                                    statusCheckBox.setText("Not payed");
                                    statusCheckBox.setChecked(false);



                                }})
                            .setNegativeButton(android.R.string.no, null).show();




                }

                else {
                    statusCheckBox.setChecked(false);

                    DatePickerPopUp datePick = new DatePickerPopUp(new DatePickerPopUp.OnDateReceived() {
                        @Override
                        public void getDate(String date) {

                            subOrderEntity.status = true;
                            subOrderEntity.paymentDate = date;
                            orderViewModel.updateSuborder(subOrderEntity);

                            statusCheckBox.setChecked(true);
                            statusCheckBox.setText(date);



                        }
                    });

                    datePick.show(getActivity().getSupportFragmentManager(), "DatePicker");


                }


            }


        }));



        int sectionPos = orderDetailsAdapter.getSectionPosition(subOrderEntity.personName);
        orderDetailsAdapter.notifyItemInserted(sectionPos);
    }

    public void startPersonFragment(){
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra("getPerson", true);
        startActivityForResult(intent, 1);
    }


    public void startCreateSubOrderItemFragment(SubOrderEntity subOrderEntity){

        CreateSubOrderItemFragment createSubOrderItemFragment = new CreateSubOrderItemFragment();
        orderViewModel.setCurrentSubOrder(subOrderEntity);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.order_container, createSubOrderItemFragment, "CreateSubOrderItemFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                String personName = data.getStringExtra("personName");

                if (orderDetailsAdapter.getSection(personName) == null){

                    SubOrderEntity subOrderEntity = new SubOrderEntity(orderViewModel.getCurrentOrder().id, personName);
                    orderViewModel.insertSubOrder(subOrderEntity);
                }

                else {
                    Toast.makeText(getActivity(), personName + "is already added", Toast.LENGTH_LONG).show();
                }

            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    private void showContextMenu(SubOrderEntity subOrderEntity){

        final CharSequence[] items = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(subOrderEntity.personName + " order");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                onItemSelected(item, subOrderEntity);

            }
        });
        builder.show();
    }



    private void onItemSelected(int item, SubOrderEntity subOrderEntity){

        if (item == 0){


        }

        else if (item == 1){

            new AlertDialog.Builder(getActivity())
                    .setTitle("Confirm").setMessage("Delete " + subOrderEntity.personName + " order?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            OrderEntity orderEntity = orderViewModel.getCurrentOrder();

                            orderEntity.total = orderEntity.total - subOrderEntity.total;
                            orderViewModel.updateOrder(orderEntity);

                            orderViewModel.deleteSuborder(subOrderEntity);

                            orderDetailsAdapter.removeSection(orderDetailsAdapter.getSection(subOrderEntity.personName));
                            orderDetailsAdapter.notifyDataSetChanged();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();

        }
    }



}
