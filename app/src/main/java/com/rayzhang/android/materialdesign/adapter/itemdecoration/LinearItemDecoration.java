package com.rayzhang.android.materialdesign.adapter.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Ray on 2017/5/29.
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * dividerSize : 分隔線的大小
     * dividerColor : 分隔線的顏色
     * mPaint : 繪製分隔線的paint
     */
    private static final String TAG = LinearItemDecoration.class.getSimpleName();
    private int dividerSize;
    private Paint mPaint;

    public LinearItemDecoration(int dividerSize, int dividerColor) {
        this.dividerSize = dividerSize;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(dividerColor);
    }

    /**
     * 律定每個Item偏移的距離
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // parent.getAdapter().getItemCount() : 取得Adapter裡全部Item的數量
        // parent.getChildCount() : 取的目前顯在螢幕上的Item數量
        Log.d(TAG, "getItemCount:" + parent.getAdapter().getItemCount() + " childCount:" + parent.getChildCount());

        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        // 判斷是否為LinearLayoutManager
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            // 在判斷LinearLayoutManager的方向，是否為VERTICAL
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                // 頂部 & 底部不繪製分隔線
                if (position != count - 1) {
                    outRect.set(0, 0, 0, dividerSize);
                }
            } else {
                if (position != count - 1) {
                    outRect.set(0, 0, dividerSize, 0);
                }
            }
        }
    }

    /**
     * 繪製分隔線
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int count = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.topMargin;
            int bottom = top + dividerSize;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int count = parent.getChildCount();
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + dividerSize;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    /**
     * 繪製分隔線，會繪製在View上方
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
