package com.android.lily.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.Toast;

import com.android.lily.R;
import com.android.lily.keyboard.ABCKeyboard;
import com.android.lily.keyboard.BaseKeyboard;
import com.android.lily.keyboard.KeyboardManager;
import com.android.lily.keyboard.NumberKeyboard;

public class KeyboardActivity extends Activity {

    private EditText editText1;
    private EditText editText2;

    private Context context;
    private KeyboardManager keyboardManagerNumber;
    private NumberKeyboard numberKeyboard;

    private KeyboardManager keyboardManagerAbc;
    private ABCKeyboard abcKeyboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        context = KeyboardActivity.this;

        editText1 = findViewById(R.id.edit1);
        editText2 = findViewById(R.id.edit2);
        editText1.setInputType(InputType.TYPE_CLASS_TEXT);
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

        keyboardManagerNumber = new KeyboardManager(this);
        initNumberKeyboard();
        keyboardManagerNumber.bindToEditor(editText2, numberKeyboard);

        keyboardManagerAbc = new KeyboardManager(this);
        keyboardManagerAbc.bindToEditor(editText1, new ABCKeyboard(context, ABCKeyboard.DEFAULT_ABC_XML_LAYOUT  ));
    }

    private void initNumberKeyboard() {
        numberKeyboard = new NumberKeyboard(context, NumberKeyboard.DEFAULT_NUMBER_XML_LAYOUT);
        numberKeyboard.setEnableDotInput(true);
        numberKeyboard.setActionDoneClickListener(new NumberKeyboard.ActionDoneClickListener() {

            @Override
            public void onActionDone(CharSequence charSequence) {
                if (TextUtils.isEmpty(charSequence) || charSequence.toString().equals("0") || charSequence.toString().equals("0.0")) {
                    Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    onNumberKeyActionDone();
                }
            }
        });

        numberKeyboard.setKeyStyle(new BaseKeyboard.KeyStyle() {
            @Override
            public Drawable getKeyBackground(Keyboard.Key key) {
                if (key.iconPreview != null) {
                    return key.iconPreview;
                } else {
                    return ContextCompat.getDrawable(context, R.drawable.key_number_bg);
                }
            }

            @Override
            public Float getKeyTextSize(Keyboard.Key key) {
                if (key.codes[0] == context.getResources().getInteger(R.integer.action_done)) {
                    return convertSpToPixels(context, 20f);
                }
                return convertSpToPixels(context, 24f);
            }

            @Override
            public Integer getKeyTextColor(Keyboard.Key key) {
                if (key.codes[0] == context.getResources().getInteger(R.integer.action_done)) {
                    return Color.WHITE;
                }
                return null;
            }

            @Override
            public CharSequence getKeyLabel(Keyboard.Key key) {
                return null;
            }
        });
    }

    public float convertSpToPixels(Context context, float sp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public void onNumberKeyActionDone() {
        editText1.requestFocus();
    }
}
