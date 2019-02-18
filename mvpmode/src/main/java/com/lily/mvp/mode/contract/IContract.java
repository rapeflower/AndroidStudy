package com.lily.mvp.mode.contract;

import com.lily.mvp.mode.entity.BaseEntity;
import com.lily.mvp.mode.view.IView;

public interface IContract {

    interface BaseView<E extends BaseEntity> extends IView {
        void showSuccess(E entity);
        void showFailure(String errorMsg);
    }

    interface Presenter {
        void obtainData();
    }
}
