package com.android.lily.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

public abstract class KeyboardSearchBaseAdapter extends RecyclerView.Adapter<KeyboardSearchBaseAdapter.BaseViewHolder> {

    public Context mContext;
    public Resources mResources;
    public LayoutInflater mLayoutInflater;
    protected List mData;
    protected View.OnClickListener itemClickListener;

    public KeyboardSearchBaseAdapter(Context context,List data) {
        this.mContext = context;
        this.mResources = context.getResources();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public List getAdapterData() {
        return mData;
    }

    public void setAdapterData(List data) {
        this.mData = data;
    }

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setTag(mData != null ? mData.get(position) : null);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
            if (itemClickListener != null) {
                itemView.setOnClickListener(itemClickListener);
            }
        }

        protected void injectView() {

        }
    }
}
