package com.android.lily.view.special;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by rape flower on 17/6/26.
 */
public class SpecialImageView extends ImageView{


    public SpecialImageView(Context context) {
        this(context, null);
    }

    public SpecialImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpecialImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
