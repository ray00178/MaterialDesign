package com.rayzhang.android.materialdesign.itemhelper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Ray on 2017/7/8.
 * <p>
 * Create a class and extends ItemTouchHelper.Callback
 */

public class RZItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemMoveSwipeListener itemMoveSwipeListener;

    public RZItemTouchHelperCallback(ItemMoveSwipeListener itemMoveSwipeListener) {
        this.itemMoveSwipeListener = itemMoveSwipeListener;
    }

    /**
     * 這個方法決定RecyclerView Item可以移動&滑動的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        // 先律定可「移動」的方向，這邊限制只能上、下移動
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        // 在律定可「滑動」的方向，這邊限制只能左、右滑動
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        // 如果是GridLayoutManager，那麼就不需要滑動所以可以這樣設置(左、上、右、下)
        // int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN;

        // 如果想讓「移動」或是「滑動」，其中1個無效用則參數設為0即可
        // int dragFlags = 0;
        // int swipeFlags = 0;

        // 再透過makeMovementFlags()方法去設置
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 移動完成後，要做甚麼事
     *
     * @param recyclerView
     * @param viewHolder   當前的手指正在移動的item
     * @param target       要被交換的item
     * @return 決定當次的移動是否要執行，true 執行 ; false 不執行
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return itemMoveSwipeListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    /**
     * 滑動完成後，要做甚麼事
     *
     * @param viewHolder
     * @param direction  當前滑動的方向
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemMoveSwipeListener.onItemSwipe(viewHolder.getAdapterPosition());
    }

    /**
     * 當item被選取到的時候，要做甚麼事
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        // 當item被選取到且是在移動的狀態下
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            // 就將透明度為原來的70%
            viewHolder.itemView.setAlpha(0.7f);
        }
    }

    /**
     * 當事件完成後，可以在這個方法，將View做復原的動作
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(1.0f);
    }

    // 如果是Swipe，要Override這個方法，因為想做的效果是隨著滑動距離
    // 來改變當前的透明度。上述的方法並無法取得滑動距離。
    // 所以需在這個方法，來實現
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // 取得控件的寬度
            float width = viewHolder.itemView.getWidth();
            // 依照滑動的距離，來計算當前透明度的值
            float alphaValue = 1 - Math.abs(dX) / width;

            viewHolder.itemView.setAlpha(alphaValue);
        }
    }
}
