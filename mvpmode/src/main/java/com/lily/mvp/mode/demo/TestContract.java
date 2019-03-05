package com.lily.mvp.mode.demo;

import com.lily.mvp.mode.core.contract.IContract;
import com.lily.mvp.mode.core.view.IView;

public interface TestContract extends IContract {

    interface TestView extends IView {
        void testSuccess(TestEntity entity);
        void testFailure(String errorMsg);
    }

    interface TPresenter {
        void getTestData();
    }
}
