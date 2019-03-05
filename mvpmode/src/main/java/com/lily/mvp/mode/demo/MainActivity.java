package com.lily.mvp.mode.demo;

import android.os.Bundle;

import com.lily.mvp.mode.R;
import com.lily.mvp.mode.core.base.BaseActivity;

public class MainActivity extends BaseActivity<TestPresenter> implements TestContract.TestView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected TestPresenter createPresenter() {
        return new TestPresenter();
    }

    @Override
    protected void init() {

    }

    @Override
    public void showSuccess(TestEntity entity) {

    }

    @Override
    public void showFailure(String errorMsg) {

    }
}
