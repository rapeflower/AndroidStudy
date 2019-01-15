package com.android.lily.keyboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.lily.R;

import java.util.List;

public class KeyboardWithSearchView extends LinearLayout {

    private static final int MAX_VISIBLE_SIZE = 3;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private BaseKeyboardView mBaseKeyboardView;
    private LinearLayout mKeyboardViewContainer;
    private EditText mEditText;

    public KeyboardWithSearchView(Context context) {
        super(context);
        init(context);
    }

    public KeyboardWithSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyboardWithSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KeyboardWithSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     *
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycler_keyboard_view, this, true);
        mEditText = view.findViewById(R.id.hide_edit_text);
        mRecyclerView = view.findViewById(R.id.search_recycler_view);
        mBaseKeyboardView = view.findViewById(R.id.keyboard_view);
        mKeyboardViewContainer = view.findViewById(R.id.keyboard_container);
    }

    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public BaseKeyboardView getBaseKeyboardView() {
        return mBaseKeyboardView;
    }

    public LinearLayout getKeyboardViewContainer() {
        return mKeyboardViewContainer;
    }

    public EditText getEditText() {
        return mEditText;
    }

    /**
     *
     * @param adapter
     * @param manager
     * @param itemDecoration
     */
    protected void initRecyclerView(KeyboardSearchBaseAdapter adapter, RecyclerView.LayoutManager manager, RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     *
     * @param list
     * @param hasFixedSize
     */
    protected void setSearchResult(List list, boolean hasFixedSize) {
        if (mRecyclerView.getAdapter() == null) {
            throw new RuntimeException("this view has not invoked init method");
        }
        mRecyclerView.getLayoutManager().scrollToPosition(0);
        if (list == null || list.size() == 0) {
            mRecyclerView.setVisibility(GONE);
        } else {
            int height = Utils.dipToPx(mContext, Math.min(3, list.size()) * 49) + Math.min(3, list.size());
            ViewGroup.LayoutParams lp =  mRecyclerView.getLayoutParams();
            if (lp != null) {
                lp.height = height;
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                mRecyclerView.setLayoutParams(layoutParams);
            }
            mRecyclerView.setVisibility(VISIBLE);
        }
        mRecyclerView.setHasFixedSize(hasFixedSize);

        KeyboardSearchBaseAdapter adapter = (KeyboardSearchBaseAdapter) mRecyclerView.getAdapter();
        adapter.setAdapterData(list);
        adapter.notifyDataSetChanged();
    }
}
