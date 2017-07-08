package com.rayzhang.android.materialdesign.itemhelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rayzhang.android.materialdesign.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ray on 2017/7/8.
 */

public class ItemTouchHelperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemMoveSwipeListener {
    private static final int NORMAL_ITEM = 9999;
    private List<String> list;

    public ItemTouchHelperAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new ItemTouchHelperAdapter.DemoNView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == NORMAL_ITEM) {
            ((ItemTouchHelperAdapter.DemoNView) holder).mTextView.setText(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL_ITEM;
    }

    private class DemoNView extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private DemoNView(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.mTextView);
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // Collections.swap() 該方法是用來交換位置
        if (fromPosition >= 49) {
            // 當手指正在移動的item，它的位置 >= 50 就執行移動的動作
            // 但是移動的範圍一樣不能 < 50，否則這次移動不會執行
            Collections.swap(list, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }
        // 如果不是，就不執行移動
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
}