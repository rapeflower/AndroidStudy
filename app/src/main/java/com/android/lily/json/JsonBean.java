package com.android.lily.json;

import java.util.List;

/**
 * Created by lilei on 2017/11/30.
 */

public class JsonBean {

    private int error;
    private String status;
    private List<Results> results;
    public int getError() {
        return error;
    }
    public void setError(int error) {
        this.error = error;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<Results> getResults() {
        return results;
    }
    public void setResults(List<Results> results) {
        this.results = results;
    }

}
