package com.lily.design.mvvm.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/***********
 * @Author rape flower
 * @Date 2017-04-18 10:46
 * @Describe 食物实体类
 */
public class FoodBean extends BaseObservable{

    private String description;
    private String img;
    private String keywords;
    private String summary;

    public FoodBean() {

    }

    public FoodBean(String description, String img, String keywords, String summary) {
        this.description = description;
        this.img = img;
        this.keywords = keywords;
        this.summary = summary;
    }

    @BindingAdapter("bind:img")
    public static void loadInternetImage(ImageView iv, String imgUrl) {
        Glide.with(iv.getContext()).load(imgUrl).into(iv);
    }

    public void onItemClick(View view) {
        Toast.makeText(view.getContext(), getDescription(), Toast.LENGTH_SHORT).show();
        setDescription("动态改变啦。。。");
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
//        notifyPropertyChanged(com.lily.design.BR.description);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
