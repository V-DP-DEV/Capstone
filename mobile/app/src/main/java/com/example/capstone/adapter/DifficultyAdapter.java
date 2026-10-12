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
 * DifficultyAdapter manages the RecyclerView list displaying mock interview options,
 * rendering metadata, dynamically generating category/question tags using Material Chips,
 * and handling single-item selection states.
 */
public class DifficultyAdapter extends RecyclerView.Adapter<DifficultyAdapter.DifficultyViewHolder> {

    /**
     * Interface definition for a callback to be invoked when an interview item is selected.
     */
    public interface OnDifficultySelectedListener {
        void onDifficultySelected(UserInterview userInterview);
    }

    private List<UserInterview> userInterviews;
    private final OnDifficultySelectedListener listener;
    private int selectedPosition = -1;

    /**
     * Constructs a new DifficultyAdapter.
     *
     * @param userInterviews Initial list of interview items to display.
     * @param listener       Callback listener for item selection events.
     */
    public DifficultyAdapter(List<UserInterview> userInterviews, OnDifficultySelectedListener listener) {
        this.userInterviews = userInterviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DifficultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the individual card layout for each list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_difficulty, parent, false);
        return new DifficultyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DifficultyViewHolder holder, int position) {
        // Bind item data and pass whether this specific position is currently selected
        holder.bind(userInterviews.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return userInterviews != null ? userInterviews.size() : 0;
    }

    /**
     * Public data injection hook for Person 2 / ViewModel to push updated lists into the adapter.
     */
    public void updateData(List<UserInterview> newList) {
        this.userInterviews = newList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class holding references to views within an individual interview list item.
     */
    class DifficultyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvDescription;
        private final ChipGroup chipGroup;

        public DifficultyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvDifficultyName);
            tvDescription = itemView.findViewById(R.id.tvDifficultyDesc);
            chipGroup = itemView.findViewById(R.id.chipGroupTags);
        }

        /**
         * Binds interview data to UI components and configures selection highlighting and click listeners.
         */
        public void bind(final UserInterview userInterview, boolean isSelected) {
            if (tvName != null && userInterview.getName() != null) {
                tvName.setText(userInterview.getName());
            }

            if (tvDescription != null && userInterview.getDifficulty() != null) {
                tvDescription.setText(userInterview.getDifficulty());
            }

            // Clear previous chips and dynamically generate new ones for tags
            chipGroup.removeAllViews();
            Context context = itemView.getContext();

            List<String> tags = getTagsList(userInterview);
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

            // Handle card click events, updating single-selection states visually
            itemView.setOnClickListener(v -> {
                int previousSelected = selectedPosition;
                selectedPosition = getBindingAdapterPosition();

                // Refresh previously selected item and newly selected item to update highlight states
                if (previousSelected != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelected);
                }
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition);
                }

                // Trigger selection callback listener
                if (listener != null && selectedPosition != RecyclerView.NO_POSITION) {
                    listener.onDifficultySelected(userInterview);
                }
            });
        }

        /**
         * Extracts a deduplicated list of tags (Category name + unique question types) from the interview object.
         */
        private List<String> getTagsList(UserInterview interview) {
            if (interview == null) return null;

            List<String> tags = new ArrayList<>();

            // 1. Add Category name if present
            if (interview.getCategory() != null && interview.getCategory().getName() != null) {
                tags.add(interview.getCategory().getName());
            }

            // 2. Extract unique question types from nested UserQuestion objects
            if (interview.getQuestions() != null) {
                for (UserQuestion q : interview.getQuestions()) {
                    if (q != null && q.getType() != null && !q.getType().trim().isEmpty()) {
                        if (!tags.contains(q.getType())) {
                            tags.add(q.getType());
                        }
                    }
                }
            }

            return tags;
        }
    }
}