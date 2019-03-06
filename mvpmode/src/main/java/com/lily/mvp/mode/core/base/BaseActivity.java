package com.lily.mvp.mode.core.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.lily.mvp.mode.core.view.IView;

public abstract class BaseActivity<T extends BasePresenter> extends FragmentActivity implements IView, LoaderManager.LoaderCallbacks<T> {

    public static final int BASE_LOADER_ACTIVITY_ID = 168;
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(BASE_LOADER_ACTIVITY_ID, null, this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @NonNull
    @Override
    public Loader<T> onCreateLoader(int id, @Nullable Bundle args) {
        //子类要实现createPresenterLoader方法创建Loader
        return createPresenterLoader(id, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<T> loader, T data) {
        mPresenter = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<T> loader) {
        mPresenter = null;
    }

    protected abstract Loader<T> createPresenterLoader(int id, @Nullable Bundle args);

    protected abstract void init();
}
