package com.lily.design.mvc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lily.design.R;
import com.lily.design.base.MBaseAdapter;
import com.lily.design.base.ViewHolder;
import com.lily.design.mvc.bean.DeliciousBean;

import java.util.List;

/***********
 * @Author rape flower
 * @Date 2017-04-05 13:10
 * @Describe 美食列表界面(MVC)
 */
public class TestAbAdapter extends MBaseAdapter<DeliciousBean>{

    public TestAbAdapter(Context context, List<DeliciousBean> data) {
        super(context, data);
    }

    @Override
    public int onBindLayout() {
        return R.layout.item_delicious;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, DeliciousBean data) {
        ((DeliciousHolder) holder).tvName.setText(data.name);
    }

    @Override
    public ViewHolder onCreateViewHolder() {
        return new DeliciousHolder();
    }

    private class DeliciousHolder extends ViewHolder{
        public TextView tvName;
        public View viewBottom;

        @Override
        public void initWidget(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_delicious_name);
            viewBottom = view.findViewById(R.id.view_bottom);
        }
    }
}
