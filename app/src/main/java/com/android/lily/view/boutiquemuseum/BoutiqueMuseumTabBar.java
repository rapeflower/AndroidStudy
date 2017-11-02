package com.android.lily.view.boutiquemuseum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.android.lily.R;

import java.util.List;

/**
 * @author rape flower
 * @descripe 滑动标签控件
 * <p>
 * </p>
 */
public class BoutiqueMuseumTabBar extends LinearLayout {

    private static final String TAG = BoutiqueMuseumTabBar.class.getSimpleName();
    private Context mContext;
    private LayoutInflater inflater;
    private BoutiqueMuseumTab bmtTab;
    private ViewPager viewPager;
    private ContentPagerAdapter pagerAdapter;
    private FragmentManager fragmentManager;
    private int tabBackgroundColor = Color.WHITE;//TAB背景颜色
    OnSlidingTabBarListener mOnSlidingTabBarListener;
    //记录点击位置
    int oldPosition = 0;//默认选中第一个Tab
    int newPosition = -1;

    //系统属性：字体大小、字体颜色
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };

    /***
     * 滑动的方向
     */
    enum ScrollOrientation {
        //None为Begin end
        Left, Right, None
    }

    private boolean isScrolling;
    private ScrollOrientation orientation = ScrollOrientation.None;
    private float lastOffset = 0.0f;
    private boolean isCalledListener;
    private int currentPosition;

    public BoutiqueMuseumTabBar(Context context) {
        this(context, null);
    }

    public BoutiqueMuseumTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoutiqueMuseumTabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //设置父布局的LinearLayout方向
        setOrientation(VERTICAL);
        inflater.inflate(R.layout.boutique_museum_tab_bar, this, true);
        initWidget();

        // 根据xml配置的属性设置UI
//        initAttributeFromXml(attrs);
    }

    private void initWidget() {
        bmtTab = (BoutiqueMuseumTab) findViewById(R.id.bmt_boutique_museum);
        viewPager = (ViewPager) findViewById(R.id.vp_pager);
    }

    private void initAttributeFromXml(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        // 获取系统属性 (android:textSize and android:textColor)
        TypedArray ta = mContext.obtainStyledAttributes(attrs, ATTRS);
        bmtTab.setTextSize(ta.getDimensionPixelSize(0, 12));
        bmtTab.setTextColor(ta.getColor(1, 0xFF666666));
        ta.recycle();
    }

    /**
     * 获取ViewPager
     *
     * @return
     */
    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * 返回标识当前用户是点击切换页面还是左右滑动切换页面
     *
     * @return
     */
    public boolean isClickChangePage() {
        return bmtTab.isClickChangePage();
    }

    /**
     * 设置TAB的高
     *
     * @param height
     */
    public void setTabHeight(int height) {
        LayoutParams lp = (LayoutParams) bmtTab.getLayoutParams();
        lp.height = height;
        bmtTab.setLayoutParams(lp);
    }

    /**
     * 设置TAB的背景颜色
     *
     * @param color
     */
    public void setTabBackgroundColor(int color) {
        bmtTab.setBackgroundColor(color);
    }

    /**
     * 设置TAB是否显示
     * @param visibility
     */
    public void setSlidingTabVisibility(int visibility) {
        bmtTab.setVisibility(visibility);
    }

    /**
     * 获取TAB是否显示
     */
    public int getSlidingTabVisibility() {
        return bmtTab.getVisibility();
    }

    /**
     * 设置页面切换监听
     *
     * @param listener
     */
    public void setDelegatePageListener(ViewPager.OnPageChangeListener listener) {
        bmtTab.setOnPageChangeListener(listener);
    }

    /***
     * 设置Tab的单击监听
     *
     * @param onSelectBoutiqueMuseumListener
     */
    public void setOnSelectBoutiqueMuseumListener(BoutiqueMuseumTab.OnSelectBoutiqueMuseumListener onSelectBoutiqueMuseumListener) {
        bmtTab.setOnSelectBoutiqueMuseumListener(onSelectBoutiqueMuseumListener);
    }

    /**
     * 设置FragmentManager
     *
     * @param fragmentManager
     */
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * 设置标题和内容数据（Fragment）
     *
     * @param data
     */
    public void setAdapter(List<BoutiqueMuseumItem> data) {
        if (fragmentManager == null) {
            throw new NullPointerException("Please set your FragmentManager for Viewpager");
        }

        if (pagerAdapter == null) {
            pagerAdapter = new ContentPagerAdapter(fragmentManager, data);
            //给ViewPager设置数源
            viewPager.setAdapter(pagerAdapter);
        }
        bindToViewPager();
    }

    /**
     * 更新滑动标签的条目数据
     */
    public void notifyDataSetChanged() {
        if (pagerAdapter != null) {
            bindToViewPager();
        }
    }

    /**
     * 关联ViewPager
     *
     * @param
     */
    private void bindToViewPager() {
        pagerAdapter.notifyDataSetChanged();
        bmtTab.setViewPager(viewPager);
    }

    /**
     * 设置SlidingTabBar的监听
     *
     * @param offsetPercent           位置偏移的百分比,取值范围[0, 1]
     * @param onSlidingTabBarListener SlidingTabBar的监听回调
     */
    public void setOnSlidingTabBarListener(float offsetPercent, OnSlidingTabBarListener onSlidingTabBarListener) {
        this.mOnSlidingTabBarListener = onSlidingTabBarListener;
        handleSlideTabBarListener(OnSlidingTabBarListener.TYPE_OFFSET_PERCENT, offsetPercent, -1);
    }

    /**
     * 设置SlidingTabBar的监听
     *
     * @param offsetPixels            位置偏移的像素值
     * @param onSlidingTabBarListener SlidingTabBar的监听回调
     */
    public void setOnSlidingTabBarListener(int offsetPixels, OnSlidingTabBarListener onSlidingTabBarListener) {
        this.mOnSlidingTabBarListener = onSlidingTabBarListener;
        handleSlideTabBarListener(OnSlidingTabBarListener.TYPE_OFFSET_PIXELS, -1, offsetPixels);
    }

    /**
     * 处理SlidingTabBar的监听
     */
    private void handleSlideTabBarListener(final int offsetType, final float offsetPercent, final int offsetPixels) {
        if (bmtTab == null) {
            return;
        }

        //设置页面切换监听
        bmtTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 页面滑动
             * @param position 当前页面，及你点击滑动的页面的下标
             * @param positionOffset 当前页面偏移的百分比
             * @param positionOffsetPixels 当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isClickChangePage()) {
                    return;
                }
                boolean isReturn = confirmScrollOrientation(position, positionOffset);
                if (isReturn) {
                    return;
                }
                //已经调用过监听或者不是向左或者向右滑时，直接return即可
                if (isCalledListener || orientation == ScrollOrientation.None)
                    return;
                switch (offsetType) {
                    case OnSlidingTabBarListener.TYPE_OFFSET_PERCENT:
                        /**当前页面偏移的百分比大于等于offsetPercent的值时，回调onSlidePageLoad(..)*/
                        if (isOnSlidePageLoadByOffsetPercent(offsetPercent, positionOffset)) {
                            handleOnSlidePageLoad();
                        }
                        break;
                    case OnSlidingTabBarListener.TYPE_OFFSET_PIXELS:
                        //当前页面偏移的像素值大于等于offsetPixels的值时，回调onSlidePageLoad(..)
                        if (positionOffsetPixels > offsetPixels) {
                            handleOnSlidePageLoad();
                        }
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                //当前页面被选中后，重新给变量oldPosition赋值
                oldPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && !isClickChangePage()) {
                    isCalledListener = false;
                    if (mOnSlidingTabBarListener != null) {
                        mOnSlidingTabBarListener.onIdleResetSlidePageLoadFlag(currentPosition);
                    }
                }
                isScrolling = state == ViewPager.SCROLL_STATE_DRAGGING;

            }
        });

        bmtTab.setOnSelectBoutiqueMuseumListener(new BoutiqueMuseumTab.OnSelectBoutiqueMuseumListener() {
            @Override
            public void onSelect(int position) {
                newPosition = position;
                int offset = Math.abs(newPosition - oldPosition);
                android.util.Log.w(TAG,"D-value offset = " + offset);
                //offset >= 2 fragment没有预加载了布局 offset < 2 fragment预加载了布局
                if (mOnSlidingTabBarListener != null)
                    mOnSlidingTabBarListener.onSlidingTabItemClick(position, offset < 2);
                //更新oldPosition的值
                oldPosition = newPosition;
            }
        });
    }

    /***
     *
     * @param offsetPercent
     * @param positionOffset
     * @return
     */
    private boolean isOnSlidePageLoadByOffsetPercent(float offsetPercent, float positionOffset) {
        return orientation == ScrollOrientation.Right ? positionOffset > offsetPercent
                : (1 - positionOffset) > offsetPercent;
    }

    /***
     * 调用监听
     */
    private void handleOnSlidePageLoad() {
        android.util.Log.w(TAG,"position = " + currentPosition + " , orientation = " + orientation);
        isCalledListener = true;
        if (mOnSlidingTabBarListener != null) {
            mOnSlidingTabBarListener.onSlidePageLoad(currentPosition);
        }
    }

    /***
     * 确定向左还是向右滑
     * @param position 当前页面，及你点击滑动的页面的下标
     * @param positionOffset 当前页面偏移的百分比
     * @return
     */
    private boolean confirmScrollOrientation(int position, float positionOffset) {

        if (!isScrolling) {
            lastOffset = positionOffset;
            return true;
        }
        //positionOffset>0的时候判断才有效
        if (positionOffset > lastOffset && lastOffset > 0)
            orientation = ScrollOrientation.Right;
        else if (positionOffset < lastOffset && lastOffset > 0)
            orientation = ScrollOrientation.Left;
        else
            orientation = ScrollOrientation.None;
        currentPosition = orientation == ScrollOrientation.Right ? position + 1 : position;
        lastOffset = positionOffset;
        return false;
    }

    /**
     * SlidingTabBar的监听
     * 1.点击标题监听
     * 2.滑动页面的监听
     */
    public interface OnSlidingTabBarListener {
        /**
         * 位置偏移类型：
         * <p>
         * 位置偏移的百分比,取值范围[0, 1]
         * </p>
         */
        int TYPE_OFFSET_PERCENT = 1;
        /**
         * 位置偏移类型：
         * <p>
         * 位置偏移的像素值
         * </p>
         */
        int TYPE_OFFSET_PIXELS = 2;


        /**
         * 滑动：挨个滑有预加载，UI会初始化。偏移量>0.25，但是onVisible不走（但是要求load数据，目前通过回调进行加载数据），只有全部划完，执行onVisi（存在两次请求数据）
         * 返回滑动：但是onVisible不走（但是要求load数据），只有全部划完，执行onVisible（存在两次请求数据).所以在true时主动调用加载server的方法
         *
         * @param nextPosition 下一个fragment的标记
         */
        void onSlidePageLoad(int nextPosition);

        /**
         * 重设onSlidePageLoad()中flag的值，滑动结束之后，要将这次的滑动事件请求server的bool还原
         *
         * @param position 当前fragment的标记
         */
        void onIdleResetSlidePageLoadFlag(int position);

        /**
         * @param position        点击标签的下标
         * @param isLayoutPreload 标识fragment是否预加载了布局
         *                        isLayoutPreload: true表示预加载了布局，false则没有
         */
        void onSlidingTabItemClick(int position, boolean isLayoutPreload);
    }
}
