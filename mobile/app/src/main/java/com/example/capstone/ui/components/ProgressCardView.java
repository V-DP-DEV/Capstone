package com.example.capstone.ui.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.capstone.R;

public class ProgressCardView extends FrameLayout {
private ProgressBar progressBar;
private TextView tvValue;
private TextView tvLabel;

public ProgressCardView(@NonNull Context context) {
    super(context);
    init(context);
}

    public ProgressCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
private void init(Context context) {
    LayoutInflater.from(context).inflate(R.layout.component_progress_card, this, true);
    progressBar = findViewById(R.id.progress_chart);
    tvValue = findViewById(R.id.tv_progress_value);
    tvLabel = findViewById(R.id.tv_progress_label);
}
    public void setProgress(int progress, String label, @DrawableRes int progressDrawableRes) {
        progressBar.setProgress(progress);
        if (progressDrawableRes != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), progressDrawableRes);
            progressBar.setProgressDrawable(drawable);
        }
        tvValue.setText(progress + "%");
        tvLabel.setText(label);
    }
}


