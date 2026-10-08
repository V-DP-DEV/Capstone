package com.example.capstone.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.capstone.R;
import com.google.android.material.card.MaterialCardView;



public class DifficultyOptionView extends FrameLayout {

        private MaterialCardView cardRoot;
        private View viewIndicator;
        private TextView tvTitle;

        public DifficultyOptionView(@NonNull Context context) {
            super(context);
            init(context, null);
        }

        public DifficultyOptionView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        public DifficultyOptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context, attrs);
        }

        private void init(Context context, @Nullable AttributeSet attrs) {
            LayoutInflater.from(context).inflate(R.layout.component_difficulty_option, this, true);
            cardRoot = findViewById(R.id.card_difficulty_root);
            viewIndicator = findViewById(R.id.view_indicator);
            tvTitle = findViewById(R.id.tv_difficulty_title);

            if (attrs != null) {
                TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DifficultyOptionView, 0, 0);
                String title = a.getString(R.styleable.DifficultyOptionView_optionTitle);
                int color = a.getColor(
                        R.styleable.DifficultyOptionView_indicatorColor,
                        ContextCompat.getColor(context, R.color.progress_green)
                );
                boolean selected = a.getBoolean(R.styleable.DifficultyOptionView_isSelected, false);

                if (title != null) {
                    tvTitle.setText(title);
                }
                setIndicatorColor(color);
                setSelectedState(selected);

                a.recycle();
            }
        }

        public void setIndicatorColor(@ColorInt int color) {
            GradientDrawable drawable = (GradientDrawable) viewIndicator.getBackground();
            if (drawable == null) {
                drawable = new GradientDrawable();
            }
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(16f);
            drawable.setColor(color);
            viewIndicator.setBackground(drawable);
        }

        public void setSelectedState(boolean isSelected) {
            if (isSelected) {
                cardRoot.setStrokeWidth(4);
                cardRoot.setStrokeColor(ContextCompat.getColor(getContext(), R.color.primary_purple));
            } else {
                cardRoot.setStrokeWidth(0);
            }
        }
    }


