package com.android.lily.view.slidingtab;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SlidingTabModel {
    /**
     * 标题
     */
    public String title;
    /***
     * 图片资源Id
     */
    public String image;
    /***
     * Fragment
     */
    public Fragment fragment;
    /***
     * Fragment对应的bundle
     */
    public Bundle bundle;
}
