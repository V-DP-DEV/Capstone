package com.example.capstone.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.capstone.R;

public class QuickActionView extends FrameLayout {
    private ImageView ivIcon;
    private TextView tvTitle;

    public QuickActionView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public QuickActionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public QuickActionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.component_quick_action, this, true);
        ivIcon = findViewById(R.id.iv_action_icon);
        tvTitle = findViewById(R.id.tv_action_title);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QuickActionView);
            try {
                String title = a.getString(R.styleable.QuickActionView_actionTitle);
                int iconRes = a.getResourceId(R.styleable.QuickActionView_actionIcon, 0);
                int tintColor = a.getColor(R.styleable.QuickActionView_actionTint, 0);

                if (title != null) {
                    tvTitle.setText(title);
                }
                if (iconRes != 0) {
                    ivIcon.setImageResource(iconRes);
                }
                if (tintColor != 0) {
                    ivIcon.setColorFilter(tintColor);
                }
            } finally {
                a.recycle();
            }
        }
    }

    public void setActionData(String title, @DrawableRes int iconRes, @ColorInt int tintColor) {
        tvTitle.setText(title);
        ivIcon.setImageResource(iconRes);
        ivIcon.setColorFilter(tintColor);
    }

    public void setDisabled(boolean isDisabled) {
        if (isDisabled) {
            tvTitle.setTextColor(getContext().getColor(R.color.text_disabled));
            ivIcon.setColorFilter(getContext().getColor(R.color.text_disabled));
        }
    }
}
