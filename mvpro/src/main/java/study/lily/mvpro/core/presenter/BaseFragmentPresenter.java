package study.lily.mvpro.core.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import study.lily.mvpro.core.utils.TypeBuilder;
import study.lily.mvpro.core.view.BaseView;

/**
 * @Author rape flower
 * @Date 2019-02-27 11:16
 * @Describe 将Fragment作为Presenter，其它业务Fragment的基类
 */
public class BaseFragmentPresenter<T extends BaseView> extends Fragment implements IPresenter<T> {

    protected T mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        beforeCreate(savedInstanceState);

        try {
            mView = getClassType().newInstance();
            View view = mView.buildContentView(inflater, container);
            mView.bindPresenter(this);
            mView.bindEvent();
            afterCreate(savedInstanceState);
            return view;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Class<T> getClassType() {
        return TypeBuilder.getTypeByClass(getClass());
    }

    @Override
    public void beforeCreate(Bundle savedInstanceState) {

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

    }
}
