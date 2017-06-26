package com.android.lily.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.android.lily.R;
import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class StackHolderView implements Holder<String> {

    private LayoutInflater mInflater;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        if (mInflater == null) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View view = mInflater.inflate(R.layout.item_brand_pavilion, null);

        return view;
    }

    @Override
    public void UpdateUI(Context context,int position, String data) {

////        ImageLoader.getInstance().displayImage(data,imageView);
//                Glide.with(context).load(data).into(imageView);
    }
}
