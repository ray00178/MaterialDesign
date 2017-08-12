package com.rayzhang.android.materialdesign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandlerThreadActivity extends AppCompatActivity {
    private static final String TAG = HandlerThreadActivity.class.getSimpleName();

    private TextView mTextView;
    private ImageView mImgView;
    // HandlerThread & Hanlder
    private HandlerThread downloadThread;
    private Handler downloadHandler;
    // 是否要定時更新
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);

        mTextView = (TextView) findViewById(R.id.mTextView);
        mImgView = (ImageView) findViewById(R.id.mImgView);

        mTextView.setText("目前亂數：");

        downloadThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        // 別忘了要呼叫start()方法，否則在下面downloadThread.getLooper()取到的Looper會是null
        downloadThread.start();
        downloadHandler = new Handler(downloadThread.getLooper());
        // 分別打印Main Thread & downloadThread資訊
        Log.d(TAG, "Main Looper:" + getMainLooper() + " thread:" + getMainLooper().getThread() +
                " progress:" + Process.getThreadPriority((int) getMainLooper().getThread().getId()));
        Log.d(TAG, "Thread Looper:" + downloadThread.getLooper() + " thread:" + downloadThread +
                " progress:" + Process.getThreadPriority(downloadThread.getThreadId()));
    }

    // 用來產生亂數
    private Runnable mRunnableRandom = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int random = (int) (Math.random() * 8999) + 1000;
                    mTextView.setText("目前亂數：" + random);
                }
            });
            if (isUpdate) {
                downloadHandler.postDelayed(mRunnableRandom, 1000);
            }
        }
    };

    // 用來下載圖片
    private Runnable mRunnableImg = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL("http://www.fhm.com.tw/fhm_upload/images/Tzuyu_10.jpg");
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
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(os.toByteArray(), 0, os.toByteArray().length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImgView.setImageBitmap(bitmap);
                        }
                    });
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
    };

    @Override
    protected void onResume() {
        super.onResume();
        isUpdate = true;
        // 發送Runnable到downloadThread Looper對列裡
        downloadHandler.postDelayed(mRunnableRandom, 1000);
        downloadHandler.postDelayed(mRunnableImg, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isUpdate = false;
        // 移除Runnable
        downloadHandler.removeCallbacks(mRunnableRandom);
        downloadHandler.removeCallbacks(mRunnableImg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 將downloadThread停止所有的任務
        boolean isQuit;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // 安全的停止所有的任務
            isQuit = downloadThread.quitSafely();
        } else {
            // 強制中斷所有的任務
            isQuit = downloadThread.quit();
        }
        Log.d(TAG, "isQuit:" + isQuit);
    }
}
