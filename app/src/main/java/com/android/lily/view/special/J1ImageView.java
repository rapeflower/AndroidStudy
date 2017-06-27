package com.android.lily.view.special;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.lily.R;
import com.android.lily.utils.DisplayUnitUtils;

/**
 * Created by rape flower on 17/6/26.
 */
public class J1ImageView extends ImageView {

    private static final String TAG = J1ImageView.class.getSimpleName();
    /**
     * ImageView显示中心Log的尺寸标准
     */
    private static final int STANDARD_SIZE_PX_100 = 100;
    private static final int STANDARD_SIZE_PX_200 = 200;
    private static final int STANDARD_SIZE_PX_300 = 300;
    private static final int STANDARD_SIZE_PX_400 = 400;

    private Context mContext;
    private Paint mPaint;
    private Bitmap mSrcBitmap;
    private boolean isSetImageResource = false;
    int [] defAttrs = {android.R.attr.src};
    int vWidth = 0;//控件的宽度
    int vHeight = 0;//控件的高度

    public J1ImageView(Context context) {
        this(context, null);
    }

    public J1ImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public J1ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, defAttrs, defStyleAttr, 0);
        int srcResource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        if (srcResource != 0) {
            mSrcBitmap = BitmapFactory.decodeResource(getResources(), srcResource);
        }
        ta.recycle();

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        isSetImageResource = false;

        setBackgroundColor(context.getResources().getColor(R.color.color_1A000000));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        vWidth = MeasureSpec.getSize(widthMeasureSpec);
        vHeight = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(width, height);
        android.util.Log.w(TAG, "width = " + vWidth + ", height = " + vHeight);
        android.util.Log.w(TAG, "scaleDensity = " + DisplayUnitUtils.getDisplayScaleDensity(mContext));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        android.util.Log.w(TAG, "mSrcBitmap = " + mSrcBitmap);
        if (mSrcBitmap == null && !isSetImageResource) {
            adaptationSrcSize();
        }
    }

    /**
     * 适配ImageView的srcResource的大小
     */
    private void adaptationSrcSize() {
        Drawable drawable = null;
        int min = Math.min(vWidth, vHeight);
        android.util.Log.w(TAG, " min = " + min);
        if (min >= STANDARD_SIZE_PX_400) {
            drawable = DrawableHelper.getDrawable400(mContext);
        } else if (min >= STANDARD_SIZE_PX_300 && min < STANDARD_SIZE_PX_300) {
            drawable = DrawableHelper.getDrawable300(mContext);
        } else if (min >= STANDARD_SIZE_PX_200 && min < STANDARD_SIZE_PX_200) {
            drawable = DrawableHelper.getDrawable200(mContext);
        } else if (min >= STANDARD_SIZE_PX_100 && min < STANDARD_SIZE_PX_100) {
            drawable = DrawableHelper.getDrawable100(mContext);
        } else {
            drawable = DrawableHelper.getDrawable100(mContext);
            float radio = vWidth / STANDARD_SIZE_PX_100;
            //对Drawable进行按比例缩放后重新赋值
            drawable = DrawableHelper.scaleDrawable(mContext, drawable, radio);
        }

        setImageDrawable(drawable);
        isSetImageResource = true;
    }

}
