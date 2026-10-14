package com.example.capstone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.domainModels.UserQuestionAttempt;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Locale;

public class ReviewInterviewAdapter extends RecyclerView.Adapter<ReviewInterviewAdapter.ViewHolder> {

    private List<UserQuestionAttempt> questionAttempts;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(UserQuestionAttempt attempt, int position);
    }

    public ReviewInterviewAdapter(List<UserQuestionAttempt> questionAttempts, OnItemClickListener listener) {
        this.questionAttempts = questionAttempts;
        this.listener = listener;
    }

    public void updateData(List<UserQuestionAttempt> newData) {
        this.questionAttempts = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserQuestionAttempt attempt = questionAttempts.get(position);

        // Badge Question Number
        if (holder.tvQuestionNumber != null) {
            holder.tvQuestionNumber.setText(String.valueOf(position + 1));
        }

        // Question Body
        if (holder.tvQuestionText != null) {
            holder.tvQuestionText.setText(attempt.getQuestion() != null ? attempt.getQuestion() : "No question text");
        }

        // Score (Earned / Max)
        if (holder.tvQuestionScore != null) {
            holder.tvQuestionScore.setText(String.format(Locale.getDefault(), "%.0f/%.0f",
                    attempt.getScore(), (double) attempt.getMaxScore()));
        }

        // Seconds Spent
        if (holder.tvQuestionTime != null) {
            long minutes = attempt.getSecondsSpent() / 60;
            long seconds = attempt.getSecondsSpent() % 60;
            holder.tvQuestionTime.setText(String.format(Locale.getDefault(), "⏱ %02d:%02d", minutes, seconds));
        }

        // Question Type & Difficulty Chips
        if (holder.chipGroupTags != null) {
            holder.chipGroupTags.removeAllViews();
            Context context = holder.itemView.getContext();

            if (attempt.getType() != null && !attempt.getType().trim().isEmpty()) {
                Chip typeChip = new Chip(context);
                typeChip.setText(attempt.getType());
                typeChip.setChipBackgroundColor(ContextCompat.getColorStateList(context, R.color.tag_bg));
                typeChip.setTextColor(ContextCompat.getColor(context, R.color.tag_text));
                typeChip.setCheckable(false);
                typeChip.setClickable(false);
                typeChip.setChipCornerRadius(16f);
                holder.chipGroupTags.addView(typeChip);
            }

            if (attempt.getDifficulty() != null && !attempt.getDifficulty().trim().isEmpty()) {
                Chip diffChip = new Chip(context);
                diffChip.setText(attempt.getDifficulty());
                diffChip.setChipBackgroundColor(ContextCompat.getColorStateList(context, R.color.tag_bg));
                diffChip.setTextColor(ContextCompat.getColor(context, R.color.tag_text));
                diffChip.setCheckable(false);
                diffChip.setClickable(false);
                diffChip.setChipCornerRadius(16f);
                holder.chipGroupTags.addView(diffChip);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(attempt, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionAttempts != null ? questionAttempts.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvQuestionText, tvQuestionScore, tvQuestionTime;
        ChipGroup chipGroupTags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tv_q_number);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            tvQuestionScore = itemView.findViewById(R.id.tv_score);
            tvQuestionTime = itemView.findViewById(R.id.tv_duration);
            chipGroupTags = itemView.findViewById(R.id.chipGroupTags);
        }
    }
}