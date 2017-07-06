package com.android.lily.view.special;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.lily.R;

/**
 * @author rape flower
 * @Date 2017-06-26 17:01
 * @descripe 自带默认图显示的ImageView
 */
public class J1ImageView extends ImageView {

    private static final String TAG = J1ImageView.class.getSimpleName();
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    /**
     * ImageView显示中心Log的尺寸标准
     */
    private static int STANDARD_SIZE_PX_50 = 50;
    private static int STANDARD_SIZE_PX_100 = 100;
    private static int STANDARD_SIZE_PX_200 = 200;
    private static int STANDARD_SIZE_PX_300 = 300;
    private static int STANDARD_SIZE_PX_400 = 400;
    private static int defaultSize = 0;

    private Context mContext;
    private int vWidth = 0;//控件的宽度
    private int vHeight = 0;//控件的高度
    private ScaleType setScaleType = ScaleType.CENTER;
    private boolean isDefaultImage = true;
    private Drawable backgroudDrawable;

    public J1ImageView(Context context) {
        this(context, null);
    }

    public J1ImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public J1ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context, attrs, defStyleAttr);
    }

    private void initLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        STANDARD_SIZE_PX_50 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_50px);
        STANDARD_SIZE_PX_100 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_100px);
        STANDARD_SIZE_PX_200 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_200px);
        STANDARD_SIZE_PX_300 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_300px);
        STANDARD_SIZE_PX_400 = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_404px);
        defaultSize = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_100px);

        setScaleType = getScaleType();

        if (attrs == null)
            return;
        int res = attrs.getAttributeResourceValue(NAMESPACE, "src", 0);
        if (res > 0) {
            isDefaultImage = false;
        }
        backgroudDrawable = getBackground();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isDefaultImageDrawable()) {
            vWidth = measureSize(widthMeasureSpec, defaultSize);
            vHeight = measureSize(heightMeasureSpec, defaultSize);
            setMeasuredDimension(vWidth, vHeight);
        }
        //android.util.Log.w(TAG, "width = " + vWidth + ", height = " + vHeight);
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

        if (isDefaultImageDrawable()) {
            // android.util.Log.w(TAG, "mSrcBitmap onDraw");
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
        } else if ((min == STANDARD_SIZE_PX_200) || (min > STANDARD_SIZE_PX_100 && min < STANDARD_SIZE_PX_200)) {
            drawable = DrawableHelper.getDrawable100(mContext);
        } else if ((min == STANDARD_SIZE_PX_100) || (min >= STANDARD_SIZE_PX_50 && min < STANDARD_SIZE_PX_100)) {
            drawable = DrawableHelper.getDrawable50(mContext);
        }
        
        setScaleType(ScaleType.CENTER);
        setImageDrawable(drawable);
//        setBackgroundColor(mContext.getResources().getColor(R.color.color_eeeeee));
        setBackground(mContext.getResources().getDrawable(R.drawable.shape_iv_bg));
    }

    private boolean isDefaultImageDrawable() {
        return isDefaultImage;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        reSetScaleType();
        super.setImageDrawable(drawable);
        // android.util.Log.w(TAG, "setImageDrawable = " + drawable + ", " + isDefaultImageDrawable());
        isDefaultImage = drawable == null;
        if (!isDefaultImageDrawable() ){
            DrawableHelper.setBackground(this,backgroudDrawable);
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        reSetScaleType();
        super.setImageResource(resId);
        isDefaultImage = resId <= 0;
        if (!isDefaultImageDrawable() ){
            DrawableHelper.setBackground(this,backgroudDrawable);
        }
    }

    private void reSetScaleType() {
        if (!isDefaultImageDrawable() && setScaleType != null && setScaleType != ScaleType.CENTER)
            setScaleType(setScaleType);
    }
}
