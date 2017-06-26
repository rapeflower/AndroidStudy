package com.android.lily.viewpager;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by lilei on 2017/5/20.
 */
public class GalleryTransformer implements ViewPager.PageTransformer{

    private static final String TAG = GalleryTransformer.class.getSimpleName();

    @Override
    public void transformPage(View view, float position) {
//        Log.w(TAG, "transformPage: "+ Math.abs(position));
        float scale = 0.5f;
        float scaleValue = 1 - Math.abs(position) * scale;
        view.setScaleX(scaleValue);
        view.setScaleY(scaleValue);
        view.setAlpha(scaleValue);
        float pivotX = view.getWidth() * (1 - position - (position > 0 ? 1 : -1) * 0.75f) * scale;
        Log.w(TAG, "pivotX = "+ pivotX);
        view.setPivotX(pivotX);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            view.setElevation(position > -0.25 && position < 0.25 ? 1 : 0);
        }
    }
}
