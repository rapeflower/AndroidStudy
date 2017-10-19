package com.lily.andfix.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.euler.andfix.patch.PatchManager;
import com.lily.andfix.download.AsyncHttpUtils;

import java.io.File;

/**
 * Created by Author on 17/8/16.
 */
public class AndFixApplication extends Application{

    private static final String PATCH_FILE_CACHE_FOLDER = "AndroidStudy/AndFix/";
    private static final String PATCH_FILE_NAME = "j1_hot_fix.apatch";
    private Context mContext;
    public static PatchManager patchManager;
    public static String filePath = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        patchManager  = new PatchManager(mContext);
        patchManager.init("1.0.0");
        patchManager.loadPatch();

        loadFromServer();
    }

    /**
     * 请求服务器获取热更新文件信息（patch file）
     */
    private void loadFromServer() {

        //当apk版本升级，需要把之前patch文件的删除，需要以下操作
        boolean isAPPVersionUpgrade = false;
        //删除所有已加载的patch文件
        patchManager.removeAllPatch();
        if (isAPPVersionUpgrade) {
            return;
        } else {
            downloadPatchFile("http://svn.j1.net/%E4%BA%A7%E5%93%81%E8%AE%BE%E8%AE%A1%E9%83%A8%E6%96%87%E6%A1%A3/APP%E5%95%86%E5%9F%8E/app-3.8/android-hot-fix/" + PATCH_FILE_NAME);
        }
    }

    /**
     * 下载 patch file
     *
     * @param url
     */
    public void downloadPatchFile(String url) {
        if (TextUtils.isEmpty(url)) return;

        String cachePath;
        // 配置图片缓存路径和缓存大小
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cachePath = Environment.getExternalStorageDirectory().getAbsoluteFile().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        File dir = new File(cachePath + File.separator + PATCH_FILE_CACHE_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir.getPath() + File.separator + url.substring(url.lastIndexOf("/") + 1, url.length()));
        if (file.exists()) {//文件存在就删除
            file.delete();
        }

        AsyncHttpUtils.downloadFile(url, file, new AsyncHttpUtils.IDownloadFileResult() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long current, long total, float progress) {

            }

            @Override
            public void onSuccess(int statusCode, File file) {
                android.util.Log.w("AsyncHttpUtils", "AsyncHttpUtils downloadFile success!!!");
                try {
                    android.util.Log.e("AsyncHttpUtils", "Patch file path = " + file.getPath());
                    filePath = file.getPath();
                    Toast.makeText(mContext, "filePath = " + filePath, Toast.LENGTH_SHORT).show();
//                    if (patchManager != null) {
//                        patchManager.addPatch(file.getPath());
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, String msg) {
                android.util.Log.w("AsyncHttpUtils", "AsyncHttpUtils downloadFile failure!!!");
            }
        });
    }
}
