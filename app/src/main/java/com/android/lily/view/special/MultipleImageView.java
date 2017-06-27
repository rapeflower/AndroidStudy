package com.android.lily.view.special;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.android.lily.R;

/***********
 *
 * @Author rape flower
 * @Date 2017-05-18 13:21
 * @Describe 自定义支持圆角、圆形的ImageView
 *
 */
public class MultipleImageView extends J1ImageView {

    private Paint paint;
    private Paint paintBorder;
    private Bitmap mSrcBitmap;
    /**
     * 圆角的弧度
     */
    private float mRadius;
    private boolean mIsCircle;
    /**
     * ImageView实现：上面两个圆角下面两个直角的效果，上面两个直角下面两个圆角的效果
     */
    private Paint roundPaint1;
    private Paint roundPaint2;
    private float roundHeight = 10;
    private float roundWidth = 10;
    private boolean topLeftRightRound = false;
    private boolean bottomLeftRightRound = false;

    public MultipleImageView(final Context context) {
        this(context, null);
    }

    public MultipleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.MultipleImageView, defStyle, 0);
        mRadius = ta.getDimension(R.styleable.MultipleImageView_radius, 0);
        mIsCircle = ta.getBoolean(R.styleable.MultipleImageView_is_circle, false);
        roundHeight = roundWidth = mRadius;
        topLeftRightRound = ta.getBoolean(R.styleable.MultipleImageView_top_left_right_round, false);
        bottomLeftRightRound = ta.getBoolean(R.styleable.MultipleImageView_bottom_left_right_round, false);

        int srcResource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        if (srcResource != 0) {
            mSrcBitmap = BitmapFactory.decodeResource(getResources(), srcResource);
        }
        ta.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);

        roundPaint1 = new Paint();
        roundPaint1.setColor(Color.WHITE);
        roundPaint1.setAntiAlias(true);
        //16种状态
        roundPaint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        roundPaint2 = new Paint();
        roundPaint2.setXfermode(null);
    }

    @Override
    public void onDraw(Canvas canvas) {
        try {
            if (!mIsCircle && (topLeftRightRound || bottomLeftRightRound)) {
                Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmap);
                super.onDraw(canvas2);
                if (topLeftRightRound && !bottomLeftRightRound) {
                    drawTopLeft(canvas2);
                    drawTopRight(canvas2);
                }
                if (!topLeftRightRound && bottomLeftRightRound) {
                    drawBottomLeft(canvas2);
                    drawBottomRight(canvas2);
                }
                canvas.drawBitmap(bitmap, 0, 0, roundPaint2);
                bitmap.recycle();
            } else {
                int width = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
                int height = canvas.getHeight() - getPaddingTop() - getPaddingBottom();
                Bitmap image = drawableToBitmap(getDrawable());
                if (mIsCircle) {
                    Bitmap reSizeImage = reSizeImageC(image, width, height);
                    canvas.drawBitmap(createCircleImage(reSizeImage, width, height),
                            getPaddingLeft(), getPaddingTop(), null);
                } else {
                    Bitmap reSizeImage = reSizeImage(image, width, height);
                    canvas.drawBitmap(createRoundImage(reSizeImage, width, height),
                            getPaddingLeft(), getPaddingTop(), null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画圆角
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private Bitmap createRoundImage(Bitmap source, int width, int height) {
        if (source == null) {
            //返回一张默认图片
            return Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        // 核心代码取两个图片的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 画圆
     *
     * @param source
     * @param width
     * @param height
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int width, int height) {
        if (source == null) {
            //返回一张默认图片
            return Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2,
                paint);
        // 核心代码取两个图片的交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, (width - source.getWidth()) / 2,
                (height - source.getHeight()) / 2, paint);
        return target;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            if (mSrcBitmap != null) {
                return mSrcBitmap;
            } else {
                return null;
            }
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap reSizeImage(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            //返回一张默认图片
            return Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算出缩放比
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 矩阵缩放bitmap
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap reSizeImageC(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            //返回一张默认图片
            return Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int x = (newWidth - width) / 2;
        int y = (newHeight - height) / 2;
        if (x > 0 && y > 0) {
            return Bitmap.createBitmap(bitmap, 0, 0, width, height, null, true);
        }

        float scale = 1;

        if (width > height) {
            // 按照宽度进行等比缩放
            scale = ((float) newWidth) / width;

        } else {
            // 按照高度进行等比缩放
            // 计算出缩放比
            scale = ((float) newHeight) / height;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 画上面左边圆角
     *
     * @param canvas
     */
    private void drawTopLeft(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        //arcTo的第二个参数是以多少度为开始点，第三个参数-90度表示逆时针画弧，正数表示顺时针
        path.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -90, -90);
        path.close();
        canvas.drawPath(path, roundPaint1);
    }

    /**
     * 画下面左边圆角
     *
     * @param canvas
     */
    private void drawBottomLeft(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() - roundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(roundWidth, getHeight());
        path.arcTo(new RectF(0, getHeight() - roundHeight * 2, 0 + roundWidth * 2, getHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, roundPaint1);
    }

    /**
     * 画上面右边圆角
     *
     * @param canvas
     */
    private void drawTopRight(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - roundWidth, 0);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(), 0 + roundHeight * 2), -90, 90);
        path.close();
        canvas.drawPath(path, roundPaint1);
    }

    /**
     * 画下面右边圆角
     *
     * @param canvas
     */
    private void drawBottomRight(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth() - roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - roundHeight);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight() - roundHeight * 2, getWidth(), getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, roundPaint1);
    }

}
