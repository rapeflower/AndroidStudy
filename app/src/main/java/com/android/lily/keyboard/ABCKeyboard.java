package com.android.lily.keyboard;

import android.content.Context;

import com.android.lily.R;

public class ABCKeyboard extends BaseKeyboard {

    public static final int DEFAULT_ABC_XML_LAYOUT = R.xml.keyboard_abc;

    public ABCKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public ABCKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public ABCKeyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public ABCKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    public boolean disposeKey(int primaryCode) {
        return false;
    }

    @Override
    public Padding getPadding() {
        return new Padding(0, 10, 0, 10);
    }
}
