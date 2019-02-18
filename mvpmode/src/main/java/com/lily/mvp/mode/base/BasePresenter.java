package com.lily.mvp.mode.base;

import com.lily.mvp.mode.model.IModel;
import com.lily.mvp.mode.presenter.IPresenter;
import com.lily.mvp.mode.view.IView;

public abstract class BasePresenter<T extends IView> implements IPresenter<T> {

    protected T mView;
    protected IModel iModel;

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
