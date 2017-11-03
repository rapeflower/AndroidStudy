package com.android.lily.view.boutiquemuseum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.lily.R;
import com.android.lily.view.special.J1ImageView;
import com.bumptech.glide.Glide;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author rape flower
 * @descripe 精品馆横向滑动View
 */
public class BoutiqueMuseumTab extends HorizontalScrollView{

    private static final String TAG = BoutiqueMuseumTab.class.getSimpleName();
    //字体大小、颜色
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    //布局参数
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private int selectedPosition = 0;
    private Paint indicatorPaint;
    private int indicatorColor = 0xFF212630;
    //该属性表示里面的TAB是否均分整个BoutiqueMuseumTab控件的宽,
    // true是,false不均分,从左到右排列,默认false
    private boolean shouldExpand = false;

    private int scrollOffset = 52;
    private int indicatorHeight = 1;
    private int tabPadding = 20;

    private int tabTextSize = 12;
    //Color.parseColor("#2b2b2b")
    //'#2b2b2b' -> rgb(43, 43, 43)
    private int tabTextColor = Color.rgb(43, 43, 43);
    //Color.parseColor("#45c01a")
    //'#45c01a' -> rgb(69, 192, 26)
    private int selectedTabTextColor = Color.rgb(69, 192, 26);//默认选择TAB的文字颜色
    private static final int SHAPE_LINE = 0;//指示器View的形状
    private static final int SHAPE_CIRCLE = 1;
    private int indicatorShape = SHAPE_LINE;
    private boolean mFadeEnabled = true;
    private boolean isFirstDefaultSelected = true;
    private boolean isClickChangePage = false;//标识当前用户是点击切换页面还是左右滑动切换页面，默认是左右滑动切换页面
    private int oldPage;
    private float zoomMax = 0.2f;
    private State mState;

    private enum State {
        IDLE, GOING_LEFT, GOING_RIGHT
    }

    private int lastScrollX = 0;//记录滚动结束时的x坐标
    private Locale locale;
    private Context mContext;
    private LayoutInflater mInflater;
    private Paint mPaintTabText = new Paint();
    private List<BoutiqueMuseumItem> dataList = null;
    private PageListener pageListener;//页面滚动监听
    public ViewPager.OnPageChangeListener delegatePageListener;
    OnSelectBoutiqueMuseumListener mOnSelectBoutiqueMuseumListener;

    public BoutiqueMuseumTab(Context context) {
        this(context, null);
    }

    public BoutiqueMuseumTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoutiqueMuseumTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataList = new ArrayList<BoutiqueMuseumItem>();
        setFillViewport(true);
        setWillNotDraw(false);//防止onDraw方法不执行

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        FrameLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tabsContainer.setLayoutParams(lp);
        addView(tabsContainer);

        //设置默认值
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        //获取系统属性 (android:textSize and android:textColor)
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();

        indicatorPaint = new Paint();
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setStyle(Paint.Style.FILL);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        int w = getDisplayWidth(context) / 9 * 2;
        defaultTabLayoutParams.width = w;

        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }

        pageListener = new PageListener();

        mPaintTabText.setAntiAlias(true);
    }

    /**
     * ViewPager的页面滑动监听
     */
    private class PageListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //Log.w(TAG, "onPageScrolled =》position = " + position);
            currentPosition = position;
            currentPositionOffset = positionOffset;

            if (tabsContainer != null && tabsContainer.getChildAt(position) != null) {
                scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            }
            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            if (mState == State.IDLE && positionOffset > 0) {
                oldPage = pager.getCurrentItem();
                mState = position == oldPage ? State.GOING_RIGHT : State.GOING_LEFT;
            }
            boolean goingRight = position == oldPage;
            if (mState == State.GOING_RIGHT && !goingRight) {
                mState = State.GOING_LEFT;
            } else if (mState == State.GOING_LEFT && goingRight) {
                mState = State.GOING_RIGHT;
            }

            float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;

            View mLeft = tabsContainer.getChildAt(position);
            View mRight = tabsContainer.getChildAt(position + 1);

            if (effectOffset == 0) {
                mState = State.IDLE;
            }

            if (mFadeEnabled) {
                animateFadeScale(mLeft, mRight, effectOffset, position);
            }

        }

        /**
         * @param state:
         *  ViewPager.SCROLL_STATE_DRAGGING: 正在滑动 pager处于正在拖拽中
         *  ViewPager.SCROLL_STATE_SETTLING: pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
         *  ViewPager.SCROLL_STATE_IDLE: 空闲状态 pager处于空闲状态
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            //Log.w(TAG, "onPageScrollStateChanged");
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isClickChangePage = false;//标识当前用户是左右滑动切换页面
                mFadeEnabled = true;
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            selectedPosition = position;

            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }

            oldPosition = selectedPosition;
        }
    }

    /****
     * 关联ViewPager
     *
     * @param pager pager
     */
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (this.pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        //addOnPageChangeListener()方法之前先执行clearOnPageChangeListeners()
        this.pager.clearOnPageChangeListeners();
        this.pager.addOnPageChangeListener(pageListener);
        this.notifyDataSetChanged();
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public int getDisplayWidth(Context context) {
        int width = Math.min(context.getResources().getDisplayMetrics().widthPixels
                , context.getResources().getDisplayMetrics().heightPixels);
        return width;
    }

    /**
     * 数据改变
     */
    public void notifyDataSetChanged() {
        if (tabsContainer == null) {
            return;
        }
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            String name = pager.getAdapter().getPageTitle(i).toString().trim();
            int flagIndex = name.indexOf("¥");
            String title = name.substring(0, flagIndex);
            String url = name.substring(flagIndex + 1, name.length());
            addTab(i, title, url);
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                currentPosition = 0;
                scrollToChild(currentPosition, 0);
                //Log.w(TAG, " onGlobalLayout ");

                updateTabStyles(currentPosition);
            }
        });
    }

    /**
     * 添加TAB选项
     *
     * @param position
     * @param title
     */
    private void addTab(final int position, String title, String url) {
        View tabItem = mInflater.inflate(R.layout.item_boutique_museum, null);
        TextView tv_name = (TextView) tabItem.findViewById(R.id.tv_boutique_museum_name);
        tv_name.setText(title);

        J1ImageView ivBMImg = (J1ImageView) tabItem.findViewById(R.id.iv_boutique_museum_img);
        showImage(ivBMImg, url);
        tabsContainer.addView(tabItem, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);

        tabItem.setOnClickListener(new PreventTooFastClickListener() {
            @Override
            void onNormalClick(View v) {
                //单击事件回调
                if (mOnSelectBoutiqueMuseumListener!=null){
                    mOnSelectBoutiqueMuseumListener.onSelect(position);
                }
                isFirstDefaultSelected = false;
                currentPosition = position;
                mFadeEnabled = false;//点击时没有文字颜色渐变效果
                isClickChangePage = true;//标识当前用户是点击切换页面
                pager.setCurrentItem(position, false);
                scrollToChild(position, 0);//滚动HorizontalScrollView
                updateTabStyles(position);
            }
        });
    }

    /**
     * 防止过快点击造成多次事件
     */
    public abstract class PreventTooFastClickListener implements OnClickListener {

        public static final int MIN_CLICK_DELAY_TIME = 1000;
        private long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNormalClick(v);
            }
        }

        abstract void onNormalClick(View v);
    }

    /**
     * 更新TAB文本样式
     */
    public void updateTabStyles(int index) {
        for (int i = 0; i < tabCount; i++) {
            View tabItem = tabsContainer.getChildAt(i);
            if (tabItem == null) {
                return;
            }
            TextView tv_name = (TextView) tabItem.findViewById(R.id.tv_boutique_museum_name);
            J1ImageView iv_img = (J1ImageView) tabItem.findViewById(R.id.iv_boutique_museum_img);
            if (i == index) {
                tv_name.setTextColor(selectedTabTextColor);
                //属性动画
                ViewPropertyAnimator animatorTv = ViewPropertyAnimator.animate(tv_name).scaleX(1.2f).scaleY(1.2f);
                ViewPropertyAnimator animatorIv = ViewPropertyAnimator.animate(iv_img).scaleX(1.2f).scaleY(1.2f);
                if (isFirstDefaultSelected) {
                    animatorTv.setDuration(0);
                    animatorIv.setDuration(0);
                } else {
                    animatorTv.setDuration(300);
                    animatorIv.setDuration(300);
                }
            } else {
                tv_name.setTextColor(tabTextColor);
                //属性动画
                createViewPropertyAnimator(tv_name, 1.0f, 1.0f, 300);
                createViewPropertyAnimator(iv_img, 1.0f, 1.0f, 300);
            }

        }
    }

    private ViewPropertyAnimator createViewPropertyAnimator(View view, float scaleX, float scaleY, long duration) {
        return ViewPropertyAnimator.animate(view).scaleX(scaleX).scaleY(scaleY).setDuration(duration);
    }

    private void showImage(ImageView iv, String url) {
        int radius = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_10px);
        Glide.with(mContext)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(mContext, radius, 0,
                        RoundedCornersTransformation.CornerType.ALL))
                .into(iv);
    }

    /**
     * 滑动
     *
     * @param position
     * @param offset
     */
    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            //不居中的
            // smoothScrollTo(newScrollX, 0);
            //以下是当tab很多时，点击屏幕右边的，点击的那个居中!!!
            int k = tabsContainer.getChildAt(position).getMeasuredWidth();
            int l = tabsContainer.getChildAt(position).getLeft() + offset;
            int i2 = l + k / 2 - this.getMeasuredWidth() / 2;
            smoothScrollTo(i2, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int height = getHeight();

        // 设置指示器画笔的颜色
        indicatorPaint.setColor(indicatorColor);
        // 默认指示器对应到当前TAB选中项
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // 如果有偏移，开始在当前和下一个选项卡之间插入左、右坐标
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        //线型
        if (indicatorShape == SHAPE_LINE) {
            float left = lineLeft + tabPadding;
            float top = height - indicatorHeight;
            float right = lineRight - tabPadding;
            float bottom = height;
            canvas.drawRect(left, top, right, bottom, indicatorPaint);
        } else {
            float cx0 = (lineLeft + lineRight) / 2;
            float cy0 = height * 5 / 6;
            float radius0 = indicatorHeight / 2;
            canvas.drawCircle(cx0, cy0, radius0, indicatorPaint);
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightDp) {
        this.indicatorHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, indicatorLineHeightDp, getResources().getDisplayMetrics());
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
    }

    public void setTextSize(int textSizeSp) {
        this.tabTextSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, textSizeSp, getResources().getDisplayMetrics());
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
    }

    public void setSelectedTextColor(int textColor) {
        this.selectedTabTextColor = textColor;
    }

    public void setTabPaddingLeftRight(int paddingDp) {
        this.tabPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, paddingDp, getResources().getDisplayMetrics());
    }
    public void setIndicatorShape(int shape) {
        this.indicatorShape = shape;
    }

    private boolean isSmall(float positionOffset) {
        return Math.abs(positionOffset) < 0.0001;
    }

    protected void animateFadeScale(View left, View right, float positionOffset, int position) {
        if (mState != State.IDLE) {
            //Log.w(TAG, "=> positionOffset = " + positionOffset);
            if (left != null) {
                TextView tv_name = (TextView) left.findViewById(R.id.tv_boutique_museum_name);
                J1ImageView iv_img = (J1ImageView) left.findViewById(R.id.iv_boutique_museum_img);

                //'#45c01a' -> '#2b2b2b'
                //rgb(69, 192, 26) -> rgb(43, 43, 43)
                int red = (int) (69 - 26 * positionOffset);
                int green = (int) (192 - 149 * positionOffset);
                int blue =  (int) (26 + 17 * positionOffset + 1);//加1是为了补偿精度损失带来的计算错误
                //Log.w(TAG, "rgb = " + String.format("rgb(%s, %s, %s)", red, green, blue));
                int tempColor = Color.rgb(red, green, blue);
                tv_name.setTextColor(tempColor);

                float mScale = 1 + zoomMax - zoomMax * positionOffset;
                ViewHelper.setScaleX(tv_name, mScale);
                ViewHelper.setScaleY(tv_name, mScale);
                ViewHelper.setScaleX(iv_img, mScale);
                ViewHelper.setScaleY(iv_img, mScale);
            }
            if (right != null) {
                TextView tv_name = (TextView) right.findViewById(R.id.tv_boutique_museum_name);
                J1ImageView iv_img = (J1ImageView) right.findViewById(R.id.iv_boutique_museum_img);

                //'#2b2b2b' -> '#45c01a'
                //rgb(43, 43, 43) -> rgb(69, 192, 26)
                int red = (int) (43 + 26 * positionOffset + 1);
                int green = (int) (43 + 149 * positionOffset + 1);
                int blue =  (int) (43 - 17 * positionOffset);//加1是为了补偿精度损失带来的计算错误
                //Log.w(TAG, "rgb = " + String.format("rgb(%s, %s, %s)", red, green, blue));
                int tempColor = Color.rgb(red, green, blue);
                tv_name.setTextColor(tempColor);

                float mScale = 1 + zoomMax * positionOffset;
                ViewHelper.setScaleX(tv_name, mScale);
                ViewHelper.setScaleY(tv_name, mScale);
                ViewHelper.setScaleX(iv_img, mScale);
                ViewHelper.setScaleY(iv_img, mScale);
            }
        }
    }

    /**
     * 设置精品馆数据源
     *
     * @param list
     */
    public void setDataSource(List<BoutiqueMuseumItem> list) {
        this.dataList = list;
    }

    /**
     * 返回标识当前用户是点击切换页面还是左右滑动切换页面
     *
     * @return
     */
    public boolean isClickChangePage() {
        return isClickChangePage;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /****
     * 设置状态监听
     *
     * @param listener listener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    /**
     * 设置点击选择精品馆监听
     *
     * @param onSelectBoutiqueMuseumListener
     */
    public void setOnSelectBoutiqueMuseumListener(OnSelectBoutiqueMuseumListener onSelectBoutiqueMuseumListener) {
        this.mOnSelectBoutiqueMuseumListener = onSelectBoutiqueMuseumListener;
    }

    public interface OnSelectBoutiqueMuseumListener{
        void onSelect(int position);
    }

}
