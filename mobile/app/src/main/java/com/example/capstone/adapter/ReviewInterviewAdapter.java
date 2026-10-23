package com.example.capstone.adapter;

import android.content.Context;
import android.graphics.Color;
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

/**
 * RecyclerView Adapter responsible for rendering individual question attempts in the review interview list.
 * Handles conditional score visibility based on analysis state, time formatting, dynamic Material Chips
 * for types and difficulties, and score threshold color coding.
 */

public class ReviewInterviewAdapter extends RecyclerView.Adapter<ReviewInterviewAdapter.ViewHolder> {

    private List<UserQuestionAttempt> questionAttempts;
    private boolean isAnalysed;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(UserQuestionAttempt attempt, int position);
    }

    public ReviewInterviewAdapter(List<UserQuestionAttempt> questionAttempts, boolean isAnalysed, OnItemClickListener listener) {
        this.questionAttempts = questionAttempts;
        this.isAnalysed = isAnalysed;
        this.listener = listener;
    }

    public void updateData(List<UserQuestionAttempt> newData, boolean isAnalysed) {
        this.questionAttempts = newData;
        this.isAnalysed = isAnalysed;
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
        if (questionAttempts == null || position >= questionAttempts.size()) return;
        UserQuestionAttempt attempt = questionAttempts.get(position);


        if (holder.tvQuestionNumber != null) {
            holder.tvQuestionNumber.setText(String.valueOf(position + 1));
        }


        if (holder.tvQuestionText != null) {
            holder.tvQuestionText.setText(attempt.getQuestion() != null ? attempt.getQuestion() : "No question text");
        }


        if (holder.tvQuestionScore != null) {
            if (isAnalysed) {
                holder.tvQuestionScore.setVisibility(View.VISIBLE);
                double score = attempt.getScore();
                double maxScore = (double) attempt.getMaxScore();
                holder.tvQuestionScore.setText(String.format(Locale.getDefault(), "%.0f/%.0f", score, maxScore));

                double percentage = maxScore > 0 ? (score / maxScore) * 100.0 : 0.0;
                holder.tvQuestionScore.setTextColor(getColorForPercentage(percentage));
            } else {

                holder.tvQuestionScore.setVisibility(View.GONE);
            }
        }


        if (holder.tvQuestionTime != null) {
            long minutes = attempt.getSecondsSpent() / 60;
            long seconds = attempt.getSecondsSpent() % 60;
            holder.tvQuestionTime.setText(String.format(Locale.getDefault(), "⏱ %02d:%02d", minutes, seconds));
        }


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

    private int getColorForPercentage(double percentage) {
        if (percentage < 40.0) {
            return Color.parseColor("#FFEF5350"); // Red
        } else if (percentage < 70.0) {
            return Color.parseColor("#FFF7A93B"); // Orange
        } else {
            return Color.parseColor("#FF08B425"); // Green
        }
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