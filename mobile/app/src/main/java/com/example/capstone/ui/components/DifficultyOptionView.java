package com.example.capstone.ui.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.capstone.R;
import com.example.capstone.databinding.ComponentDifficultyOptionBinding;

public class DifficultyOptionView extends FrameLayout {

    private ComponentDifficultyOptionBinding binding;

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

    private void init(Context context, AttributeSet attrs) {
        // Inflate component_difficulty_option.xml into this FrameLayout
        binding = ComponentDifficultyOptionBinding.inflate(LayoutInflater.from(context), this, true);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DifficultyOptionView);

            String title = a.getString(R.styleable.DifficultyOptionView_optionTitle);
            int indicatorColor = a.getColor(
                    R.styleable.DifficultyOptionView_indicatorColor,
                    ContextCompat.getColor(context, R.color.primary_purple)
            );

            if (title != null) {
                binding.tvDifficultyTitle.setText(title);
            }
            binding.viewIndicator.setBackgroundTintList(ColorStateList.valueOf(indicatorColor));

            a.recycle();
        }
    }

    public void setOptionTitle(String title) {
        if (binding != null) {
            binding.tvDifficultyTitle.setText(title);
        }
    }

    public void setSelectedState(boolean isSelected) {
        if (binding == null) return;

        binding.cardDifficultyRoot.setSelected(isSelected);

        int strokeWidthPx = isSelected ? (int) (2 * getResources().getDisplayMetrics().density) : 0;
        binding.cardDifficultyRoot.setStrokeWidth(strokeWidthPx);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (binding != null) {
            // Forward click events to the root card layout
            binding.cardDifficultyRoot.setOnClickListener(l);
        } else {
            super.setOnClickListener(l);
        }
    }
}