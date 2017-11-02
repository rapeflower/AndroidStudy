package com.android.lily.view.boutiquemuseum;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rape flower
 * @descripe 滑动标签条目适配器
 * <p>
 *     FragmentPagerAdapter
 *     FragmentStatePagerAdapter
 *     <li>
 *         FragmentPagerAdapter与FragmentStatePagerAdapter区别：
 *         http://www.cnblogs.com/lianghui66/p/3607091.html
 *     </li>
 *     <li>
 *         getItemPosition(Object object)
 *         每次调用 notifyDataSetChanged() 方法时，都会激活 getItemPosition方法
 *         POSITION_NONE表示该 Item 会被 destroyItem方法remove 掉，然后重新加载
 *         POSITION_UNCHANGED表示不会重新加载，默认是 POSITION_UNCHANGED
 *     </li>
 * </p>
 */
public class ContentPagerAdapter extends FragmentStatePagerAdapter {

    private List<BoutiqueMuseumItem> contents = new ArrayList<BoutiqueMuseumItem>();

    public ContentPagerAdapter(FragmentManager fm, List<BoutiqueMuseumItem> data) {
        super(fm);
        this.contents = data;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (contents == null) {
            return "";
        }
        BoutiqueMuseumItem tabModel = contents.get(position);
        String str = tabModel.name + "¥" + tabModel.image;
        return str;
    }

    @Override
    public int getCount() {
        return contents == null ? 0 : contents.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (contents == null) {
            return new Fragment();
        }
        BoutiqueMuseumItem tabModel = contents.get(position);
        return tabModel == null ? new Fragment() : tabModel.fragment;
    }
}
