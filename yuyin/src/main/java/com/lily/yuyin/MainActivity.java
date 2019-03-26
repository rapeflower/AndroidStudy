package com.lily.yuyin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lily.yuyin.base.BaseActivity;
import com.lily.yuyin.recognizer.OnlineRecognizerActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void open(View view) {
        startActivity(new Intent(MainActivity.this, OnlineRecognizerActivity.class));
    }
}
