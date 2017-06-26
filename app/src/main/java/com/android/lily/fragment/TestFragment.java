package com.android.lily.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.lily.R;
import com.android.lily.activity.NetworkImageHolderView;
import com.android.lily.activity.StackHolderView;
import com.android.lily.activity.ZoomPageTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Author on 17/5/25.
 */

public class TestFragment extends Fragment{

    private static final String ARG_POSITION = "position";

    private Context mContext;
    private int position;
    private List<String> networkImages;
    private String[] images = {"https://img01.j1.com/upload/pic/homepage/1494827131061.jpg",
            "https://img01.j1.com/upload/pic/homepage/1494300131640.jpg",
            "https://img01.j1.com/upload/pic/homepage/1494386780755.jpg",
            "https://img01.j1.com/upload/pic/homepage/1491553107975.jpg",
            "https://img01.j1.com/upload/pic/homepage/1494468383462.jpg"
    };

    public static TestFragment newInstance(int position) {
        TestFragment f = new TestFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    private View fragmentView;
    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private ConvenientBanner cbStack;
    private ArrayList<Integer> localImages = new ArrayList<Integer>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageLoader();
    }

    //初始化网络图片缓存库
    private void initImageLoader(){
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.ic_default_adimage)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.fragment_test, container, false);

            init();
        }
        ViewGroup parent = (ViewGroup) fragmentView.getParent();
        if (parent != null) {
            parent.removeView(fragmentView);
        }
        return fragmentView;
    }

    /**
     * 初始化
     */
    private void init() {
        convenientBanner = (ConvenientBanner) fragmentView.findViewById(R.id.convenientBanner);
        cbStack = (ConvenientBanner) fragmentView.findViewById(R.id.cb_stack);

        //本地图片集合
        for (int position = 0; position < 7; position++) {
            localImages.add(getResId("ic_test_" + position, R.drawable.class));
        }

//        //本地图片例子
//        convenientBanner.setPages(
//                new CBViewHolderCreator<LocalImageHolderView>() {
//                    @Override
//                    public LocalImageHolderView createHolder() {
//                        return new LocalImageHolderView();
//                    }
//                }, localImages)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});


        //网络加载例子
        networkImages= Arrays.asList(images);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages).setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});;
        convenientBanner.startTurning(3000);

        cbStack.setPages(new CBViewHolderCreator<StackHolderView>() {
            @Override
            public StackHolderView createHolder() {
                return new StackHolderView();
            }
        }, networkImages);
        cbStack.getViewPager().setOffscreenPageLimit(3);
        cbStack.getViewPager().setPageTransformer(true, new ZoomPageTransformer(mContext));
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
