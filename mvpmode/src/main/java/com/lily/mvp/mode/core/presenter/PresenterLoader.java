package com.lily.mvp.mode.core.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

public class PresenterLoader<T extends IPresenter> extends Loader {

    private final PresenterFactory<T> factory;
    private T presenter;

    public PresenterLoader(@NonNull Context context, PresenterFactory factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter = null;
    }
}
