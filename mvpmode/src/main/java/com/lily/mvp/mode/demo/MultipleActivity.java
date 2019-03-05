package com.lily.mvp.mode.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lily.mvp.mode.core.base.BaseActivity;
import com.lily.mvp.mode.core.base.ExpandPresenter;

/**
 * 单页面多网络请求
 */
public class MultipleActivity extends BaseActivity<ExpandPresenter> implements TestContract.TestView, ExContract.ExView {

    private TestPresenter testPresenter;
    private ExPresenter exPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
    }

    @Override
    protected ExpandPresenter createPresenter() {
        ExpandPresenter expandPresenter = new ExpandPresenter(this);

        testPresenter = new TestPresenter();
        exPresenter = new ExPresenter();

        expandPresenter.addPresenter(testPresenter);
        expandPresenter.addPresenter(exPresenter);

        return expandPresenter;
    }

    @Override
    protected void init() {

    }

    @Override
    public void exSuccess(ExEntity entity) {

    }

    @Override
    public void exFailure(String errorMsg) {

    }

    @Override
    public void testSuccess(TestEntity entity) {

    }

    @Override
    public void testFailure(String errorMsg) {

    }
}
