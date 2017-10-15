package com.rayzhang.android.materialdesign;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 執行緒池的分類
 */
public class ExecutorActivity extends AppCompatActivity {
    private static final String TAG = ExecutorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor);

        findViewById(R.id.mFixBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixedThread();
            }
        });
        findViewById(R.id.mCacheBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cacheThread();
            }
        });
        findViewById(R.id.mScheduleBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduledThread();
            }
        });
        findViewById(R.id.mSingleBut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleThread();
            }
        });
    }

    private void fixedThread() {
        // 每次執行最多4個，但不保證順序性
        ExecutorService fixed = Executors.newFixedThreadPool(4);
        Log.d(TAG, "CurrentTime:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(new Date()));
        for (int i = 1; i <= 8; i++) {
            Task task = new Task("Fixed " + i);
            fixed.execute(task);
        }
    }

    private void cacheThread() {
        // 無次數限制，但不保證順序性
        ExecutorService cache = Executors.newCachedThreadPool();
        Log.d(TAG, "CurrentTime:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(new Date()));
        for (int i = 1; i <= 10; i++) {
            Task task = new Task("Cache " + i);
            cache.execute(task);
        }
    }

    private void scheduledThread() {
        // 定時性的任務
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(4);
        Log.d(TAG, "CurrentTime:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(new Date()));
        Task task = new Task("Scheduled");
        // 2s後執行
        scheduled.schedule(task, 2, TimeUnit.SECONDS);
        // 延遲1s後，每2s執行1次
        scheduled.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
    }

    private void singleThread() {
        // 每次只執行1個任務，有順序性
        ExecutorService single = Executors.newSingleThreadExecutor();
        Log.d(TAG, "CurrentTime:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN).format(new Date()));
        for (int i = 1; i < 5; i++) {
            Task task = new Task("SingleThread " + i);
            single.execute(task);
        }
    }

    private static class Task implements Runnable {
        private String taskName;

        private Task(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            SystemClock.sleep(1000);
            Log.d(TAG, "TaskName:" + taskName);
        }
    }
}
