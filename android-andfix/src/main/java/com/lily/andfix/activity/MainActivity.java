package com.lily.andfix.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lily.andfix.R;
import com.lily.andfix.application.AndFixApplication;

import java.io.IOException;

public class MainActivity extends Activity {

    TextView tvHotFix;
    Button btnHotFix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHotFix = (TextView) findViewById(R.id.tv_show_hot_fix);
        btnHotFix = (Button) findViewById(R.id.btn_hot_fix);
        btnHotFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AndFixApplication.patchManager.addPatch(AndFixApplication.filePath);
                    Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        setText();
    }

    private void setText() {
        tvHotFix.setText("AndFix应用 2.0.0(打补丁后)");
    }
}
