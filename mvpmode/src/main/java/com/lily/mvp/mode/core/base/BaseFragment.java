package com.lily.mvp.mode.core.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lily.mvp.mode.core.view.IView;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IView {

    protected T mPresenter;
    protected View fragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(onBindLayoutId(), container, false);
            init();
        }
        ViewGroup parent = (ViewGroup) fragmentView.getParent();
        if (parent != null) {
            parent.removeView(fragmentView);
        }
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initPresenter();
    }

    private void initPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract int onBindLayoutId();

    protected abstract T createPresenter();

    protected abstract void init();
}
