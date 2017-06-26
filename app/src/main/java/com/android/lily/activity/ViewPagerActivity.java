package com.android.lily.activity;

import android.app.Activity;
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
import com.android.lily.activity.CCBActivity;
import com.android.lily.activity.CircleActivity;
import com.android.lily.viewpager.CarouselViewPager;
import com.android.lily.viewpager.GalleryTransformer;
import com.android.lily.viewpager.ImagePagerAdapter;

/**
 * <pre>
 * @author
 * </pre>
 */
public class ViewPagerActivity extends Activity {

	private CarouselViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewpager);

		viewPager = (CarouselViewPager) findViewById(R.id.id_viewpager);

		ImagePagerAdapter adapter = new ImagePagerAdapter(this, viewPager);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(adapter);
		// 设置轮播时间
		viewPager.setTimeOut(5);
		// 设置3d效果
		viewPager.setPageTransformer(true, new GalleryTransformer());
		// 设置已经有数据了，可以进行轮播，一般轮播的图片等数据是来源于网络，网络数据来了后才设置此值，此处因为是demo，所以直接赋值了
		viewPager.setHasData(true);
		// 开启轮播
		viewPager.startTimer();
	}

}
