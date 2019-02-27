package study.lily.mvpro.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import study.lily.mvpro.core.presenter.BaseFragmentActivityPresenter;

public class TestActivity extends BaseFragmentActivityPresenter<TestView> implements View.OnClickListener, AdapterView.OnItemClickListener {

    ArrayList<String> list;

    @Override
    public void beforeCreate(Bundle savedInstanceState) {
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("Test 第" + i + "字符串");
        }
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mView.initDataSource(list);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
