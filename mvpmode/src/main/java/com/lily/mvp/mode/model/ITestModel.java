package com.lily.mvp.mode.model;

import com.lily.mvp.mode.interfaces.Callback;

public interface ITestModel extends IModel {
    void getTest(final Callback callback);
}
