package com.lily.yuyin.recognizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author rape flower
 * @Date 2019-03-26 10:54
 * @Describe 百度语音识别结果mode
 */
public class RecognizerResult {

    private static final int ERROR_NONE = 0;

    private String originalJson;
    private String[] resultsRecognition;
    private String originalResult;
    private String sn; // 日志id，请求有问题请提问带上sn
    private String desc;
    private String resultType;
    private int error = -1;
    private int subError = -1;

    public static RecognizerResult parseJson(String jsonStr) {
        RecognizerResult result = new RecognizerResult();
        result.setOriginalJson(jsonStr);
        try {
            JSONObject json = new JSONObject(jsonStr);
            int error = json.optInt("error");
            result.setError(error);
            result.setDesc(json.optString("desc"));
            result.setResultType(json.optString("result_type"));
            result.setSubError(json.optInt("sub_error"));
            if (error == ERROR_NONE) {
                result.setOriginalResult(json.optString("origin_result"));
                JSONArray array = json.optJSONArray("results_recognition");
                if (array != null) {
                    int size = array.length();
                    String[] recognizers = new String[size];
                    for (int i = 0; i < size; i++) {
                        recognizers[i] = array.optString(i);
                    }
                    result.setResultsRecognition(recognizers);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean hasError() {
        return error != ERROR_NONE;
    }

    public boolean isFinalResult() {
        return "final_result".equals(resultType);
    }

    public boolean isPartialResult() {
        return "partial_result".equals(resultType);
    }

    public boolean isNluResult() {
        return "nlu_result".equals(resultType);
    }

    public String getOriginalJson() {
        return originalJson;
    }

    public void setOriginalJson(String originalJson) {
        this.originalJson = originalJson;
    }

    public String[] getResultsRecognition() {
        return resultsRecognition;
    }

    public void setResultsRecognition(String[] resultsRecognition) {
        this.resultsRecognition = resultsRecognition;
    }

    public String getOriginalResult() {
        return originalResult;
    }

    public void setOriginalResult(String originalResult) {
        this.originalResult = originalResult;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getSubError() {
        return subError;
    }

    public void setSubError(int subError) {
        this.subError = subError;
    }
}
