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
import com.example.capstone.ui.mock.Difficulty;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.List;

public class DifficultyAdapter extends RecyclerView.Adapter<DifficultyAdapter.DifficultyViewHolder> {

    public interface OnDifficultySelectedListener {
        void onDifficultySelected(Difficulty difficulty);
    }

    private List<Difficulty> difficulties;
    private final OnDifficultySelectedListener listener;
    private int selectedPosition = -1;

    public DifficultyAdapter(List<Difficulty> difficulties, OnDifficultySelectedListener listener) {
        this.difficulties = difficulties;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DifficultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_difficulty, parent, false);
        return new DifficultyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DifficultyViewHolder holder, int position) {
        holder.bind(difficulties.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return difficulties != null ? difficulties.size() : 0;
    }

    public void updateData(List<Difficulty> newList) {
        this.difficulties = newList;
        notifyDataSetChanged();
    }

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

        public void bind(final Difficulty difficulty, boolean isSelected) {
            tvName.setText(difficulty.getName());
            tvDescription.setText(difficulty.getDescription());

            chipGroup.removeAllViews();
            Context context = itemView.getContext();

            if (difficulty.getTags() != null) {
                for (String tag : difficulty.getTags()) {
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
                    listener.onDifficultySelected(difficulty);
                }
            });
        }
    }
}
