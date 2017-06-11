package com.rayzhang.android.materialdesign.adapter.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ray on 2017/6/11.
 */

public class LinearSectionDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = LinearItemDecoration.class.getSimpleName();
    // 分類群組的大小
    private int sectionSize = 30;
    // 分隔線的大小
    private int dividerSize = 1;
    // 是否要有分類群組
    private boolean hasSection = false;
    private Paint mPaint, mPaintText;
    private LinearSectionCallback callback;

    public LinearSectionDecoration(int dividerSize, int dividerColor) {
        this.dividerSize = dividerSize;
        initPaint(dividerColor);
    }

    public LinearSectionDecoration(int sectionSize, int dividerSize, int dividerColor, LinearSectionCallback callback) {
        this.sectionSize = sectionSize;
        this.dividerSize = dividerSize;
        this.callback = callback;
        hasSection = true;
        // 繪製標題名稱的paint
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.argb(255, 255, 255, 255));
        mPaintText.setTextSize(16f);
        mPaintText.setDither(true);
        initPaint(dividerColor);
    }

    private void initPaint(int color) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);
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

        int position = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.VERTICAL) {
                if (hasSection) {
                    // 如果要繪製分類群組
                    setVerticalItemOffSetBySection(outRect, position);
                } else {
                    setVerticalItemOffSet(outRect, position);
                }
            }
        }
    }

    private void setVerticalItemOffSetBySection(Rect outRect, int position) {
        if (isFirstInGroup(position)) {
            outRect.set(0, sectionSize, 0, 0);
        } else {
            outRect.set(0, dividerSize, 0, 0);
        }
    }

    private void setVerticalItemOffSet(Rect outRect, int position) {
        if (position != 0) outRect.set(0, dividerSize, 0, 0);
    }

    private boolean isFirstInGroup(int position) {
        // 判斷是否為相同群組的第1個
        return position == 0 || !callback.getItemStr(position).equals(callback.getItemStr(position - 1));
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

        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.VERTICAL) {
                if (hasSection) {
                    drawVerticalBySection(c, parent);
                } else {
                    drawVertical(c, parent);
                }
            }
        }
    }

    private void drawVerticalBySection(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = parent.getChildAdapterPosition(child);

            int top = child.getTop() + params.topMargin;
            int bottom = top + dividerSize;
            if (isFirstInGroup(position)) {
                // 要減去sectionSize的大小
                top = child.getTop() - sectionSize + params.topMargin;
                bottom = top + sectionSize;
            }
            c.drawRect(left, top, right, bottom, mPaint);
            if (hasSection && isFirstInGroup(position)) {
                // 繪製標題名稱
                c.drawText(callback.getItemStr(position), 20, bottom - sectionSize / 3, mPaintText);
            }
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = parent.getChildAdapterPosition(child);

            if (position != 0) {
                int top = child.getTop() + params.topMargin;
                int bottom = top + dividerSize;
                c.drawRect(left, top, right, bottom, mPaint);
            }
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

        // 如果沒有要繪製分類群組，就不繼續執行
        if (!hasSection) return;

        int childCount = parent.getChildCount();
        int itemCount = parent.getAdapter().getItemCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        // 上個Item分組的標誌
        String preGroupId = "";
        // 現在Item分組的標誌
        String nowGroupId = "-1";

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = parent.getChildAdapterPosition(child);

            preGroupId = nowGroupId;
            nowGroupId = callback.getItemStr(position);
            // 如果目前的Item分組的標誌等於 "-1" or 跟前1個Item一樣，就跳過
            if (nowGroupId.equals("-1") || nowGroupId.equals(preGroupId)) continue;

            int otherSize = params.topMargin + parent.getPaddingTop();
            int childBottom = child.getBottom();

            int top = Math.max(otherSize + sectionSize, otherSize + child.getTop());
            //Log.d(TAG, String.format(Locale.TAIWAN, "position:%d sectionSize:%d getTop():%d", position, sectionSize, child.getTop()));
            if (position + 1 < itemCount) {
                if (isLastInGroup(position) && childBottom < top) {
                    // 如果是當前群組，最後1個Item並且 childBottom < top
                    // 就把繪製製的top等於目前的childBottom，這樣才會達到吸附的效果
                    top = childBottom;
                    //Log.d(TAG, String.format(Locale.TAIWAN, "childBottom:%d", childBottom));
                }
            }
            c.drawRect(left, top - sectionSize, right, top, mPaint);
            // 繪製標題名稱
            c.drawText(callback.getItemStr(position), 20, top - sectionSize / 3, mPaintText);
        }
    }

    private boolean isLastInGroup(int position) {
        // 判斷是否為當前群組的最後1個
        return !callback.getItemStr(position).equals(callback.getItemStr(position + 1));
    }

    /**
     * 給外部調用的Callback，取得每個Item的字
     */
    public interface LinearSectionCallback {
        // 取得當前位置Item的Text
        String getItemStr(int poisition);
    }
}
