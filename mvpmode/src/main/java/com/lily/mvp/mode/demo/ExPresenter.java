package com.lily.mvp.mode.demo;

import com.lily.mvp.mode.core.base.BasePresenter;
import com.lily.mvp.mode.core.interfaces.Callback;

public class ExPresenter extends BasePresenter<ExContract.ExView> implements ExContract.ExPresenter {

    private final IExModel iExModel;

    public ExPresenter() {
        iExModel = new ExModel();
    }

    @Override
    public void getExData() {
        iExModel.getEx(new Callback<ExEntity, String>() {

            @Override
            public void onSuccess(ExEntity data) {
                if (isAttached()) {
                    mView.exSuccess(data);
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAttached()) {
                    mView.exFailure(error);
                }
            }
        });
    }
}
