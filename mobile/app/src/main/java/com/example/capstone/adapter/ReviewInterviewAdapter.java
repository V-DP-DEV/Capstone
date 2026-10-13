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
import com.example.capstone.domainModels.UserQuestion;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class ReviewInterviewAdapter extends RecyclerView.Adapter<ReviewInterviewAdapter.ReviewViewHolder> {

    public interface OnQuestionClickListener {
        void onQuestionClick(UserQuestion question, int position);
    }

    private List<UserQuestion> questionsList;
    private OnQuestionClickListener listener;

    public ReviewInterviewAdapter(List<UserQuestion> questionList) {
        this(questionList, null);
    }

    public ReviewInterviewAdapter(List<UserQuestion> questionList, OnQuestionClickListener listener) {
        this.questionsList = questionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interview, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        UserQuestion question = questionsList.get(position);
        holder.bind(question, position + 1);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuestionClick(question, holder.getBindingAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsList != null ? questionsList.size() : 0;
    }

    public void updateData(List<UserQuestion> newList) {
        this.questionsList = newList;
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNumber;
        private final TextView tvQuestionText;
        private final TextView tvScore;
        private final TextView tvDuration;
        private final ChipGroup chipGroupTags;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_q_number);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            tvScore = itemView.findViewById(R.id.tv_score);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            chipGroupTags = itemView.findViewById(R.id.chipGroupTags);
        }

        public void bind(UserQuestion question, int positionNumber) {
            if (question == null) return;

            // 1. Question Number Badge
            if (tvNumber != null) {
                tvNumber.setText(String.valueOf(positionNumber));
            }

            // 2. Question Text
            if (tvQuestionText != null && question.getText() != null) {
                tvQuestionText.setText(question.getText());
            }

            // 3. Score & Duration Placeholders
            if (tvScore != null) {
                tvScore.setText("5/5");
            }

            if (tvDuration != null) {
                tvDuration.setText("01:24");
            }

            // 4. Dynamic Chips for Type and Difficulty
            if (chipGroupTags != null) {
                chipGroupTags.removeAllViews();
                Context context = itemView.getContext();

                if (question.getType() != null && !question.getType().trim().isEmpty()) {
                    addChip(context, question.getType());
                }

                if (question.getDifficulty() != null && !question.getDifficulty().trim().isEmpty()) {
                    addChip(context, question.getDifficulty());
                }
            }
        }

        private void addChip(Context context, String text) {
            Chip chip = new Chip(context);
            chip.setText(text);
            chip.setChipBackgroundColor(ContextCompat.getColorStateList(context, R.color.tag_bg));
            chip.setTextColor(ContextCompat.getColor(context, R.color.tag_text));
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setChipCornerRadius(16f);
            chipGroupTags.addView(chip);
        }
    }
}