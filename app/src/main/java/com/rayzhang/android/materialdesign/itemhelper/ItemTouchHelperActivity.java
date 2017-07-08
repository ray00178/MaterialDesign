package com.rayzhang.android.materialdesign.itemhelper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.rayzhang.android.materialdesign.R;
import com.rayzhang.android.materialdesign.adapter.itemdecoration.LinearSectionDecoration;

import java.util.ArrayList;
import java.util.List;

public class ItemTouchHelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_touch_helper);

        RecyclerView mRecyView = (RecyclerView) findViewById(R.id.mRecyView);
        mRecyView.setHasFixedSize(true);
        mRecyView.setItemAnimator(new DefaultItemAnimator());
        mRecyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyView.addItemDecoration(new LinearSectionDecoration(1, Color.LTGRAY));

        // 資料源
        final List<String> list = new ArrayList<>();
        for (int i = 1, j = 100; i <= j; i++) {
            list.add("" + i);
        }
        ItemTouchHelperAdapter adapter = new ItemTouchHelperAdapter(list);
        mRecyView.setAdapter(adapter);

        // 設置ItemTouchHelper並與RecycleView做關聯
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RZItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyView);
    }
}
