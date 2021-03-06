package com.lily.mvp.mode.core.interfaces;

public interface Callback<R, E> {

    void onSuccess(R data);

    void onFailure(E error);
}
