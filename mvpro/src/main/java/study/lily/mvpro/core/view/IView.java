package study.lily.mvpro.core.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import study.lily.mvpro.core.presenter.IPresenter;

/**
 * @Author rape flower
 * @Date 2019-02-26 17:32
 * @Describe View的接口
 */
public interface IView {

    /**
     * 构建根布局
     * @param inflater
     * @param container
     * @return
     */
    View buildContentView(LayoutInflater inflater, ViewGroup container);

    /**
     * Activity的onCreate执行完后调用
     */
    void onCreateComplete();

    /**
     * 返回当前根布局的layout的id
     */
    int bindLayoutId();

    /**
     * 根据id获取view
     * @param id
     * @param <T>
     * @return
     */
    <T extends View> T findViewById(int id);

    /**
     * 绑定Presenter
     * @param presenter
     */
    void bindPresenter(IPresenter presenter);

    /**
     * 绑定事件
     */
    void bindEvent();

}
