package com.lily.mvp.mode.core.base;

import com.lily.mvp.mode.core.presenter.IPresenter;
import com.lily.mvp.mode.core.view.IView;

public abstract class BasePresenter<T extends IView> implements IPresenter<T> {

    protected T mView;

    @Override
    public void attachView(T view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return mView != null;
    }
}
