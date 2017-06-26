package com.android.lily.view.slidingtab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class SlidingTabItem extends FrameLayout {

    private GestureDetector gestureDetector;
    private DoubleSingleClickListener mDoubleSingleClickListener;

    public SlidingTabItem(Context context) {
        this(context, null);
    }

    public SlidingTabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(getContext(), new MyGestureDetector());
        setClickable(true);
    }

    private final class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mDoubleSingleClickListener != null) {
                mDoubleSingleClickListener.onDoubleTap(e);
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mDoubleSingleClickListener != null) {
                mDoubleSingleClickListener.onSingleTapConfirmed(e);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public interface DoubleSingleClickListener {
        void onDoubleTap(MotionEvent e);

        void onSingleTapConfirmed(MotionEvent e);
    }

    public void setDoubleSingleClickListener(DoubleSingleClickListener mDoubleSingleClickListener) {
        this.mDoubleSingleClickListener = mDoubleSingleClickListener;
    }
}
