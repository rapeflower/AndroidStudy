package com.android.lily.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.lily.R;
import com.android.lily.view.special.J1ImageView;
import com.android.lily.view.special.RoundImageViewByXfermode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Author on 17/5/24.
 */

public class TestActivity extends Activity{

    ImageView iv;
    RoundImageViewByXfermode rivxf;
    J1ImageView j1ImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        iv = (ImageView) findViewById(R.id.iv_img);
        rivxf = (RoundImageViewByXfermode) findViewById(R.id.riv_xf_img);
        j1ImageView = (J1ImageView) findViewById(R.id.j1ImageView);

        Glide.with(this)
                .load(R.drawable.ym)
                .bitmapTransform(new RoundedCornersTransformation(this, 10, 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(iv);

        Glide.with(this)
                .load("http://img.j1.cn/upload/pic/brandStreet/1499131077234.jpg")
                .bitmapTransform(new CenterCrop(this), new RoundedCornersTransformation(this, 10, 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(j1ImageView);
    }
}
