package study.lily.mvpro.core.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import study.lily.mvpro.core.presenter.IPresenter;

/**
 * @Author rape flower
 * @Date 2019-02-27 10:16
 * @Describe View的基类
 */
public abstract class BaseView implements IView {

    protected View mRootView;
    protected IPresenter mPresenter;

    @Override
    public View buildContentView(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(bindLayoutId(), container, false);
        onCreateComplete();
        return mRootView;
    }

    @Override
    public void onCreateComplete() {

    }

    @Override
    public <T extends View> T findViewById(int id) {
        return (T) mRootView.findViewById(id);
    }

    @Override
    public void bindPresenter(IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void bindEvent() {

    }
}
