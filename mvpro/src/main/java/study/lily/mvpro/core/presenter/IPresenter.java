package study.lily.mvpro.core.presenter;

import android.os.Bundle;

/**
 * @Author rape flower
 * @Date 2019-02-26 17:16
 * @Describe Presenter接口
 */
public interface IPresenter<T> {

    /**
     * 获取当前presenter泛型的类型
     * @return
     */
    Class<T> getClassType();

    /**
     *
     * @param savedInstanceState
     */
    void beforeCreate(Bundle savedInstanceState);

    /**
     *
     * @param savedInstanceState
     */
    void afterCreate(Bundle savedInstanceState);

}
