package com.mahozi.sayed.talabiya.Feature.ui.order.view.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahozi.sayed.talabiya.Feature.ui.order.OrderViewModel;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.create.CreateOrderFragment;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.create.DatePickerPopUp;
import com.mahozi.sayed.talabiya.Feature.ui.order.view.details.OrderDetailsFragment;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.OrderEntity;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private RecyclerView.LayoutManager layoutManager;

    FloatingActionButton addOrderFab;

    OrderViewModel orderViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);


        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);

        mRecyclerView = view.findViewById(R.id.fragment_order_recycler_view);
        //this add a separator
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);



        mOrderAdapter = new OrderAdapter(new OrderRecyclerViewListener() {
            @Override
            public void onClick(OrderEntity orderEntity) {

                orderViewModel.setCurrentOrderEntity(orderEntity);
                startOrderDetailsFragment();
            }

            @Override
            public void onLongClick(OrderEntity orderEntity) {


                OrderActionMode orderActionMode = new OrderActionMode(new OrderActionMode.OnSelectionActionMode() {
                    @Override
                    public void finished() {

                        mOrderAdapter.finishSelectionSession();
                    }

                    @Override
                    public void delete(ActionMode actionMode) {

                        deleteOrder(actionMode);

                    }

                    @Override
                    public void changeStatus(ActionMode mode) {

                        changeOrderStatus(mode);
                    }
                });


                getActivity().startActionMode(orderActionMode);

            }
        });
        mRecyclerView.setAdapter(mOrderAdapter);


        orderViewModel.getAllOrderEntities().observe(this, new Observer<List<OrderEntity>>() {
            @Override
            public void onChanged(List<OrderEntity> orderEntities) {
                mOrderAdapter.setDataList(orderEntities);
            }
        });


        FloatingActionButton addOrderFab = view.findViewById(R.id.add_order_fab);
        addOrderFab.setOnClickListener(view1 -> { if (!mOrderAdapter.getIsActionModeOn())startCreateOrderFragment(); });

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Orders");

    }

    public void startCreateOrderFragment(){

        CreateOrderFragment createOrderFragment = new CreateOrderFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.order_container, createOrderFragment, "CreateOrderFragment")
                .addToBackStack(null)
                .commit();
    }

    public void startOrderDetailsFragment(){

        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.order_container, orderDetailsFragment, "OrderDetailsFragment")
                .addToBackStack(null)
                .commit();


    }

    public void deleteOrder(ActionMode actionMode){

        ArrayList<Integer> selectedItemsPositions = new ArrayList<>(mOrderAdapter.getSelectedItems());

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm).setMessage(getString(R.string.delete_all_these, selectedItemsPositions.size() + ""))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        for (int pos: selectedItemsPositions){


                            orderViewModel.deleteOrder(mOrderAdapter.get(pos));

                        }
                        actionMode.finish();
                        mOrderAdapter.finishSelectionSession();



                    }})
                .setNegativeButton(android.R.string.no, null).show();



    }


    public void changeOrderStatus(ActionMode actionMode){

        ArrayList<Integer> selectedItemsPositions = new ArrayList<>(mOrderAdapter.getSelectedItems());

        DatePickerPopUp datePick = new DatePickerPopUp(new DatePickerPopUp.OnDateReceived() {
            @Override
            public void getDate(String date) {

                for (int pos: selectedItemsPositions){

                    OrderEntity orderEntity = mOrderAdapter.get(pos);

                    if (orderEntity.status == false){

                        orderEntity.clearance_date = date;
                        orderEntity.status = true;

                        orderViewModel.updateOrder(orderEntity);
                    }



                }

                actionMode.finish();

            }
        });

        datePick.show(getActivity().getSupportFragmentManager(), "DatePicker");

    }


}
