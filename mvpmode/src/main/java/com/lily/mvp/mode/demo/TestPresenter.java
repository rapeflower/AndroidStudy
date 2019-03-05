package com.lily.mvp.mode.demo;

import com.lily.mvp.mode.core.base.BasePresenter;
import com.lily.mvp.mode.core.interfaces.Callback;

public class TestPresenter extends BasePresenter<TestContract.TestView> implements TestContract.TPresenter {

    private final ITestModel testModel;

    public TestPresenter() {
        testModel = new TestModel();
    }

    @Override
    public void getTestData() {
        testModel.getTest(new Callback<TestEntity, String>() {
            @Override
            public void onSuccess(TestEntity data) {
                if (isAttached()) {
                    mView.testSuccess(data);
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAttached()) {
                    mView.testFailure(error);
                }
            }
        });
    }
}
