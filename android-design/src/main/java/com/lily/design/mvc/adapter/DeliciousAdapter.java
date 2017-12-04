package com.lily.design.mvc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lily.design.R;
import com.lily.design.mvc.bean.DeliciousBean;

import java.util.List;

/***********
 * @Author rape flower
 * @Date 2017-04-05 13:10
 * @Describe 美食列表界面(MVC)
 */
public class DeliciousAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    List<DeliciousBean> deliciousList;

    public DeliciousAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 设置数据源
     * @param deliciousList
     */
    public void setDeliciousList(List<DeliciousBean> deliciousList) {
        this.deliciousList = deliciousList;
    }

    @Override
    public int getCount() {
        return deliciousList == null ? 0 : deliciousList.size();
    }

    @Override
    public Object getItem(int position) {
        return deliciousList == null ? null: deliciousList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeliciousHolder holder = null;
        if (convertView == null) {
            holder = new DeliciousHolder();
            convertView = inflater.inflate(R.layout.item_delicious, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_delicious_name);
            holder.viewBottom = convertView.findViewById(R.id.view_bottom);
            convertView.setTag(holder);
        } else {
            holder = (DeliciousHolder) convertView.getTag();
        }

        DeliciousBean delicious = deliciousList.get(position);
        holder.tvName.setText(delicious.name);

        final DeliciousHolder finalHolder = holder;
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.viewBottom.setVisibility(View.VISIBLE);
            }
        });

        return convertView;
    }

    private class DeliciousHolder {
        public TextView tvName;
        public View viewBottom;
    }
}
