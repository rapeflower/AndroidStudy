package com.lily.mvp.mode.presenter;

import com.lily.mvp.mode.view.IView;

public interface IPresenter<T extends IView> {

    void attachView(T view);

    void detachView();

    boolean isAttached();
}
