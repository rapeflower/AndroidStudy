package com.android.lily.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.lily.R;

/**
 * Created by rape flower on 17/6/26.
 * @describe 默认图适配
 */
public class DefaultPicActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_pic);
    }
}
