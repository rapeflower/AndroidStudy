package com.android.lily.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AgentLifecycle {

    private static final String TAG = "AgentLifecycle";

    public static void init(Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            application.registerActivityLifecycleCallbacks(new NewActivityLifecycleImpl());
        } else {
            replaceInstrumentation();
        }

        ActivityLifeManager.getInstance().addActivityLifeChange(new IActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                android.util.Log.w(TAG, "Activity: " + activity.getClass().getName());
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 替换系统默认的Instrumentation
     */
    public static void replaceInstrumentation() {
        Class<?> activityThreadClass;

        try {
            // 加载activity thread的class
            activityThreadClass = Class.forName("android.app.ActivityThread");
            // 找到方法currentActivityThread
            Method method = activityThreadClass.getDeclaredMethod("currentActivityThread");
            // 由于这个方法是静态的，所以传入Null就行了
            Object currentActivityThread = method.invoke(null);

            // 把原来ActivityThread中的mInstrumentation替换成自定义的ExInstrumentation
            Field field = activityThreadClass.getDeclaredField("mInstrumentation");
            field.setAccessible(true);
            ExInstrumentation exInstrumentation = new ExInstrumentation();
            field.set(currentActivityThread, exInstrumentation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
