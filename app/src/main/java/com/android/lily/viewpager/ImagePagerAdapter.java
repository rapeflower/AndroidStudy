package com.android.lily.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.lily.R;

/**
 * Created by lilei on 2017/5/20.
 */

public class ImagePagerAdapter extends CarouselPagerAdapter<CarouselViewPager>{

    private LayoutInflater inflater;

    public ImagePagerAdapter(Context context, CarouselViewPager viewPager) {
        super(viewPager);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    int[] imgRes = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
    };

    @Override
    public Object instantiateRealItem(ViewGroup container, int position) {
//        View item_view = inflater.inflate(R.layout.item_viewpager, null);
//        ImageView view = (ImageView) item_view.findViewById(R.id.iv_img);

        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setAdjustViewBounds(true);
        view.setImageResource(imgRes[position]);
        view.setLayoutParams(new LinearLayout.LayoutParams(900, 400));

        container.addView(view);
        return view;
    }

    @Override
    public int getRealDataCount() {
        return imgRes != null ? imgRes.length : 0;
    }
}
