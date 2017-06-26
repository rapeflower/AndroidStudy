package com.lily.design.mvvm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.lily.design.R;
import com.lily.design.mvvm.adapter.FoodAdapter;
import com.lily.design.mvvm.bean.FoodBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***********
 * @Author rape flower
 * @Date 2017-04-18 11:33
 * @Describe (MVVM)
 */
public class MVVMActivity extends Activity {

    private List<FoodBean> foods;
    private ListView lv;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            FoodAdapter<FoodBean> adapter = new FoodAdapter<>(MVVMActivity.this,
                    R.layout.item_food, foods, com.lily.design.BR.food);//com.lily.design.mvvm.bean.BR.food
            lv.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm);
        initView();
        initData();
    }

    /**
     * 初始化
     */
    private void initView() {
        lv = ((ListView) findViewById(R.id.lv_foods));
    }

    private void initData() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url("http://www.tngou.net/api/food/list?id=1").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    parseJson(response.body().string());
                }
            }
        });
    }

    private void parseJson(String jsonStr) {
        foods = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(jsonStr);
            JSONArray tngou = jo.getJSONArray("tngou");
            for (int i = 0; i < tngou.length(); i++) {
                JSONObject item = tngou.getJSONObject(i);
                String description = item.getString("description");
                String img = "http://tnfs.tngou.net/image"+item.getString("img");
                String keywords = "【关键词】 "+item.getString("keywords");
                String summary = item.getString("summary");
                foods.add(new FoodBean(description, img, keywords, summary));
            }
            mHandler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
