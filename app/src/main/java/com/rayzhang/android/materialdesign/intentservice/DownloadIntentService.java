package com.rayzhang.android.materialdesign.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ray on 2017/8/4.
 */

public class DownloadIntentService extends IntentService {
    private static final String TAG = DownloadIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadIntentService(String name) {
        super(name);
    }

    public DownloadIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.d(TAG, "onStart");
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean isMainThread = Thread.currentThread() == getMainLooper().getThread();
        // 確認是否開啟一個執行緒執行任務
        Log.d(TAG, "isMainThread :" + isMainThread);
        // 接收傳進來的intent資料
        Log.d(TAG, "onHandleIntent path:" + intent.getStringExtra("PATH"));
        try {
            // 睡眠2秒(模擬耗時任務)
            Thread.sleep(2000);
            // 下載圖片
            downloadPicture(intent.getStringExtra("PATH"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void downloadPicture(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            InputStream is = null;
            ByteArrayOutputStream os = null;
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                is = httpURLConnection.getInputStream();
                os = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(os.toByteArray(), 0, os.toByteArray().length);
                Log.d(TAG, "savePicture bitmap:" + bitmap);
            }
            if (is != null) {
                os.flush();
                os.close();
                is.close();
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception:" + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
