package com.android.lily.application;

import android.app.Application;

import com.android.lily.lifecycle.AgentLifecycle;

public class ExApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AgentLifecycle.init(this);
    }
}
