package com.rayzhang.android.materialdesign.itemhelper;

/**
 * Created by Ray on 2017/7/8.
 */

public interface ItemMoveSwipeListener {
    /**
     * 設置1個監聽的interface
     *
     * onItemMove : 當item移動完的時候
     * onItemSwipe : 當item滑動完的時候
     */
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemSwipe(int position);
}
