package com.android.lily.view.special;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.android.lily.R;

/**
 * Created by rape flower on 17/6/27.
 */
public class DrawableHelper {

    private static final String TAG = DrawableHelper.class.getSimpleName();
    private static Drawable drawable50;
    private static Drawable drawable100;
    private static Drawable drawable200;
    private static Drawable drawable300;
    private static Drawable drawable400;

    private DrawableHelper() {

    }

    /**
     * 获取Drawable: 50X18
     *
     * @param context
     * @return
     */
    public static Drawable getDrawable50(Context context) {
        if (drawable50 == null) {
            drawable50 = context.getResources().getDrawable(R.drawable.def_50);
        }
        return drawable50;
    }

    /**
     * 获取Drawable: 100X100
     *
     * @param context
     * @return
     */
    public static Drawable getDrawable100(Context context) {
        if (drawable100 == null) {
            drawable100 = context.getResources().getDrawable(R.drawable.def_100);
        }
        return drawable100;
    }

    /**
     * 获取Drawable: 200X200
     *
     * @param context
     * @return
     */
    public static Drawable getDrawable200(Context context) {
        if (drawable200 == null) {
            drawable200 = context.getResources().getDrawable(R.drawable.def_200);
        }
        return drawable200;
    }

    /**
     * 获取Drawable: 300X300
     *
     * @param context
     * @return
     */
    public static Drawable getDrawable300(Context context) {
        if (drawable300 == null) {
            drawable300 = context.getResources().getDrawable(R.drawable.def_300);
        }
        return drawable300;
    }

    /**
     * 获取Drawable: 400X400
     *
     * @param context
     * @return
     */
    public static Drawable getDrawable400(Context context) {
        if (drawable400 == null) {
            drawable400 = context.getResources().getDrawable(R.drawable.def_400);
        }
        return drawable400;
    }

    /**
     * drawable 转换成 bitmap
     *
     * @param drawable
     * @return
     */
    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();   // 取 drawable 的长宽
        int height = drawable.getIntrinsicHeight();
        //取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);//建立对应 bitmap
        Canvas canvas = new Canvas(bitmap);//建立对应 bitmap 的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);//把 drawable 内容画到画布中
        return bitmap;
    }

    /**
     * 按比例缩放Drawable
     *
     * @param drawable 原图
     * @param ratio  比例
     * @return 新的drawable
     */
    public static Drawable scaleDrawable(Context context, Drawable drawable, float ratio) {
        if (drawable == null) {
            return null;
        }
        int width = drawable.getIntrinsicWidth();
        int height= drawable.getIntrinsicHeight();

        Bitmap oldBitmap = drawableToBitmap(drawable); // drawable 转换成 bitmap
        Matrix matrix = new Matrix();   // 创建操作图片用的 Matrix 对象
        //设置缩放比例
        matrix.postScale(ratio, ratio);// 设置缩放比例
        Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, width, height, matrix, true);// 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
        return new BitmapDrawable(context.getResources(), newBitmap);// 把 bitmap 转换成 drawable 并返回
    }

    public static void setBackground(View view, Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        }
        else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 释放资源
     */
    public static void clear() {
        drawable100 = null;
        drawable200 = null;
        drawable300 = null;
        drawable400 = null;
    }
}
