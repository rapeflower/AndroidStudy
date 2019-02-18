package com.lily.mvp.mode.presenter;

import com.lily.mvp.mode.base.BasePresenter;
import com.lily.mvp.mode.contract.TestContract;
import com.lily.mvp.mode.entity.TestEntity;
import com.lily.mvp.mode.interfaces.Callback;
import com.lily.mvp.mode.model.TestModel;

public class TestPresenter extends BasePresenter<TestContract.TestView> implements TestContract.TPresenter {

    public TestPresenter() {
        iModel = new TestModel();
    }

    @Override
    public void getTestData() {
        ((TestModel) iModel).getTest(new Callback<TestEntity, String>() {
            @Override
            public void onSuccess(TestEntity data) {
                if (isAttached()) {
                    mView.showSuccess(data);
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAttached()) {
                    mView.showFailure(error);
                }
            }
        });
    }
}
