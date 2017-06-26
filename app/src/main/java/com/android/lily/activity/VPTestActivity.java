package com.android.lily.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.android.lily.R;
import com.android.lily.viewpager.CarouselViewPager;
import com.android.lily.viewpager.GalleryTransformer;
import com.android.lily.viewpager.ImagePagerAdapter;
import com.android.lily.viewpager.VPTestAdapter;

/**
 * <pre>
 * @author
 * </pre>
 */
public class VPTestActivity extends Activity {

	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vp_test);
		viewPager = (ViewPager) findViewById(R.id.id_viewpager);

		VPTestAdapter adapter = new VPTestAdapter(this, viewPager);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setPageMargin(-50);
		viewPager.setAdapter(adapter);
		// 设置3d效果
		viewPager.setPageTransformer(true, new GalleryTransformer());
		viewPager.setCurrentItem(1, true);
	}

}
