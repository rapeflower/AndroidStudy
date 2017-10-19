package com.android.lily.view.boutiquemuseum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.lily.R;
import com.android.lily.view.special.J1ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author rape flower
 * @descripe 精品馆横向滑动View
 */
public class BoutiqueMuseumTabView extends HorizontalScrollView {

    //字体大小、颜色
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    //布局参数
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private LinearLayout tabsContainer;

    private int tabCount;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private Paint indicatorPaint;
    private int indicatorColor = 0xFF212630;
    //该属性表示里面的TAB是否均分整个SlidingTab控件的宽,
    // true是,false不均分,从左到右排列,默认false
    private boolean shouldExpand = false;

    private int scrollOffset = 52;
    private int indicatorHeight = 1;
    private int tabPadding = 20;

    private int tabTextSize = 12;
    private int tabTextColor = 0xFF666666;
    private int selectedTabTextColor = 0xFF45c01a;//默认选择TAB的文字颜色
    private static final int SHAPE_LINE = 0;//指示器View的形状
    private static final int SHAPE_CIRCLE = 1;
    private int indicatorShape = SHAPE_LINE;

    private int lastScrollX = 0;//记录滚动结束时的x坐标
    private Locale locale;
    private Context mContext;
    private LayoutInflater mInflater;
    private Paint mPaintTabText = new Paint();
    private List<BoutiqueMuseumItem> dataList = null;
    OnSelectBoutiqueMuseumListener mOnSelectBoutiqueMuseumListener;

    public BoutiqueMuseumTabView(Context context) {
        this(context, null);
    }

    public BoutiqueMuseumTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoutiqueMuseumTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataList = new ArrayList<BoutiqueMuseumItem>();
        setFillViewport(true);
        setWillNotDraw(false);//防止onDraw方法不执行

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
        defaultTabLayoutParams.rightMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_20px);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }

        mPaintTabText.setAntiAlias(true);
    }

    /**
     * 数据改变
     */
    public void notifyDataSetChanged() {
        if (tabsContainer == null || dataList == null || dataList.size() == 0) {
            return;
        }
        tabsContainer.removeAllViews();
        tabCount = dataList.size();
        for (int i = 0; i < tabCount; i++) {
            BoutiqueMuseumItem bmt = dataList.get(i);
            addTab(i, bmt.getName());
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                currentPosition = 0;
                scrollToChild(currentPosition, 0);

//                updateTabStyles();
            }
        });
    }

    /**
     * 添加TAB选项
     *
     * @param position
     * @param title
     */
    private void addTab(final int position, String title) {
        View tabItem = mInflater.inflate(R.layout.item_boutique_museum, null);
        TextView tv_name = (TextView) tabItem.findViewById(R.id.tv_boutique_museum_name);
        tv_name.setText(title);

        J1ImageView ivBMImg = (J1ImageView) tabItem.findViewById(R.id.iv_boutique_museum_img);
        String url = "https://img01.j1.com/upload/pic/homepage/1494300131640.jpg";
        int radius = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_6px);
        Glide.with(mContext)
                .load(url)
                .bitmapTransform(new CenterCrop(mContext), new RoundedCornersTransformation(mContext, radius, 0,
                        RoundedCornersTransformation.CornerType.ALL))
                .into(ivBMImg);
        tabsContainer.addView(tabItem, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);

        tabItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //单击事件回调
                if (mOnSelectBoutiqueMuseumListener!=null){
                    mOnSelectBoutiqueMuseumListener.onSelect(position);
                }
                currentPosition = position;
                scrollToChild(position, 0);//滚动HorizontalScrollView
            }
        });
    }

    /**
     * 恢复指示器的初始状态
     */
    private void restoreDrawIndicatorStatus() {
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        for (BoutiqueMuseumItem bmt : dataList) {
             if (bmt.isDrawIndicatorLine()) {
                 bmt.setDrawIndicatorLine(false);
             }
        }
    }

    /**
     * 更新TAB文本样式
     */
    public void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            LinearLayout layout = (LinearLayout) tabsContainer.getChildAt(i);
            if (layout == null) {
                return;
            }

            for (int j = 0; j < layout.getChildCount(); j++) {
                View v = layout.getChildAt(j);
                if (v instanceof TextView) {
                    TextView tab = (TextView) v;
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                    if (j == 0) {
                        tab.setTextColor(tabTextColor);
                    } else {
                        tab.setTextColor(selectedTabTextColor);
                    }
                }
            }
        }
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

    /**
     * 设置精品馆数据源
     *
     * @param list
     */
    public void setDataSource(List<BoutiqueMuseumItem> list) {
        this.dataList = list;
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
