package com.lily.mvp.mode.core.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lily.mvp.mode.core.view.IView;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IView, LoaderManager.LoaderCallbacks<T> {

    public static final int BASE_FRAGMENT_LOADER_ID = 188;
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
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(BASE_FRAGMENT_LOADER_ID, null, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @NonNull
    @Override
    public Loader<T> onCreateLoader(int id, @Nullable Bundle args) {
        return createPresenterLoader();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<T> loader, T data) {
        mPresenter = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<T> loader) {
        mPresenter = null;
    }

    protected abstract int onBindLayoutId();

    protected abstract Loader<T> createPresenterLoader();

    protected abstract void init();
}
