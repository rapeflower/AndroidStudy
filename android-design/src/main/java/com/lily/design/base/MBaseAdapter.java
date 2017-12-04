package com.lily.design.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/***********
 * @Author rape flower
 * @Date 2017-12-04 16:48
 * @Describe 通用数据适配器类
 */
public abstract class MBaseAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mData;
	protected LayoutInflater mInflater;

	public MBaseAdapter(Context context, List<T> data) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData == null ? (T) null : mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(onBindLayout(), parent, false);
			holder = onCreateViewHolder();
			holder.initWidget(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		onBindViewHolder(holder, getItem(position));
		return convertView;
	}

	public abstract int onBindLayout();

	public abstract void onBindViewHolder(ViewHolder holder, T data);

	public abstract ViewHolder onCreateViewHolder();
}
