package com.android.lily.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by lilei on 2017/11/30.
 */

public class JsonTest {

    public static void main(String[] args) {

        String s = "{\"error\":0,\"status\":\"success\",\"results\":[{\"currentCity\":\"青岛\",\"index\":[{\"title\":\"穿衣\",\"zs\":\"较冷\",\"tipt\":\"穿衣指数\",\"des\":\"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。\"},{\"title\":\"紫外线强度\",\"zs\":\"最弱\",\"tipt\":\"紫外线强度指数\",\"des\":\"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。\"}]}]}";
        Gson gson = new Gson();
        //把JSON数据转化为对象
        JsonBean jsonBean = gson.fromJson(s, JsonBean.class);
        Results results = jsonBean.getResults().get(0);
        System.out.println(" size = " + results.getIndex().size());

        Index index = results.getIndex().get(1);
        System.out.println(" title = " + index.getTitle());


        String jsonArray = "[{\"time\":\"2017-03-08 13:39:38\",\"ftime\":\"2017-03-08 13:39:38\",\"context\":\"上海市松江区泗泾公司(点击查询电话)杨** 派件中 派件员电话18017901716\",\"location\":\"null\"},{\"time\":\"2017-03-08 13:39:38\",\"ftime\":\"2017-03-08 13:39:38\",\"context\":\"上海市松江区泗泾公司(点击查询电话)杨** 派件中 派件员电话18017901716\",\"location\":\"null\"},{\"time\":\"2017-03-08 13:39:38\",\"ftime\":\"2017-03-08 13:39:38\",\"context\":\"上海市松江区泗泾公司(点击查询电话)杨** 派件中 派件员电话18017901716\",\"location\":\"null\"}]";
        List<Logistics> list = gson.fromJson(jsonArray, new TypeToken<List<Logistics>>(){}.getType());
        System.out.println(" list size = " + list.size());
        System.out.println(" Logistics = " + list.get(2).getContext());
    }
}
