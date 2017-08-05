package com.rayzhang.android.materialdesign.intentservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rayzhang.android.materialdesign.R;

public class IntentServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = IntentServiceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);
        Log.d("DownloadIntentService", "IntentServiceActivity onCreate");

        Button mDownloadBut = (Button) findViewById(R.id.mDownloadBut);
        mDownloadBut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mDownloadBut:
                Intent downloadService = new Intent(this, DownloadIntentService.class);
                downloadService.putExtra("PATH", "http://attach.setn.com/newsimages/2016/04/13/496395.jpg");
                startService(downloadService);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DownloadIntentService", "IntentServiceActivity onDestroy");
    }
}
