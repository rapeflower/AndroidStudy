package com.lily.mvp.mode.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import com.lily.mvp.mode.R;
import com.lily.mvp.mode.core.base.BaseActivity;
import com.lily.mvp.mode.core.presenter.IPresenter;
import com.lily.mvp.mode.core.presenter.PresenterFactory;
import com.lily.mvp.mode.core.presenter.PresenterLoader;

public class MainActivity extends BaseActivity<TestPresenter> implements TestContract.TestView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected Loader<TestPresenter> createPresenterLoader(int id, @Nullable Bundle args) {
        return new PresenterLoader(this, new PresenterFactory<TestPresenter>() {
            @Override
            public TestPresenter create() {
                return new TestPresenter();
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    public void testSuccess(TestEntity entity) {

    }

    @Override
    public void testFailure(String errorMsg) {

    }
}
