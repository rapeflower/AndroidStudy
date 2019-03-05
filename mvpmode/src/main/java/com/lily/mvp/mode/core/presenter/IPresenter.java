package com.lily.mvp.mode.core.presenter;

import com.lily.mvp.mode.core.view.IView;

public interface IPresenter<T extends IView> {

    void attachView(T view);

    void detachView();

    boolean isAttached();
}
