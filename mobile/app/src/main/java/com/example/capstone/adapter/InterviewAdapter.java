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

/**
 * InterviewAdapter manages the RecyclerView list displaying mock interview options on the selection screen.
 * It renders interview titles, difficulties, dynamically generates category and question type chips,
 * and maintains single-item selection state.
 */
public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.InterviewViewHolder> {

    /**
     * Interface definition for a callback to be invoked when an interview item is selected.
     */
    public interface OnInterviewSelectedListener {
        void onInterviewSelected(UserInterview interview);
    }

    private List<UserInterview> interviews;
    private final OnInterviewSelectedListener listener;
    private int selectedPosition = -1;

    /**
     * Constructs a new InterviewAdapter.
     *
     * @param interviews Initial list of interviews to display.
     * @param listener   Callback listener for item selection events.
     */
    public InterviewAdapter(List<UserInterview> interviews, OnInterviewSelectedListener listener) {
        this.interviews = interviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InterviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual interview items
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_difficulty, parent, false);
        return new InterviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterviewViewHolder holder, int position) {
        // Bind data and pass whether this specific position is currently selected
        holder.bind(interviews.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return interviews != null ? interviews.size() : 0;
    }

    /**
     * Public data injection hook for Person 2 / ViewModel to push updated or filtered lists into the adapter.
     */
    public void updateData(List<UserInterview> newList) {
        this.interviews = newList;
        notifyDataSetChanged();
    }

    /**
     * Retrieves the currently selected interview object, or null if no item is selected.
     */
    public UserInterview getSelectedInterview() {
        if (selectedPosition != -1 && selectedPosition < getItemCount()) {
            return interviews.get(selectedPosition);
        }
        return null;
    }

    /**
     * ViewHolder class holding view references for individual interview list rows.
     */
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

        /**
         * Binds interview data to the UI views, generates Material Chips for tags,
         * and configures selection and click behaviors.
         */
        public void bind(final UserInterview interview, boolean isSelected) {
            // Bind Title Name
            if (tvName != null && interview.getName() != null) {
                tvName.setText(interview.getName());
            }

            // Bind Difficulty
            if (tvDescription != null && interview.getDifficulty() != null) {
                tvDescription.setText(interview.getDifficulty());
            }

            // Clear previous chips and dynamically populate tag chips
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

            // Apply selected background state to the root view
            itemView.setSelected(isSelected);

            // Handle card click events, managing single-selection highlights
            itemView.setOnClickListener(v -> {
                int previousSelected = selectedPosition;
                selectedPosition = getBindingAdapterPosition();

                // Refresh previously selected item and newly selected item visually
                if (previousSelected != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelected);
                }
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition);
                }

                // Trigger selection callback listener
                if (listener != null && selectedPosition != RecyclerView.NO_POSITION) {
                    listener.onInterviewSelected(interview);
                }
            });
        }

        /**
         * Extracts a deduplicated list of tags (Category name + unique question types) from the interview.
         */
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