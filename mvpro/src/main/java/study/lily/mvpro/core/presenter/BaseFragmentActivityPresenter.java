package study.lily.mvpro.core.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import study.lily.mvpro.core.utils.TypeBuilder;
import study.lily.mvpro.core.view.BaseView;

/**
 * @Author rape flower
 * @Date 2019-02-27 11:16
 * @Describe 将FragmentActivity作为Presenter，其它业务Activity的基类
 */
public class BaseFragmentActivityPresenter<T extends BaseView> extends FragmentActivity implements IPresenter<T> {

    protected T mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeCreate(savedInstanceState);

        try {
            mView = getClassType().newInstance();
            mView.bindPresenter(this);
            setContentView(mView.buildContentView(getLayoutInflater(), null));
            mView.bindEvent();
            afterCreate(savedInstanceState);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
