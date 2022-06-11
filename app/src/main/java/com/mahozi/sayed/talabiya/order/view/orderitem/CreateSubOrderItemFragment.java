package com.mahozi.sayed.talabiya.order.view.orderitem;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.order.OrderViewModel;
import com.mahozi.sayed.talabiya.order.store.OrderEntity;
import com.mahozi.sayed.talabiya.order.store.OrderItemEntity;
import com.mahozi.sayed.talabiya.order.store.SubOrderEntity;
import com.mahozi.sayed.talabiya.order.store.SuborderAndorderItem;
import com.mahozi.sayed.talabiya.resturant.store.MenuItemEntity;
import com.mahozi.sayed.talabiya.resturant.view.detail.CreateFoodFragment;
import com.mahozi.sayed.talabiya.resturant.view.detail.FoodLis;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class CreateSubOrderItemFragment extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;
    private RecyclerView mRecyclerView;
    private OrderItemSection<OrderItemEntity> selectedItemsSections;
    OrderItemSection<MenuItemEntity> menuSection;

    private OrderViewModel orderViewModel;



    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;



    private final int TYPE_ORDER_ITEM = 0;
    private final int TYPE_MENU_ITEM = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_create_suborder_item, container, false);



        setHasOptionsMenu(true);



        mRecyclerView = view.findViewById(R.id.fragment_suborder_item_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);



        // Create an instance of SectionedRecyclerViewAdapter
        sectionAdapter = new SectionedRecyclerViewAdapter();

        // Add your Sections
        selectedItemsSections = new OrderItemSection("Selected",sectionAdapter, new CreateOrderItemRecyclerViewListener() {
            @Override
            public void onClick(View view) {

                showQuantityDialog(selectedItemsSections.selectedItem.quantity, "", 1);



            }

            @Override
            public void onLongClick(int position){
                showContextMenu(TYPE_ORDER_ITEM, position);


            }
        });
        sectionAdapter.addSection("Selected", selectedItemsSections);


        menuSection = new OrderItemSection("Menu", sectionAdapter, new CreateOrderItemRecyclerViewListener() {
            @Override
            public void onClick(View view) {
                showQuantityDialog(1, "", 0);

            }

            @Override
            public void onLongClick(int position){

                showContextMenu(TYPE_MENU_ITEM, position);

            }
        });
        sectionAdapter.addSection("Menu", menuSection);



        orderViewModel.selectAllMenuItemsForCurrentRestaurant().observe(this, new Observer<List<MenuItemEntity>>() {
            @Override
            public void onChanged(List<MenuItemEntity> menuItemEntities) {
                menuSection.setDataList(menuItemEntities);

                //for (OrderItemEntity selectedItems: selectedItemsSections.mDataList ){
                //    menuItemEntities.remove(new MenuItemEntity("", selectedItems.menuItemName, 0, 0));
                //}

                sectionAdapter.notifyDataSetChanged();
            }
        });

        orderViewModel.selectOrderItemsWithOrderIdAndPerson(orderViewModel.getCurrentOrder().id, orderViewModel.getCurrentSubOrder().personName)
                .observe(this, new Observer<List<OrderItemEntity>>() {
                    @Override
                    public void onChanged(List<OrderItemEntity> orderItemEntities) {

                        selectedItemsSections.setDataList(orderItemEntities);
                        sectionAdapter.notifyDataSetChanged();


                    }
                });

        // Set up your RecyclerView with the SectionedRecyclerViewAdapter
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(sectionAdapter);


        FloatingActionButton addOrderFab = view.findViewById(R.id.add_menu_item_fab);
        addOrderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateFoodFragment();
            }
        });




        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(orderViewModel.getCurrentSubOrder().personName + " order");

    }

    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }



    public void showQuantityDialog(int previousQuantity, String previousNote, int type) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Quantity");
        alertDialog.setMessage("Enter Quantity");

        final EditText editText = new EditText(getActivity());
        editText.setText(String.valueOf(previousQuantity));
        editText.setKeyListener(new DigitsKeyListener());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(lp);
        editText.setSelectAllOnFocus(true);

        alertDialog.setView(editText);

        showKeyboard();

        alertDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final int quantity = Integer.parseInt(editText.getText().toString());

                //TODO change name
                if (type == 0)
                    createOrderItem(quantity, null);

                else
                    updateOrderItem(quantity, null);

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            }
        });

        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            }
        });

        alertDialog.setNeutralButton(R.string.note, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final int quantity = Integer.parseInt(editText.getText().toString());


                showNoteDialog(previousNote, quantity, type);
            }
        });

        alertDialog.show();

    }



    private OrderItemEntity createOrderItem(int quantity, String note){


        MenuItemEntity selectedMenuItem = menuSection.selectedItem;


        OrderEntity orderEntity = orderViewModel.getCurrentOrder();
        SubOrderEntity currentSuborderEntity = orderViewModel.getCurrentSubOrder();


        OrderItemEntity matchingOrderItemEntity = selectedItemsSections.getByName(selectedMenuItem.itemName);
        if (matchingOrderItemEntity != null){

            //remove the total of the previous orderItem from the suborder
            currentSuborderEntity.total = currentSuborderEntity.total - matchingOrderItemEntity.total;
            orderEntity.total = orderEntity.total - matchingOrderItemEntity.total;

            matchingOrderItemEntity.updateQuantityAndTotal(quantity);

            if (note != null)
                matchingOrderItemEntity.note = note;

            orderViewModel.updateOrderItem(matchingOrderItemEntity);
        }

        else {

            matchingOrderItemEntity = new OrderItemEntity(orderEntity.id, currentSuborderEntity.id,  currentSuborderEntity.personName,
                    orderEntity.restaurantName, selectedMenuItem.itemName, quantity, selectedMenuItem.price*quantity);

            if (note != null)
                matchingOrderItemEntity.note = note;

            matchingOrderItemEntity.id = (int)orderViewModel.insertOrderItem(matchingOrderItemEntity);
        }


        currentSuborderEntity.total = currentSuborderEntity.total + selectedMenuItem.price*quantity;
        orderEntity.total = orderEntity.total + selectedMenuItem.price*quantity;


        orderViewModel.updateSuborder(currentSuborderEntity);



        orderViewModel.updateOrder(orderEntity);


        return matchingOrderItemEntity;

    }


    private void updateOrderItem(int quantity, String note){


        OrderItemEntity selectedOrderItem = selectedItemsSections.selectedItem;


        OrderEntity orderEntity = orderViewModel.getCurrentOrder();
        SubOrderEntity currentSuborderEntity = orderViewModel.getCurrentSubOrder();


        //remove the total of the previous orderItem from the suborder
        currentSuborderEntity.total = currentSuborderEntity.total - selectedOrderItem.total;
        orderEntity.total = orderEntity.total - selectedOrderItem.total;


        selectedOrderItem.updateQuantityAndTotal(quantity);

        if (note != null)
            selectedOrderItem.note = note;


        orderViewModel.updateOrderItem(selectedOrderItem);

        currentSuborderEntity.total = currentSuborderEntity.total + selectedOrderItem.total;
        orderEntity.total = orderEntity.total + selectedOrderItem.total;



        orderViewModel.updateSuborder(currentSuborderEntity);
        orderViewModel.updateOrder(orderEntity);


    }



    private void showNoteDialog(String previousNote, int quantity, int type){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.note);
        alertDialog.setMessage(R.string.enter_note);

        final EditText editText = new EditText(getActivity());
        editText.setText(previousNote);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(lp);
        //editText.setSelectAllOnFocus(true);
        editText.setSelection(previousNote.length());
        alertDialog.setView(editText);
        //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        alertDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final String note = editText.getText().toString().trim();

                //TODO change name
                if (type == 0)
                    createOrderItem(quantity, note);

                else
                    updateOrderItem(quantity, note);

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            }
        });

        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            }
        });



        alertDialog.show();


    }

    int CREATE_MENU_ITEM_REQUEST_CODE = 1;

    public void startCreateFoodFragment(){


        CreateFoodFragment createFoodFragment = new CreateFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("createMenuItem", true);
        bundle.putString("restaurantName", orderViewModel.getCurrentOrder().restaurantName);
        createFoodFragment.t1 = this;
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.order_container, createFoodFragment)
                .commit();
    }

    public FoodLis lis = new FoodLis() {
        @Override
        public void onCreateMenuItem(@NonNull String name, @NonNull String oldName, double price) {
            if (oldName.equals("")) {
                menuSection.selectedItem = new MenuItemEntity("", name, price, 0);
                showQuantityDialog(1, "", 0);
            } else {

                OrderEntity orderEntity = orderViewModel.getCurrentOrder();

                List<SuborderAndorderItem> subOrderAndOrderItems = orderViewModel
                        .selectSubordersAndOrderItemsWithOrderIdAndName(oldName, orderEntity.id);

                String updatedSubordersNames = "";

                for (SuborderAndorderItem subOrderAndOrderItem: subOrderAndOrderItems){
                    SubOrderEntity subOrderEntity = subOrderAndOrderItem.subOrderEntity;
                    OrderItemEntity orderItemEntity = subOrderAndOrderItem.orderItemEntity;

                    //remove the old price of the order item from the order and suborder totals
                    subOrderEntity.total = subOrderEntity.total - orderItemEntity.total;
                    orderEntity.total = orderEntity.total - orderItemEntity.total;

                    //update order item with the new price
                    orderItemEntity.menuItemName = name;
                    orderItemEntity.total = orderItemEntity.quantity * price;

                    //add the new price of the order item from the order and suborder totals
                    subOrderEntity.total = subOrderEntity.total + orderItemEntity.total;
                    orderEntity.total = orderEntity.total + orderItemEntity.total;

                    //update database
                    orderViewModel.updateOrderItem(orderItemEntity);
                    orderViewModel.updateSuborder(subOrderEntity);
                    orderViewModel.updateOrder(orderEntity);

                    updatedSubordersNames = updatedSubordersNames + subOrderEntity.personName + ", ";
                }

                if (!updatedSubordersNames.equals("")){
                    updatedSubordersNames = name + " name and price updated in "
                            + updatedSubordersNames.substring(0, updatedSubordersNames.length()-1) + " orders and menu";
                } else {
                    updatedSubordersNames = name + " price updated in menu";
                }

                Toast.makeText(getContext(), updatedSubordersNames, Toast.LENGTH_LONG).show();
            }
            }


    };


    private void showContextMenu(int type, int position){
        CharSequence[] items = new CharSequence[2];
        if (type == TYPE_MENU_ITEM){

            items[0] = "Edit";
            items[1] = "Delete";

        }

        else {

            items[0] = "Change price";
            items[1] = "Delete";


        }



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //builder.setTitle(subOrderEntity.personName + " order");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                onItemSelected(item, type, position);

            }
        });
        builder.show();
    }

    public void onItemSelected(int item, int type, int position){



        if (item == 0){

            if (type == TYPE_MENU_ITEM){


                editMenuItem(menuSection.mDataList.get(position));


            }

            else if (type == TYPE_ORDER_ITEM){

                OrderItemEntity orderItemEntity = selectedItemsSections.getItemAt(position);

                /*showQuantityDialog(-1, new MenuItemEntity("", orderItemEntity.menuItemName,
                        orderItemEntity.total/orderItemEntity.quantity,0), orderItemEntity.note, orderItemEntity.quantity);*/

                changeOrderItemPrice(orderItemEntity);

            }

        }


        else if (item == 1){


            if (type == TYPE_MENU_ITEM){
                MenuItemEntity selectedItem = menuSection.getItemAt(position);


                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm").setMessage("Delete " + selectedItem.itemName + "from the menu?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                orderViewModel.deleteMenuItem(selectedItem);

                                menuSection.mDataList.remove(position);
                                sectionAdapter.notifyItemRemovedFromSection(menuSection, position);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }

            else if(type == TYPE_ORDER_ITEM){

                OrderItemEntity selectedItem = selectedItemsSections.getItemAt(position);
                SubOrderEntity currentSuborder = orderViewModel.getCurrentSubOrder();
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirm").setMessage("Delete " + selectedItem.menuItemName + " from " +
                        currentSuborder.personName + " order?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                orderViewModel.deleteOrderItem(selectedItem);
                                selectedItemsSections.mDataList.remove(position);
                                sectionAdapter.notifyItemRemovedFromSection(selectedItemsSections, position);


                                currentSuborder.total = currentSuborder.total - selectedItem.total;
                                orderViewModel.updateSuborder(currentSuborder);

                                OrderEntity orderEntity = orderViewModel.getCurrentOrder();
                                orderEntity.total = orderEntity.total - selectedItem.total;

                                orderViewModel.updateOrder(orderEntity);

                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }

        }
    }


    int EDIT_MENU_ITEM_REQUEST_CODE = 2;
    public void editMenuItem(MenuItemEntity menuItemEntity){

        CreateFoodFragment createFoodFragment = new CreateFoodFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("editMenuItem", true);
        bundle.putString("restaurantName", orderViewModel.getCurrentOrder().restaurantName);
        bundle.putLong("id", menuItemEntity.id);
        bundle.putString("menuItem", menuItemEntity.itemName);
        bundle.putDouble("price", menuItemEntity.price);
        bundle.putInt("category", menuItemEntity.category);
        createFoodFragment.t1 = this;
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.order_container, createFoodFragment)
                .commit();
    }


    private void changeOrderItemPrice(OrderItemEntity orderItemEntity){


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Change price");
        alertDialog.setMessage("Enter new price (price for 1 item only)");

        final EditText editText = new EditText(getActivity());
        editText.setText(String.valueOf(orderItemEntity.total/orderItemEntity.quantity));
        editText.setKeyListener(new DigitsKeyListener());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(lp);
        editText.setSelectAllOnFocus(true);


        //CheckBox changeMenuPrice = new CheckBox(getActivity());
        //changeMenuPrice.setText("Change price in menu");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);


        layout.addView(editText);
        //layout.addView(changeMenuPrice);

        alertDialog.setView(layout);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        alertDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                final double price = Double.parseDouble(editText.getText().toString());

                OrderEntity orderEntity = orderViewModel.getCurrentOrder();


                List<SuborderAndorderItem> subOrderAndOrderItems = orderViewModel
                        .selectSubordersAndOrderItemsWithOrderIdAndName(orderItemEntity.menuItemName, orderEntity.id);


                String updatedSubordersNames = "";

                for (SuborderAndorderItem subOrderAndOrderItem: subOrderAndOrderItems){


                    SubOrderEntity subOrderEntity = subOrderAndOrderItem.subOrderEntity;


                    OrderItemEntity orderItemEntity = subOrderAndOrderItem.orderItemEntity;


                    //remove the old price of the order item from the order and suborder totals
                    subOrderEntity.total = subOrderEntity.total - orderItemEntity.total;
                    orderEntity.total = orderEntity.total - orderItemEntity.total;


                    //update order item with the new price
                    orderItemEntity.total = orderItemEntity.quantity * price;

                    //add the new price of the order item from the order and suborder totals
                    subOrderEntity.total = subOrderEntity.total + orderItemEntity.total;
                    orderEntity.total = orderEntity.total + orderItemEntity.total;

                    //update database
                    orderViewModel.updateOrderItem(orderItemEntity);
                    orderViewModel.updateSuborder(subOrderEntity);
                    orderViewModel.updateOrder(orderEntity);


                    updatedSubordersNames = updatedSubordersNames + subOrderEntity.personName + ", ";
                }

            }
        });

        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            }
        });



        alertDialog.show();
    }









    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.create_suborder_item_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    menuSection.filter(newText);


                    mRecyclerView.smoothScrollToPosition(sectionAdapter.getSectionPosition("Menu"));
                    mRecyclerView.scrollBy(0, 500);
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }


    public boolean onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return true;
        }

        return false;
    }
}
