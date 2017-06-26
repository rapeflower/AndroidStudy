package com.lily.design.mvc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lily.design.R;
import com.lily.design.mvc.adapter.DeliciousAdapter;
import com.lily.design.mvc.bean.DeliciousBean;

import java.util.ArrayList;
import java.util.List;

/***********
 * @Author rape flower
 * @Date 2017-04-05 11:04
 * @Describe 美食列表界面(MVC)
 */
public class MVCActivity extends Activity{

    private ListView lv;

    private DeliciousAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvc_delicious);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        lv = (ListView) findViewById(R.id.lv_mvc_delicious);
        adapter = new DeliciousAdapter(MVCActivity.this);
        adapter.setDeliciousList(getDeliciousData());
        lv.setAdapter(adapter);

        iniListener();
    }

    /**
     * 设置监听
     */
    private void iniListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter != null) {
                    DeliciousBean de = (DeliciousBean) adapter.getItem(position);
                    Toast.makeText(MVCActivity.this, de.name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 获取数据
     * @return
     */
    private List<DeliciousBean> getDeliciousData() {
        List<DeliciousBean> list = new ArrayList<DeliciousBean>();
        list.add(new DeliciousBean("川贝枇杷膏"));
        list.add(new DeliciousBean("自制瓜子仁糖"));
        list.add(new DeliciousBean("香蕉芒果思慕雪"));
        list.add(new DeliciousBean("麻辣薄荷鸡丝"));
        list.add(new DeliciousBean("素鸡烧五花肉"));
        list.add(new DeliciousBean("翡翠茼蒿蛋糕"));
        list.add(new DeliciousBean("百香果果脯"));
        list.add(new DeliciousBean("野菜烧蘑菇"));
        list.add(new DeliciousBean("梅干菜牛蛙"));
        list.add(new DeliciousBean("韭菜炒蕨"));
        list.add(new DeliciousBean("自制瓜子仁糖"));
        list.add(new DeliciousBean("野菜烧蘑菇"));
        list.add(new DeliciousBean("芝麻椒香卷心菜"));
        list.add(new DeliciousBean("什锦小鸡素包"));

        return list;
    }
}
