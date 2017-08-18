package com.lily.andfix.download;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/***********
 *
 * @Author rape flower
 * @Date 2017-08-16 16:48
 * @Describe 上传文件（图片）or下载文件的工具类
 *
 */
public class AsyncHttpUtils {

    private static final String TAG = "AsyncHttpUtils";
    public static final String KEY = "upload_file";
    private static AsyncHttpClient client = null;

    private AsyncHttpUtils() {

    }

    /**
     * 获取AsyncHttpClient实例
     *
     * @return
     */
    private static AsyncHttpClient getAsyncHttpClient() {
        if (client == null) {
            synchronized (AsyncHttpUtils.class) {
                if (client == null) {
                    client = new AsyncHttpClient();
                    //支持https
                    //SSlSocketFactory which trusts all certificates
                    client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
                }
            }
        }
        return client;
    }

    /***
     * 文件上传的事件监听
     */
    public interface IUploadFileResult {
        int SUCCESS = 1;//-> 200
        int FAIL = 2;//-> 401, 403, 404

        void onStart();
        void onResult(int statusCode, String result);
        void onProgress(long current, long total, float progress);
    }

    /***
     * 文件下载的事件监听
     */
    public interface IDownloadFileResult {
        int SUCCESS = 1;//-> 200
        int FAIL = 2;//-> 401, 403, 404

        void onStart();
        void onProgress(long current, long total, float progress);
        void onSuccess(int statusCode, File file);
        void onFailure(int statusCode, String msg);
    }

    /**
     * 构建请求参数
     *
     * @param source the source key/value string map to add.
     * @return
     */
    public static RequestParams buildParams(Map<String, Object> source) {
        RequestParams params = new RequestParams();
        try {
            if (source != null) {
                for (Map.Entry<String, Object> entry : source.entrySet()) {
                    Object object = entry.getValue();
                    if (object instanceof File) {
                        params.put(entry.getKey(), (File) object);
                    } else if (object instanceof File[]) {
                        params.put(entry.getKey(), (File[]) object);
                    } else if (object instanceof String) {
                        params.put(entry.getKey(), (String) object);
                    } else if (object instanceof Integer) {
                        params.put(entry.getKey(), (int) object);
                    } else if (object instanceof Long) {
                        params.put(entry.getKey(), (long) object);
                    } else {
                        params.put(entry.getKey(), object);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return params;
    }

    /**
     * 由AsyncHttpClient框架上传
     * <p>
     *     单文件上传
     *     <b>上传到公司soa服务器</b>
     * </p>
     *
     * @param path 上传文件的路径
     * @param callback 上传文件回调
     * @return void
     */
    public static void upLoadSingleFile(String path, IUploadFileResult callback)  {
        upLoadSingleFile(path, "null", callback);
    }

    /**
     * 由AsyncHttpClient框架上传
     * <p>
     *     单文件上传
     * </p>
     *
     * @param path 上传文件的路径
     * @param requestUrl 请求地址
     * @param callback 上传文件回调
     * @return void
     */
    public static void upLoadSingleFile(String path, String requestUrl, IUploadFileResult callback)  {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(KEY, new File(path));

            uploadFile(requestUrl, buildParams(map), callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 由AsyncHttpClient框架上传
     * <p>
     *     多文件上传
     *     <b>上传到公司soa服务器</b>
     * </p>
     *
     * @param paths 上传文件路径集合
     * @param callback 上传文件回调
     * @return void
     */
    public static void upLoadMultiFile(List<String> paths, IUploadFileResult callback)  {
        upLoadMultiFile(paths, "null", callback);
    }

    /**
     * 由AsyncHttpClient框架上传
     * <p>
     *     多文件上传
     * </p>
     *
     * @param paths 上传文件路径集合
     * @param requestUrl 请求地址
     * @param callback 上传文件回调
     * @return void
     */
    public static void upLoadMultiFile(List<String> paths, String requestUrl, IUploadFileResult callback)  {
        if (paths == null || paths.size() == 0) {
            return;
        }

        ArrayList<File> fileArrayList = new ArrayList<>();
        for (String pt : paths) {
            fileArrayList.add(new File(pt));
        }
        File[] files = new File[fileArrayList.size()];
        fileArrayList.toArray(files);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(KEY, files);

            uploadFile(requestUrl, buildParams(map), callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param requestUrl 请求地址
     * @param params 请求参数
     * @param callback 上传文件的回调
     */
    public static void uploadFile(String requestUrl, RequestParams params, final IUploadFileResult callback) {
        try {
            getAsyncHttpClient().post(requestUrl, params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    if (callback != null) {
                        callback.onStart();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // called when response HTTP status is "200 OK"
                    Log.d(TAG, " uploadFile onSuccess statusCode = " + statusCode);
                    if (callback != null) {
                        callback.onResult(IUploadFileResult.SUCCESS, convertByteToString(responseBody));
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.d(TAG, " uploadFile onFailure statusCode = " + statusCode);
                    if (callback != null) {
                        callback.onResult(IUploadFileResult.FAIL, convertByteToString(responseBody));
                    }
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    if (callback != null) {
                        callback.onProgress(bytesWritten, totalSize, getProgress(bytesWritten, totalSize));
                    }
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算单精度浮点类型的进度值
     *
     * @param currentSize
     * @param totalSize
     * @return
     */
    private static float getProgress(long currentSize, long totalSize) {
        BigDecimal bigDecimal = new BigDecimal((currentSize * 1.0 / totalSize) * 100);
        float progress = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return progress;
    }

    /**
     * 将字节数据转换成字符串
     *
     * @param bt
     * @return
     */
    private static String convertByteToString(byte[] bt) {
        String res = null;
        try {
            if (bt != null) {
                res = new String(bt, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 由AsyncHttpClient框架下载文件
     *
     * @param url 下载地址
     * @param file 下载到本地的文件
     * @param callback 下载文件回调
     */
    public static void downloadFile(String url, File file, final IDownloadFileResult callback) {
        /**
         * String apk = context.getExternalCacheDir().getAbsolutePath() + "/test.apk";
         * File apkFile = new File(apk);
         * new FileAsyncHttpResponseHandler(apkFile)
         */
        getAsyncHttpClient().get(url, new FileAsyncHttpResponseHandler(file) {


            @Override
            public void onStart() {
                super.onStart();
                if (callback != null) {
                    callback.onStart();
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                if (callback != null) {
                    callback.onProgress(bytesWritten, totalSize, getProgress(bytesWritten, totalSize));
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                if (callback != null) {
                    callback.onSuccess(IDownloadFileResult.SUCCESS, file);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                if (callback != null) {
                    callback.onFailure(IDownloadFileResult.FAIL, throwable.getMessage());
                }
            }

        });
    }
}
