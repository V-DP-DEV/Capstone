package com.capstone.myapplication.UI.Component;

import android.content.Context;
import android.widget.Toast;

public class ErrorHandler {

    public static void showError(Context context, String message) {
        Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}
