package com.mahozi.sayed.talabiya.Feature.ui.order.view.details.full;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.mahozi.sayed.talabiya.Feature.ui.order.OrderViewModel;
import com.mahozi.sayed.talabiya.R;
import com.mahozi.sayed.talabiya.Storage.order.FullOrderEntity;

import java.util.List;

public class FullOrderFragment extends Fragment {

    private TextView fullOrderTextView;

    OrderViewModel mOrderViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_order, container, false);

        mOrderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);


        fullOrderTextView = view.findViewById(R.id.full_order_text_view);

        List<FullOrderEntity> fullOrderEntityList = mOrderViewModel.selectFullOrder(mOrderViewModel.getCurrentOrder().id);




        String fullOrder = "";
        for (FullOrderEntity fullOrderEntity: fullOrderEntityList){

            fullOrder = fullOrder + System.getProperty("line.separator") + fullOrderEntity.toString();

        }




        fullOrderTextView.setMaxLines(fullOrderEntityList.size());
        fullOrderTextView.setText(fullOrder.length() > 1? fullOrder.substring(1): fullOrder);//remove the first line separator


        return view;
    }

}
