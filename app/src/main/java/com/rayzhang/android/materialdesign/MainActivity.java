package com.rayzhang.android.materialdesign;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName() + "Ray";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = "";
        // 回覆數據
        if (savedInstanceState != null) {
            name = savedInstanceState.getString("Name");
        }
        logStr("onCreate：" + name);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.mToolBar);
        // 設定導航攔的圖示
        mToolBar.setNavigationIcon(R.drawable.ic_google_play_24dp);
        // 設定toolBar的logo
        mToolBar.setLogo(R.drawable.ic_arrow_back_white_24dp);
        // 設定標題文字
        mToolBar.setTitle("Hey");
        // 設定標題文字顏色
        mToolBar.setTitleTextColor(Color.YELLOW);
        // 設定子標題文字
        mToolBar.setSubtitle("Hi");
        // 設定子標題文字顏色
        mToolBar.setSubtitleTextColor(Color.GREEN);
        // 設定toolBar的actionMenu
        mToolBar.inflateMenu(R.menu.toolbar_menu);
        // 設定actionMenu的點擊事件
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favorite:
                        Toast.makeText(MainActivity.this, "favorite", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.delete:
                        Toast.makeText(MainActivity.this, "delete", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_item1:
                        Toast.makeText(MainActivity.this, "action_item1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_item2:
                        Toast.makeText(MainActivity.this, "action_item2", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        FloatingActionButton mFabBut = (FloatingActionButton) findViewById(R.id.mFabBut);
        mFabBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "I'm FAB", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, CollapsingActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
    }

    private void logStr(String str) {
        Log.d(TAG, str);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logStr("onSaveInstanceState");
        outState.putString("Name", "Ray");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // 螢幕翻轉時 會觸發
        logStr("onRestoreInstanceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 將Activity設定如下(在AndroidManifest.xml) 會觸發
        // android:configChanges="orientation|keyboardHidden|screenSize"
        logStr("onConfigurationChanged");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logStr("onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logStr("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logStr("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logStr("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logStr("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logStr("onDestroy");
    }
}
