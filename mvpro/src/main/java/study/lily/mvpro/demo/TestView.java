package study.lily.mvpro.demo;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import study.lily.mvpro.R;
import study.lily.mvpro.core.utils.EventHelper;
import study.lily.mvpro.core.view.BaseView;

public class TestView extends BaseView {

    private ListView listView;
    private ArrayAdapter<String> mAdapter;

    @Override
    public int bindLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void inflateComplete() {
        listView = findViewById(R.id.lv);
    }

    @Override
    public void bindEvent() {
        EventHelper.itemClick(mPresenter, listView);
    }

    public void initDataSource(ArrayList<String> data) {
        mAdapter = new ArrayAdapter<String>(mRootView.getContext(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(mAdapter);
    }

    public void addData(ArrayList<String> data) {
        mAdapter.addAll(data);
    }
}
