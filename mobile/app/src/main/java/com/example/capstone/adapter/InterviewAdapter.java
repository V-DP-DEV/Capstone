package com.example.capstone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.databinding.ItemInterviewBinding;
import com.example.capstone.model.Interview;
import com.google.android.material.chip.Chip;

import java.util.List;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.InterviewViewHolder> {

    public interface OnItemSelectedListener {
        void onItemSelected(Interview interview);
    }

    private List<Interview> interviews;
    private final OnItemSelectedListener listener;
    private int selectedPosition = -1;

    public InterviewAdapter(List<Interview> interviews, OnItemSelectedListener listener) {
        this.interviews = interviews;
        this.listener = listener;
    }
    @NonNull
    @Override
    public InterviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInterviewBinding binding = ItemInterviewBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new InterviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InterviewViewHolder holder, int position) {
        holder.bind(interviews.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return interviews != null ? interviews.size() : 0;
    }

    public void updateData(List<Interview> newList) {
        this.interviews = newList;
        this.selectedPosition = -1; // Reset selection state when list filters
        notifyDataSetChanged();
    }

    class InterviewViewHolder extends RecyclerView.ViewHolder {
        private final ItemInterviewBinding binding;

        public InterviewViewHolder(@NonNull ItemInterviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Interview interview, boolean isSelected) {
            binding.tvTitle.setText(interview.getTitle());
            binding.tvDifficulty.setText(interview.getDifficulty());

            // Render tag chips dynamically
            binding.chipGroupFocusAreas.removeAllViews();
            Context context = itemView.getContext();

            if (interview.getFocusAreas() != null) {
                for (String area : interview.getFocusAreas()) {
                    Chip chip = new Chip(context);
                    chip.setText(area);
                    chip.setChipBackgroundColor(ContextCompat.getColorStateList(context, R.color.tag_bg));
                    chip.setTextColor(ContextCompat.getColor(context, R.color.tag_text));
                    chip.setCheckable(false);
                    chip.setClickable(false);
                    chip.setChipCornerRadius(16f);
                    binding.chipGroupFocusAreas.addView(chip);
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
                    listener.onItemSelected(interview);
                }
            });
        }
    }
}
