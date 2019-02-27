package study.lily.mvpro.core.presenter;

import android.os.Bundle;

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
