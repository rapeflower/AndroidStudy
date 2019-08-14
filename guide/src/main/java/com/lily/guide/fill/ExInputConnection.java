package com.lily.guide.fill;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExInputConnection extends BaseInputConnection {

    private InputListener mListener;

    public ExInputConnection(View targetView, boolean fullEditor, InputListener listener) {
        super(targetView, fullEditor);
        this.mListener = listener;
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        if (!isEmoji(text)) {
            mListener.onTextInput(text);
        }
        return super.commitText(text, newCursorPosition);
    }

    /**
     * 过滤Emoji表情
     */
    private boolean isEmoji(CharSequence text) {
        Pattern pattern = Pattern.compile("[^\\u0000-\\uFFFF]");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        // 软键盘的删除键 DEL 无法直接监听，自己发送del事件
        if (beforeLength == 1 && afterLength == 0) {
            return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
        }
        return super.deleteSurroundingText(beforeLength, afterLength);
    }

    interface InputListener {
        void onTextInput(CharSequence text);
        void onDeleteWord();
    }
}
