package com.android.lily.view.slidingtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.lily.R;

import java.util.List;

public class SlidingTabBar extends LinearLayout{

    private View slidingTabBarView;
    private Context mContext;
    private LayoutInflater inflater;
    private SlidingTab slidingTab;
    private ViewPager viewPager;

    //System attrs
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    public SlidingTabBar(Context context) {
        this(context, null);
    }

    public SlidingTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //设置父布局的LinearLayout方向
        setOrientation(VERTICAL);
        slidingTabBarView = inflater.inflate(R.layout.sliding_tab_bar, this, true);
        initWidget();

        // 根据xml配置的属性设置UI
        initAttributeFromXml(attrs);
    }

    private void initWidget() {
        slidingTab = (SlidingTab) slidingTabBarView.findViewById(R.id.st_tabs);
        viewPager = (ViewPager) slidingTabBarView.findViewById(R.id.vp_pager);
    }

    private void initAttributeFromXml(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        // 获取系统属性 (android:textSize and android:textColor)
        TypedArray ta = mContext.obtainStyledAttributes(attrs, ATTRS);
        slidingTab.setTextSize(ta.getDimensionPixelSize(0, 12));
        slidingTab.setTextColor(ta.getColor(1, 0xFF666666));
        ta.recycle();

        // 获取自定义属性
        TypedArray typed = this.mContext.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        if (typed == null) {
            return;
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor)) {
            slidingTab.setIndicatorColor(typed.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, 0xFF666666));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor)) {
            slidingTab.setUnderlineColor(typed.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, 0x1A000000));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsDividerColor)) {
            slidingTab.setDividerColor(typed.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, 0x1A000000));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight)) {
            slidingTab.setIndicatorHeight(typed.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, 8));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight)) {
            slidingTab.setUnderlineHeight(typed.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, 2));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsDividerPaddingTopBottom)) {
            slidingTab.setDividerPaddingTopBottom(typed.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPaddingTopBottom, 12));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight)) {
            slidingTab.setTabPaddingLeftRight(typed.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, 20));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsScrollOffset)) {
            slidingTab.setScrollOffset(typed.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, 52));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsTabBackground)) {
            slidingTab.setTabBackground(typed.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, -1));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsShouldExpand)) {
            slidingTab.setShouldExpand(typed.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, false));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsTextSelectedColor)) {
            slidingTab.setSelectedTextColor(typed.getColor(R.styleable.PagerSlidingTabStrip_pstsTextSelectedColor, 0xFF45c01a));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsScaleZoomMax)) {
            slidingTab.setZoomMax(typed.getFloat(R.styleable.PagerSlidingTabStrip_pstsScaleZoomMax, 0.3f));
        }
        if (typed.hasValue(R.styleable.PagerSlidingTabStrip_pstsSmoothScrollWhenClickTab)) {
            slidingTab.setSmoothScrollWhenClickTab(typed.getBoolean(R.styleable.PagerSlidingTabStrip_pstsSmoothScrollWhenClickTab, true));
        }

        typed.recycle();
    }

    /**
     * 设置标题和内容数据（Fragment）
     *
     * @param fm
     * @param data
     */
    public void setContent(FragmentManager fm, List<SlidingTabModel> data) {
        ContentPagerAdapter adapter = new ContentPagerAdapter(fm, data);
        //给ViewPager设置数源
        viewPager.setAdapter(adapter);
        int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        slidingTab.setViewPager(viewPager);
    }
}
