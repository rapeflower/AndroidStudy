package com.android.lily.lifecycle;

import android.app.Activity;
import android.os.Bundle;

/**
 * 4.0以下
 */
public interface IActivityLifecycleCallbacks {

    void onActivityCreated(Activity activity, Bundle savedInstanceState);

    void onActivityStarted(Activity activity);

    void onActivityResumed(Activity activity);

    void onActivityPaused(Activity activity);

    void onActivityStopped(Activity activity);

    void onActivityDestroyed(Activity activity);

}
