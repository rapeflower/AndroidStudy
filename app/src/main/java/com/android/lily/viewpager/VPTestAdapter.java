package com.android.lily.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.lily.R;

/**
 * Created by lilei on 2017/5/21.
 */
public class VPTestAdapter extends PagerAdapter{

    private LayoutInflater inflater;
    private ViewPager mViewPager;

    int[] imgRes = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
    };

    public VPTestAdapter(Context context, ViewPager viewPager) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mViewPager = viewPager;
    }

    @Override
    public int
    getCount() {
        return imgRes != null ? imgRes.length : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        View item_view = inflater.inflate(R.layout.item_viewpager, null);
        ImageView view = (ImageView) item_view.findViewById(R.id.iv_img);

//        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setAdjustViewBounds(true);
        view.setImageResource(imgRes[position]);
        view.setLayoutParams(new LinearLayout.LayoutParams(900, 400));

        container.addView(item_view);
        return item_view;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public final void finishUpdate(ViewGroup container) {
    }
}