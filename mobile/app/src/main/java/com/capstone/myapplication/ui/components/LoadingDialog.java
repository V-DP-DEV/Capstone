package com.capstone.myapplication.UI.Component;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.capstone.myapplication.R;

public class LoadingDialog {

    private final Dialog dialog;

    public LoadingDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(
                LayoutInflater.from(context).inflate(R.layout.loading, null)
        );

        dialog.setCancelable(false);
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}