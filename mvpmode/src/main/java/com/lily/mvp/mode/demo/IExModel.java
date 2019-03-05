package com.lily.mvp.mode.demo;

import com.lily.mvp.mode.core.interfaces.Callback;
import com.lily.mvp.mode.core.model.IModel;

public interface IExModel extends IModel {
    void getEx(final Callback callback);
}
