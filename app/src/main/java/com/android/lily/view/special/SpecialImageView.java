package com.android.lily.view.special;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.lily.R;
import com.android.lily.utils.DisplayUnitUtils;

/**
 * Created by rape flower on 17/6/26.
 */
public class SpecialImageView extends ImageView {

    private static final String TAG = SpecialImageView.class.getSimpleName();
    private Context mContext;
    private Paint mPaint;
    private Bitmap mSrcBitmap;
    int [] defAttrs = {android.R.attr.src};

    public SpecialImageView(Context context) {
        this(context, null);
    }

    public SpecialImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpecialImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, defAttrs, defStyleAttr, 0);

        setBackgroundColor(context.getResources().getColor(R.color.color_1A000000));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);

        android.util.Log.w(TAG, "width = " + width + ", height = " + height);
        android.util.Log.w(TAG, "scaleDensity = " + DisplayUnitUtils.getDisplayScaleDensity(mContext));
    }
}
