package com.lily.mvp.mode.contract;

public interface TestContract extends IContract {

    interface TestView extends BaseView<com.lily.mvp.mode.entity.TestEntity> {

    }

    interface TPresenter {
        void getTestData();
    }
}
