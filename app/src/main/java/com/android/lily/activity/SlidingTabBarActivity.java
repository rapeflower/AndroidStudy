package com.android.lily.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.lily.R;
import com.android.lily.fragment.SuperAwesomeCardFragment;
import com.android.lily.fragment.TestFragment;
import com.android.lily.view.slidingtab.SlidingTabBar;
import com.android.lily.view.slidingtab.SlidingTabModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Author on 17/5/11.
 */

public class SlidingTabBarActivity extends FragmentActivity {

    private SlidingTabBar slidingTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_tab_bar);

        slidingTabBar = (SlidingTabBar) findViewById(R.id.stb_tab);
        slidingTabBar.setContent(getSupportFragmentManager(), getData());
    }

    private List<SlidingTabModel> getData() {
        //final String[] TITLES = { "推荐", "母婴专区", "性福生活", "纤体塑型", "养生保健"};
        List<SlidingTabModel> tabModels = new ArrayList<SlidingTabModel>();

        SlidingTabModel slidingTabModel0 = new SlidingTabModel();
        slidingTabModel0.title = "推荐";
        slidingTabModel0.fragment = TestFragment.newInstance(0);
        tabModels.add(slidingTabModel0);

        SlidingTabModel slidingTabModel1 = new SlidingTabModel();
        slidingTabModel1.title = "母婴专区";
        slidingTabModel1.fragment = SuperAwesomeCardFragment.newInstance(1);
        tabModels.add(slidingTabModel1);

        SlidingTabModel slidingTabModel2 = new SlidingTabModel();
        slidingTabModel2.title = "性福生活";
        slidingTabModel2.fragment = SuperAwesomeCardFragment.newInstance(2);
        tabModels.add(slidingTabModel2);

        SlidingTabModel slidingTabModel3 = new SlidingTabModel();
        slidingTabModel3.title = "纤体塑型";
        slidingTabModel3.fragment = SuperAwesomeCardFragment.newInstance(3);
        tabModels.add(slidingTabModel3);

        SlidingTabModel slidingTabModel4 = new SlidingTabModel();
        slidingTabModel4.title = "养生保健";
        slidingTabModel4.fragment = SuperAwesomeCardFragment.newInstance(4);
        tabModels.add(slidingTabModel4);

        return tabModels;
    }
}
