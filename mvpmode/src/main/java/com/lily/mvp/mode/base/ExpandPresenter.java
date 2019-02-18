package com.lily.mvp.mode.base;

import com.lily.mvp.mode.presenter.IPresenter;
import com.lily.mvp.mode.view.IView;

import java.util.ArrayList;
import java.util.List;

public class ExpandPresenter<T extends IView> extends BasePresenter<T> {

    protected T mView;
    private List<IPresenter> presenters = new ArrayList<>();

    public ExpandPresenter(T view) {
        this.mView = view;
    }

    public final <Z extends IPresenter<T>> void addPresenter(Z... ap) {
        for (Z p : ap) {
            p.attachView(mView);
            presenters.add(p);
        }
    }

    @Override
    public void detachView() {
        for (IPresenter presenter: presenters) {
            presenter.detachView();
        }
        mView = null;
    }
}
