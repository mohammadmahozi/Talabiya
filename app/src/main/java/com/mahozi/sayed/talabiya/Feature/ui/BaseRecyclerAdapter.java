package com.mahozi.sayed.talabiya.Feature.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahozi.sayed.talabiya.R;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder>{


    protected int mRecyclerItemLayoutResourceId;

    ArrayList<Integer> mSelectedItems;
    protected List<T> mDataList;

    protected boolean mIsActionModeOn;

    public class BaseViewHolder extends RecyclerView.ViewHolder{

        public BaseViewHolder(View itemView){
            super(itemView);


            //mapViews(itemView);

        }

    }

    //public abstract void mapViews(View itemView);

    public <T extends View> T bind(View itemView, int id) {
        return itemView.findViewById(id);
    }




    public BaseRecyclerAdapter(){


        mSelectedItems = new ArrayList();

        mDataList = new ArrayList<>();
        mIsActionModeOn = false;

    }


    @NonNull
    @Override
    public BaseRecyclerAdapter.BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View itemView = LayoutInflater.from(parent.getContext()).inflate(mRecyclerItemLayoutResourceId, parent, false);
        BaseRecyclerAdapter.BaseViewHolder baseViewHolder = new BaseRecyclerAdapter.BaseViewHolder(itemView);

        baseViewHolder.itemView.setOnClickListener(mOnClickListener);
        baseViewHolder.itemView.setOnLongClickListener(mOnLongClickListener);

        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerAdapter.BaseViewHolder holder, int position) {

        holder.itemView.setBackgroundColor(mSelectedItems.contains(position)? Color.GRAY : Color.TRANSPARENT);

        if (mSelectedItems.contains(position)){
            holder.itemView.setBackgroundColor(Color.GRAY);
        }

        else {

            holder.itemView.setBackgroundResource(R.drawable.ripple);
        }

        holder.itemView.setTag(position);
        //Log.v("vvvvpos", position + "");
        //Log.v("vvvvsize", mDataList.size() + "");

        onBindViewHolder(holder, mDataList.get(position));

    }

    public abstract void onBindViewHolder(@NonNull BaseRecyclerAdapter.BaseViewHolder holder, T model);


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setDataList(List<T> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();

    }

    private View.OnLongClickListener mOnLongClickListener  = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {

           itemSelection(view);


           onItemLongClick(view);
            return true;


        }
    };

    protected View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (mIsActionModeOn){

                itemSelection(view);

            }

            else {
                onItemClick(view);
            }
        }
    };

    private void itemSelection(View view){

        mIsActionModeOn = true;

        int selectedItemPosition = (Integer)view.getTag();


        if (mSelectedItems.contains(selectedItemPosition)){
            mSelectedItems.remove(Integer.valueOf(selectedItemPosition));

            view.setBackgroundResource(R.drawable.ripple);

        }

        else {
            mSelectedItems.add(selectedItemPosition);
            view.setBackgroundResource(R.color.colorRipple);

        }

    }


    public void finishSelectionSession(){

        mIsActionModeOn = false;

        mSelectedItems.clear();

        notifyDataSetChanged();

    }

    public ArrayList<Integer> getSelectedItems(){
        return mSelectedItems;
    }

    public abstract void onItemClick(View view);

    public abstract void onItemLongClick(View view);

    public T get(int pos){
      return mDataList.get(pos);
    }


    public boolean getIsActionModeOn(){
        return mIsActionModeOn;
    }

}
