package com.lily.mvp.mode.core.presenter;

public interface PresenterFactory<T extends IPresenter> {
    T create();
}
