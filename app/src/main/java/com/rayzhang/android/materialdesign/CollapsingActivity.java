package com.rayzhang.android.materialdesign;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rayzhang.android.materialdesign.adapter.DemoAdapter;

import java.util.ArrayList;
import java.util.List;

public class CollapsingActivity extends AppCompatActivity {
    /**
     * CollapsingToolbarLayout use
     */
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsLayout;
    private Toolbar mToolBar;
    private RecyclerView mRecyView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.mAppBarLayout);
        mCollapsLayout = (CollapsingToolbarLayout) findViewById(R.id.mCollaspLayout);
        mToolBar = (Toolbar) findViewById(R.id.mToolBar);
        setSupportActionBar(mToolBar);

        /*mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            // https://codedump.io/share/x8JwlFS66glv/1/show-collapsingtoolbarlayout-title-only-when-collapsed
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) scrollRange = appBarLayout.getTotalScrollRange();
                if (scrollRange + verticalOffset == 0) {
                    mToolBar.setTitle("CollapsingToolbarLayout");
                    isShow = true;
                } else if (isShow) {
                    mToolBar.setTitle("");
                    isShow = false;
                }
            }
        });*/
        mCollapsLayout.setTitle("Collapsing");
        mCollapsLayout.setCollapsedTitleTypeface(Typeface.SERIF);
        mCollapsLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsLayout.setExpandedTitleTypeface(Typeface.SERIF);
        mCollapsLayout.setExpandedTitleColor(Color.TRANSPARENT);

        mRecyView = (RecyclerView) findViewById(R.id.mRecyView);
        mRecyView.setItemAnimator(new DefaultItemAnimator());
        mRecyView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyView.setLayoutManager(manager);
        /**
         * 設置RecyclerView的Adapter
         */
        List<String> list = new ArrayList<>();
        for (int i = 1, j = 100; i <= j; i++) {
            list.add("item:" + i);
        }
        DemoAdapter adapter = new DemoAdapter(list);
        mRecyView.setAdapter(adapter);
        adapter.setOnItemClickListener(new DemoAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view) {
                Intent i = new Intent(CollapsingActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}