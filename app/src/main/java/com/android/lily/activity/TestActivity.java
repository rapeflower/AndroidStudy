package com.android.lily.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.lily.R;
import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Author on 17/5/24.
 */

public class TestActivity extends Activity{

    ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        iv = (ImageView) findViewById(R.id.iv_img);

        Glide.with(this)
                .load(R.drawable.ym)
                .bitmapTransform(new RoundedCornersTransformation(this, 20, 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(iv);
    }
}
