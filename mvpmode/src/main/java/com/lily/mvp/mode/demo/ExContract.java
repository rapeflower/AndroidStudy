package com.lily.mvp.mode.demo;

import com.lily.mvp.mode.core.contract.IContract;
import com.lily.mvp.mode.core.view.IView;

public interface ExContract extends IContract {

    interface ExView extends IView {
        void exSuccess(ExEntity entity);
        void exFailure(String errorMsg);
    }

    interface ExPresenter {
        void getExData();
    }
}
