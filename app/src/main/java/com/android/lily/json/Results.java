package com.android.lily.json;

import java.util.List;

/**
 * Created by lilei on 2017/11/30.
 */

public class Results {

    private String currentCity;
    private List<Index> index;
    public String getCurrentCity() {
        return currentCity;
    }
    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }
    public List<Index> getIndex() {
        return index;
    }
    public void setIndex(List<Index> index) {
        this.index = index;
    }

}
