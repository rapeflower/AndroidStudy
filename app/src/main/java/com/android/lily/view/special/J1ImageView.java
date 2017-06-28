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
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    /**
     * ImageView显示中心Log的尺寸标准
     */
    private static int STANDARD_SIZE_PX_100 = 100;
    private static int STANDARD_SIZE_PX_200 = 200;
    private static int STANDARD_SIZE_PX_300 = 300;
    private static int STANDARD_SIZE_PX_400 = 400;
    private static int defaultSize = 0;

    private Context mContext;
    private Paint mPaint;
    private Bitmap mSrcBitmap;
    private boolean isSetImageResource = false;
    int [] sysAttrs = {android.R.attr.src};
    float density = 0;
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
        STANDARD_SIZE_PX_100 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_100px);
        STANDARD_SIZE_PX_200 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_200px);
        STANDARD_SIZE_PX_300 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_300px);
        STANDARD_SIZE_PX_400 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_400px);
        defaultSize = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_100px);
        density = DisplayUnitUtils.getDisplayScaleDensity(mContext);

        TypedArray ta = context.obtainStyledAttributes(attrs, sysAttrs, defStyleAttr, 0);
        int srcResource = attrs.getAttributeResourceValue(NAMESPACE, "src", 0);
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
        if (mSrcBitmap == null) {
            vWidth = measureSize(widthMeasureSpec, defaultSize);
            vHeight = measureSize(heightMeasureSpec, defaultSize);
            setMeasuredDimension(vWidth, vHeight);
        }
        android.util.Log.w(TAG, "width = " + vWidth + ", height = " + vHeight);
    }

    /**
     * 测量大小
     *
     * @param measureSpec
     * @param defaultValue
     * @return
     */
    private int measureSize(int measureSpec, int defaultValue) {
        //设置一个默认值，就是这个View的默认宽度或高度为defaultValue
        int result = defaultValue;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //android.util.Log.w(TAG, "specSize = " + specSize);
        switch (specMode) {
            // Mode = UNSPECIFIED,AT_MOST时使用提供的默认大小
            case MeasureSpec.AT_MOST://相当于我们设置为wrap_content
            case MeasureSpec.UNSPECIFIED:
                result = defaultValue;
                break;
            case MeasureSpec.EXACTLY:// Mode = EXACTLY时使用测量的大小
                //相当于我们设置为match_parent或者为一个具体的值
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mSrcBitmap == null && !isSetImageResource) {
            //android.util.Log.w(TAG, "mSrcBitmap = " + mSrcBitmap);
            adaptationSrcSize();
        }
    }

    /**
     * 适配ImageView的srcResource的大小
     */
    private void adaptationSrcSize() {
        Drawable drawable = null;
        int min = Math.min(vWidth, vHeight);
        //android.util.Log.w(TAG, " min = " + min);
        if (min > STANDARD_SIZE_PX_400) {
            drawable = DrawableHelper.getDrawable400(mContext);
        } else if ((min == STANDARD_SIZE_PX_400) || (min > STANDARD_SIZE_PX_300 && min < STANDARD_SIZE_PX_400)) {
            drawable = DrawableHelper.getDrawable300(mContext);
        } else if ((min == STANDARD_SIZE_PX_300) || (min > STANDARD_SIZE_PX_200 && min < STANDARD_SIZE_PX_300)) {
            drawable = DrawableHelper.getDrawable200(mContext);
        } else if ( (min == STANDARD_SIZE_PX_200) || (min > STANDARD_SIZE_PX_100 && min < STANDARD_SIZE_PX_200)) {
            drawable = DrawableHelper.getDrawable100(mContext);
        } else {
            drawable = DrawableHelper.getDrawable100(mContext);
            float radio = ((float) min) / STANDARD_SIZE_PX_100;
            android.util.Log.w(TAG, " radio = " + radio);
            //对Drawable进行按比例缩放后重新赋值
            drawable = DrawableHelper.scaleDrawable(mContext, drawable, radio);
        }

        setScaleType(ScaleType.CENTER);
        setImageDrawable(drawable);
        isSetImageResource = true;
    }

}
