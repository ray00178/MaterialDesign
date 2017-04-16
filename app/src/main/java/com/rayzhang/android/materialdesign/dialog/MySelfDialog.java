package com.rayzhang.android.materialdesign.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rayzhang.android.materialdesign.R;

/**
 * Created by Ray on 2017/4/16.
 */

public class MySelfDialog extends DialogFragment {
    /**
     * Use DialogFragment but view by myself.
     */

    private static MySelfDialog dialog;
    private TextView mTextTitle, mTextMsg;
    private Button mButOK, mButCancel;
    private MySelfDialogListener listener;

    public static MySelfDialog instance(String title) {
        if (dialog == null) {
            synchronized (MySelfDialog.class) {
                if (dialog == null) {
                    dialog = new MySelfDialog();
                }
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // android.R.style.Theme_Holo_Light_Dialog
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogTheme);
        //setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MyDialogTheme);
        //setStyle(DialogFragment.STYLE_NO_INPUT, R.style.MyDialogTheme);
        //setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
      }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        View view = inflater.inflate(R.layout.dialog_myself, container, false);
        mTextTitle = (TextView) view.findViewById(R.id.mTextTitle);
        mTextMsg = (TextView) view.findViewById(R.id.mTextMsg);
        mButOK = (Button) view.findViewById(R.id.mButOK);
        mButCancel = (Button) view.findViewById(R.id.mButCancel);

        mTextTitle.setText(title);
        mTextMsg.setText("是否要離開此頁面MySelfDialog");

        mButOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDialogOKClick(MySelfDialog.this);
                }
            }
        });
        mButCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDialogCancelClick(MySelfDialog.this);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MySelfDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    public interface MySelfDialogListener {
        void onDialogOKClick(DialogFragment dialog);

        void onDialogCancelClick(DialogFragment dialog);
    }
}
