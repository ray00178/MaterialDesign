package com.rayzhang.android.materialdesign;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rayzhang.android.materialdesign.dialog.MySelfDialog;
import com.rayzhang.android.materialdesign.dialog.NoticeDialog;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener, NoticeDialog.NoticeDialogListener,
        MySelfDialog.MySelfDialogListener {
    /**
     * Dialog Demo
     */
    private static final String TAG = DialogActivity.class.getSimpleName();
    private Button mButAlert, mButFragment, mButSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        initView();
    }

    private void initView() {
        mButAlert = (Button) findViewById(R.id.mButAlert);
        mButFragment = (Button) findViewById(R.id.mButFragment);
        mButSelf = (Button) findViewById(R.id.mButSelf);

        mButAlert.setOnClickListener(this);
        mButFragment.setOnClickListener(this);
        mButSelf.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mButAlert:
                // Use AlertDialog
                showAlertDailog();
                break;
            case R.id.mButFragment:
                // Use DialogFragment
                showDialogFragment();
                break;
            case R.id.mButSelf:
                // Use DialogFragment by myself
                showMySelfDialog();
                break;
        }
    }

    private void showAlertDailog() {
        AlertDialog dialog = new AlertDialog.Builder(DialogActivity.this)
                .setTitle("離開此頁面")
                .setMessage("是否要離開?")
                .setIcon(R.drawable.ic_bird_shape)
                .setCancelable(false)
                .setNegativeButton("再等等", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logStr("witch:" + which);
                    }
                })
                .setNeutralButton("忽略", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logStr("witch:" + which);
                    }
                })
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logStr("witch:" + which);
                    }
                }).create();
        dialog.show();
    }

    private void showDialogFragment() {
        NoticeDialog dialog = NoticeDialog.instance("離開此頁面");
        dialog.show(getSupportFragmentManager(), "NoticeDialog");
    }

    private void showMySelfDialog() {
        // 可以透過FragmentTransaction來管理
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("MySelfDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        MySelfDialog dialog = MySelfDialog.instance("離開此頁面");
        dialog.show(getSupportFragmentManager(), "MySelfDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        logStr("onDialogPositiveClick");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
        logStr("onDialogNegativeClick");
    }

    @Override
    public void onDialogOKClick(DialogFragment dialog) {
        dialog.dismiss();
        logStr("onDialogOKClick");
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {
        dialog.dismiss();
        logStr("onDialogCancelClick");
    }

    private void logStr(String str) {
        Log.d(TAG, str);
    }
}
