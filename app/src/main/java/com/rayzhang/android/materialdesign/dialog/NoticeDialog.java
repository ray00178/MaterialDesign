package com.rayzhang.android.materialdesign.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.rayzhang.android.materialdesign.R;

/**
 * Created by Ray on 2017/4/16.
 */

public class NoticeDialog extends DialogFragment {
    /**
     * Use DialogFragment
     */
    private static NoticeDialog dialog;
    private NoticeDialogListener listener;

    public static NoticeDialog instance(String title) {
        // 使用Singleton
        if (dialog == null) {
            synchronized (NoticeDialog.class) {
                if (dialog == null) {
                    dialog = new NoticeDialog();
                }
            }
        }
        // 傳入title參數
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage("是否要離開NoticeDialog")
                .setIcon(R.drawable.ic_bird_shape)
                .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onDialogPositiveClick(NoticeDialog.this);
                        }
                    }
                })
                .setNegativeButton("等會兒", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onDialogNegativeClick(NoticeDialog.this);
                        }
                    }
                }).create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    public interface NoticeDialogListener {
        // 建立Listener 給外部呼叫的方法
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
