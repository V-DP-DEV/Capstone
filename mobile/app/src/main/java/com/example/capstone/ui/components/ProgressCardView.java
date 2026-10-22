package com.example.capstone.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.card.MaterialCardView;
import com.example.capstone.R;

public class ProgressCardView extends MaterialCardView {

    private ProgressBar progressChart;
    private TextView tvProgressValue;
    private TextView tvProgressLabel;

    public ProgressCardView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProgressCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void setProgressData(int percentage, String label) {
        if (progressChart != null) {
            progressChart.setProgress(percentage);
        }
        if (tvProgressValue != null) {
            tvProgressValue.setText(String.format("%d%%", percentage));
        }
        if (tvProgressLabel != null) {
            tvProgressLabel.setText(label);
        }
    }
    public void setProgressDrawable(int drawableResId) {
        if (progressChart != null) {
            progressChart.setProgressDrawable(getResources().getDrawable(drawableResId, getContext().getTheme()));
        }
    }
    private void init(Context context) {
        // Enforce background and clean rounded corners without borders
        setCardBackgroundColor(getResources().getColor(R.color.card_background, context.getTheme()));

        float radiusInPx = 24 * context.getResources().getDisplayMetrics().density;
        setRadius(radiusInPx);

        setCardElevation(2f * context.getResources().getDisplayMetrics().density);
        setStrokeWidth(0);
        setStrokeColor(Color.TRANSPARENT);
        setPreventCornerOverlap(true);
        setClipToOutline(true);

        // Inflate the inner ConstraintLayout view
        inflate(context, R.layout.component_progress_card, this);

        progressChart = findViewById(R.id.progress_chart);
        tvProgressValue = findViewById(R.id.tv_progress_value);
        tvProgressLabel = findViewById(R.id.tv_progress_label);
    }


}