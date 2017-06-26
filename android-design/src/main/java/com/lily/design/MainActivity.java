package com.lily.design;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lily.design.mvc.MVCActivity;
import com.lily.design.mvp.MVPActivity;
import com.lily.design.mvvm.MVVMActivity;

public class MainActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
    }

    /**
     *
     * @param view
     */
    public void openMVC(View view) {
        startActivity(new Intent(context, MVCActivity.class));
    }

    /**
     *
     * @param view
     */
    public void openMVP(View view) {
        startActivity(new Intent(context, MVPActivity.class));
    }

    /**
     *
     * @param view
     */
    public void openMVVM(View view) {
        startActivity(new Intent(context, MVVMActivity.class));
    }
}
