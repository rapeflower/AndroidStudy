package com.android.lily.keyboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.IntegerRes;
import android.text.Editable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import com.android.lily.R;

public abstract class BaseKeyboard extends Keyboard implements KeyboardView.OnKeyboardActionListener {

    private EditText mEditText;
    private View mNextFocusView;
    private Context mContext;
    private KeyStyle mKeyStyle;

    public BaseKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
        this.mContext = context;
    }

    public BaseKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
        this.mContext = context;
    }

    public BaseKeyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
        this.mContext = context;
    }

    public BaseKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
        this.mContext = context;
    }

    public void setEditText(EditText editText) {
        this.mEditText = editText;
    }

    public void setNextFocusView(View nextFocusView) {
        this.mNextFocusView = nextFocusView;
    }

    public void setKeyStyle(KeyStyle keyStyle) {
        this.mKeyStyle = keyStyle;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public View getNextFocusView() {
        return mNextFocusView;
    }

    public KeyStyle getKeyStyle() {
        return mKeyStyle;
    }

    public int getKeyCode(@IntegerRes int resId) {
        return mContext.getResources().getInteger(resId);
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (null != mEditText && mEditText.hasFocus() && !disposeKey(primaryCode)) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            int end = mEditText.getSelectionEnd();
            if (end > start) {
                editable.delete(start, end);
            }
            if (primaryCode == KEYCODE_DELETE) {
                if (!TextUtils.isEmpty(editable)) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == getKeyCode(R.integer.hide_keyboard)) {
                hideKeyboard();
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void hideKeyboard() {
        if (mNextFocusView != null) {
            mNextFocusView.requestFocus();
        }
    }

    public float convertSpToPixels(Context context, float sp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public Padding getPadding() {
        return new Padding(0, 0, 0, 0);
    }

    public static class Padding {
        int left;
        int top;
        int right;
        int bottom;

        public Padding(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    /**
     * 处理输入键
     *
     * @param primaryCode
     * @return
     */
    public abstract boolean disposeKey(int primaryCode);

    public interface KeyStyle {

        Drawable getKeyBackground(Key key);

        Float getKeyTextSize(Key key);

        Integer getKeyTextColor(Key key);

        CharSequence getKeyLabel(Key key);
    }


    public static class DefaultKeyStyle implements KeyStyle {

        @Override
        public Drawable getKeyBackground(Key key) {
            return key.iconPreview;
        }

        @Override
        public Float getKeyTextSize(Key key) {
            return null;
        }

        @Override
        public Integer getKeyTextColor(Key key) {
            return null;
        }

        @Override
        public CharSequence getKeyLabel(Key key) {
            return key.label;
        }
    }
}
