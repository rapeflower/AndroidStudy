package com.lily.guide;

import android.app.Activity;

import com.lily.guide.fill.FillTextView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private FillTextView fillTextView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillTextView = findViewById(R.id.fv);
        textView = findViewById(R.id.tv_fills);

        fillTextView.setEditTag(null, null);
        fillTextView.setText("<fill>大家好，我是<fill>，我来自<fill>我就是来填个空而已<fill>。");
        fillTextView.displayUnderline(true);
    }

    public void clickBtn(View view) {
        String t = "";
        for (String text : fillTextView.getFillTexts()) {
            t += text;
            t +=",";
        }

        textView.setText(t);
    }
}
