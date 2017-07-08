package com.rayzhang.android.materialdesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rayzhang.android.materialdesign.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2017/3/12.
 */

public class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int NORMAL_ITEM = 9999;
    private ArrayList<String> list;

    public DemoAdapter(List<String> list) {
        this.list = new ArrayList<>();
        this.list.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item, parent, false);
        return new DemoNView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == NORMAL_ITEM) {
            ((DemoNView) holder).mTextView.setText(list.get(position));
            if (onItemClickListener != null) {
                ((DemoNView) holder).mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v);
                    }
                });
            }
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

    public class DemoNView extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public DemoNView(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.mTextView);
        }
    }

    public interface onItemClickListener {
        void onItemClick(View view);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
