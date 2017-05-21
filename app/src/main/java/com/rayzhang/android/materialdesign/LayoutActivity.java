package com.rayzhang.android.materialdesign;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

public class LayoutActivity extends AppCompatActivity {
    /**
     * 布局優化 : include、merge使用
     */
    private static final String TAG = LayoutActivity.class.getSimpleName();
    private ViewStub mViewStub;
    private ImageView mImgView;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (mImgView == null) mImgView = (ImageView) mViewStub.inflate();
                    Log.d(TAG, "mImgView already load. " + mImgView);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        // 當include，有設定android:id時，直接用include的Id，來找到相對應的根 View
        // 而其裡面子View，直接透過findViewById()即可取得
        Toolbar mToolBar = (Toolbar) findViewById(R.id.mToolBarLayout);
        mToolBar.setTitle("Include ToolBar");
        mToolBar.setTitleTextColor(Color.WHITE);
        TextView mTextView = (TextView) findViewById(R.id.mTextView);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        // 當include，沒有設定android:id時，直接用findViewById來找到相對應的View
        /*Toolbar mToolBar = (Toolbar) findViewById(R.id.mToolBar);
        mToolBar.setTitle("Include ToolBar");
        mToolBar.setTitleTextColor(Color.WHITE);
        TextView mTextView = (TextView) findViewById(R.id.mTextView);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);*/

        // 當include，有設定android:id時，直接用include的Id，來找到相對應的根 View
        // 此時的子View 可以透過這2個方式尋找
        /*View includeView = findViewById(R.id.mLinearLayout);
        // First
        TextView mTextHi = (TextView) includeView.findViewById(R.id.mTextHi);
        TextView mTextHello = (TextView) includeView.findViewById(R.id.mTextHello);
        includeView.setBackgroundColor(Color.LTGRAY);*/
        // Second
        TextView mTextHi = (TextView) findViewById(R.id.mTextHi);
        TextView mTextHello = (TextView) findViewById(R.id.mTextHello);
        mTextHi.setText("Hi");
        mTextHello.setText("Hello");

        // 判斷ViewStub是否已加載 方式_1 use setVisibility()
        // 該方式無法取得到View，單純用來顯示
        /*mViewStub = (ViewStub) findViewById(R.id.mViewStub);
        mViewStub.setVisibility(View.VISIBLE);
        if (mViewStub.getVisibility() == View.VISIBLE) {
            // 已經載入
            Log.d(TAG, "mImgView already load. " + mImgView);
        } else {
            // 尚未載入
            Log.d(TAG, "mImgView not load. " + mImgView);
        }*/

        // 判斷ViewStub是否已加載 方式_2 use Object is null
        mViewStub = (ViewStub) findViewById(R.id.mViewStub);
        /*if (mImgView == null) {
            // 直接取得ImageView，並載入
            mImgView = (ImageView) mViewStub.inflate();
            Log.d(TAG, "mImgView not load. " + mImgView);
        } else {
            Log.d(TAG, "mImgView already load.");
        }*/

        // 模擬延遲5秒 在載入
        Message message = Message.obtain();
        message.what = 100;
        mHandler.sendMessageDelayed(message, 5000);
    }
}