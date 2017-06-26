package com.android.lily.activity;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * @author rape flower
 * @descripe ViewPager页面切换的缩放效果动画
 */
public class ZoomPageTransformer implements ViewPager.PageTransformer{

    private static final String TAG = ZoomPageTransformer.class.getSimpleName();
    private float pivotScale = 0.75f;

    public ZoomPageTransformer(Context context) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        pivotScale = 0.25f * density;
    }

    @Override
    public void transformPage(View view, float position) {
        Log.w(TAG, "transformPage: abs = "+ Math.abs(position));
        float scale = 0.5f;
        float scaleValueX = 1 - Math.abs(position) * scale;
        float scaleValueY = 1 - Math.abs(position) * (scale - 0.3f);
        Log.w(TAG, "transformPage: scaleValueX = "+ scaleValueX);
        Log.w(TAG, "transformPage: scaleValueY = "+ scaleValueY);
        view.setScaleX(scaleValueX);
        view.setScaleY(scaleValueY);
        view.setAlpha(scaleValueX);
        float pivotX = view.getWidth() * (1 - position - (position > 0 ? 1 : -1) * pivotScale) * scale;
        float pivotY = view.getHeight() *  scale;
        Log.w(TAG, "transformPage: pivotY = "+ pivotY);
        view.setPivotX(pivotX);
        view.setPivotY(pivotY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           view.setElevation(position > -0.25 && position < 0.25 ? 1 : 0);
        }
    }
}
