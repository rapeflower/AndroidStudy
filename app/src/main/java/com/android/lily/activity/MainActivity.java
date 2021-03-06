package com.android.lily.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.lily.R;
import com.android.lily.colortrackview.ViewPagerUseActivity;
import com.android.lily.nestedscrollview.NestedScrollActivity;

/**
 * <pre>
 * http://blog.csdn.net/lmj623565791/article/details/43131133
 * </pre>
 */
public class MainActivity extends ListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getListView().setAdapter(
				new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, new String[] {
						"圆形菜单1", "圆形菜单2", "测试", "RoundView", "默认图适配", "Icon滑动tab",
						"Color Track ViewPager", "Nested Scroll", "Keyboard"}));

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent intent = null;
		if (position == 0) {
			intent = new Intent(this, CCBActivity.class);
		} else if (position == 2) {
			intent = new Intent(this, SlidingTabBarActivity.class);
		} else if (position == 3) {
			intent = new Intent(this, TestActivity.class);
		} else if (position == 4) {
			intent = new Intent(this, DefaultPicActivity.class);
		} else if (position == 5) {
			intent = new Intent(this, AbroadBoutiqueTestActivity.class);
		} else if (position == 6) {
			intent = new Intent(this, ViewPagerUseActivity.class);
		} else if (position == 7) {
			intent = new Intent(this, NestedScrollActivity.class);
		} else if (position == 8) {
			intent = new Intent(this, KeyboardActivity.class);
		} else {
			intent = new Intent(this, CircleActivity.class);
		}
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri
					.parse("http://blog.csdn.net/lmj623565791?viewmode=contents"));
			startActivity(intent);
			return true;
		default:
			return false;
		}
	}

}
