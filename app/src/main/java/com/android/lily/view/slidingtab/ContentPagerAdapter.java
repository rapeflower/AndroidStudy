package com.android.lily.view.slidingtab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContentPagerAdapter extends FragmentPagerAdapter{

    private List<SlidingTabModel> contents = new ArrayList<SlidingTabModel>();

    public ContentPagerAdapter(FragmentManager fm, List<SlidingTabModel> data) {
        super(fm);
        this.contents = data;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SlidingTabModel tabModel = contents.get(position);
        return tabModel.title;
    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public Fragment getItem(int position) {
        SlidingTabModel tabModel = contents.get(position);
        return tabModel.fragment;
    }
}
