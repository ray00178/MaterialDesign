package com.rayzhang.android.materialdesign.adapter.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Locale;

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
    private int dividerSize, topSize = 30;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private List<String> list;

    public LinearItemDecoration(int dividerSize, int dividerColor, List<String> list) {
        this.dividerSize = dividerSize;
        this.list = list;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(dividerColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setDither(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(16f);
    }

    /**
     * 律定每個Item偏移的距離
     *
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
        //Log.d(TAG, "getItemCount:" + parent.getAdapter().getItemCount() + " childCount:" + parent.getChildCount());

        //Log.d(TAG, "LayoutPositon:" + parent.getChildLayoutPosition(view) + " AdapterPosition:" + parent.getChildAdapterPosition(view));

        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        // 判斷是否為LinearLayoutManager
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            // 在判斷LinearLayoutManager的方向，是否為VERTICAL
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                // 頂部 & 底部不繪製分隔線
//                if (position != count - 1) {
//                    outRect.set(0, 0, 0, dividerSize);
//                }
                setVerticalOffSet(outRect, position);
            } else {
                if (position != count - 1) {
                    outRect.set(0, 0, dividerSize, 0);
                }
            }
        }
    }

    private void setVerticalOffSet(Rect outRect, int position) {
        if (isFirstInGroup(position)) {
            outRect.set(0, topSize, 0, 0);
            //Log.d(TAG, "position:" + position + " true");
        } else {
            outRect.set(0, dividerSize, 0, 0);
            //Log.d(TAG, "position:" + position + " false");
        }
    }

    private boolean isFirstInGroup(int position) {
        // 檢查第1個名稱是否相同
        if (position == 0 || !list.get(position).substring(0, 1).equals(list.get(position - 1).substring(0, 1))) {
            return true;
        }
        return false;
    }

    /**
     * 繪製分隔線
     *
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
            int position = parent.getChildAdapterPosition(child);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getTop() + params.topMargin;
            int bottom = top + dividerSize;

            if (isFirstInGroup(position)) {
                top = child.getTop() - topSize;
                bottom = top + topSize;
                c.drawRect(left, top, right, bottom, mPaint);
                c.drawText(list.get(position), 20, (float) top + (float) (topSize / 1.5), mTextPaint);
            } else {
                c.drawRect(left, top, right, bottom, mPaint);
            }
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
     *
     * @param c
     * @param parent
     * @param state
     */
    // TODO : 標題分類
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = parent.getAdapter().getItemCount();
        int count = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        String preGroupId = "";
        String groupId = "-1";

        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = parent.getChildAdapterPosition(child);

            preGroupId = groupId;
            groupId = list.get(position).substring(0, 1);
            if (groupId.equals("-1") || groupId.equals(preGroupId)) continue;

            int otherSize = params.topMargin + parent.getPaddingTop();
            int childBottom = child.getBottom();
            int top = Math.max(otherSize + topSize, otherSize + child.getTop());
            if (position + 1 < itemCount) {
                if (isLastInGroup(position) && childBottom < top) {
                    top = childBottom;
                }
            }
            int bottom = top + topSize;
            Log.d(TAG, String.format(Locale.TAIWAN, "position:%d top:%d sectionStr:%s", position, top, list.get(position).substring(0, 1)));
            c.drawRect(left, top - topSize, right, top, mPaint);
            // (float) top + (float) (topSize / 1.5)
            c.drawText(list.get(position).substring(0, 1), 20, top , mTextPaint);
        }
    }

    private boolean isLastInGroup(int position) {
        if (list.get(position).substring(0, 1).equals(list.get(position + 1).substring(0, 1))) {
            return false;
        }
        return true;
    }
}
