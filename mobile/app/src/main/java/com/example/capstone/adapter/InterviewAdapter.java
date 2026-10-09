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
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserQuestion;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.InterviewViewHolder> {

    public interface OnInterviewSelectedListener {
        void onInterviewSelected(UserInterview interview);
    }

    private List<UserInterview> interviews;
    private final OnInterviewSelectedListener listener;
    private int selectedPosition = -1;

    public InterviewAdapter(List<UserInterview> interviews, OnInterviewSelectedListener listener) {
        this.interviews = interviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InterviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_difficulty, parent, false);
        return new InterviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterviewViewHolder holder, int position) {
        holder.bind(interviews.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return interviews != null ? interviews.size() : 0;
    }

    public void updateData(List<UserInterview> newList) {
        this.interviews = newList;
        notifyDataSetChanged();
    }

    public UserInterview getSelectedInterview() {
        if (selectedPosition != -1 && selectedPosition < getItemCount()) {
            return interviews.get(selectedPosition);
        }
        return null;
    }

    class InterviewViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvDescription;
        private final ChipGroup chipGroup;

        public InterviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvDifficultyName);
            tvDescription = itemView.findViewById(R.id.tvDifficultyDesc);
            chipGroup = itemView.findViewById(R.id.chipGroupTags);
        }

        public void bind(final UserInterview interview, boolean isSelected) {
            // Bind Title Name
            if (tvName != null && interview.getName() != null) {
                tvName.setText(interview.getName());
            }

            // Bind Difficulty
            if (tvDescription != null && interview.getDifficulty() != null) {
                tvDescription.setText(interview.getDifficulty());
            }

            // Extract Tags & Category Chips
            chipGroup.removeAllViews();
            Context context = itemView.getContext();

            List<String> tags = getTagsList(interview);
            if (tags != null) {
                for (String tag : tags) {
                    Chip chip = new Chip(context);
                    chip.setText(tag);
                    chip.setChipBackgroundColor(ContextCompat.getColorStateList(context, R.color.tag_bg));
                    chip.setTextColor(ContextCompat.getColor(context, R.color.tag_text));
                    chip.setCheckable(false);
                    chip.setClickable(false);
                    chip.setChipCornerRadius(16f);
                    chipGroup.addView(chip);
                }
            }

            itemView.setSelected(isSelected);
            itemView.setOnClickListener(v -> {
                int previousSelected = selectedPosition;
                selectedPosition = getBindingAdapterPosition();

                if (previousSelected != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelected);
                }
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition);
                }

                if (listener != null && selectedPosition != RecyclerView.NO_POSITION) {
                    listener.onInterviewSelected(interview);
                }
            });
        }

        private List<String> getTagsList(UserInterview interview) {
            if (interview == null) return null;

            List<String> tags = new ArrayList<>();

            // 1. Add Category Name if present
            if (interview.getCategory() != null && interview.getCategory().getName() != null) {
                tags.add(interview.getCategory().getName());
            }

            // 2. Add Question Types from questions list
            if (interview.getQuestions() != null) {
                for (UserQuestion question : interview.getQuestions()) {
                    if (question != null && question.getType() != null && !question.getType().trim().isEmpty()) {
                        if (!tags.contains(question.getType())) {
                            tags.add(question.getType());
                        }
                    }
                }
            }

            return tags;
        }
    }
}
