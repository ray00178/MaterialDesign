package com.rayzhang.android.materialdesign;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}
