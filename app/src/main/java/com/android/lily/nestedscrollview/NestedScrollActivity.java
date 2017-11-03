package com.android.lily.nestedscrollview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.android.lily.R;

/***********
 *
 * @Author rape flower
 * @Date 2017-11-03 10:10
 * @Describe
 *
 */
public class NestedScrollActivity extends Activity{


    private NestedListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll);
        mListView = (NestedListView) findViewById(R.id.list_view);
        String[] arr = new String[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + "";
        }
        mListView.setAdapter(new ArrayAdapter<>(this, R.layout.list_item_layout, R.id.text_name, arr));
    }
}
