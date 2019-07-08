package com.android.lily.lifecycle;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ActivityLifeManager implements IActivityLifecycleCallbacks {

    private static ActivityLifeManager manager;
    private List<IActivityLifecycleCallbacks> lifecycleCallbacks = new ArrayList<>();

    private ActivityLifeManager() {

    }

    public static synchronized ActivityLifeManager getInstance() {
        if (manager == null) {
            manager = new ActivityLifeManager();
        }
        return manager;
    }

    public void addActivityLifeChange(IActivityLifecycleCallbacks lifecycleCallback) {
        lifecycleCallbacks.add(lifecycleCallback);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        for (IActivityLifecycleCallbacks ilc: lifecycleCallbacks) {
            ilc.onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        for (IActivityLifecycleCallbacks ilc: lifecycleCallbacks) {
            ilc.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        for (IActivityLifecycleCallbacks ilc: lifecycleCallbacks) {
            ilc.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        for (IActivityLifecycleCallbacks ilc: lifecycleCallbacks) {
            ilc.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        for (IActivityLifecycleCallbacks ilc: lifecycleCallbacks) {
            ilc.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        for (IActivityLifecycleCallbacks ilc: lifecycleCallbacks) {
            ilc.onActivityDestroyed(activity);
        }
    }
}
