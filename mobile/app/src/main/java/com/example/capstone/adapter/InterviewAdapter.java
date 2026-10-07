package com.example.capstone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.model.InterviewTopic;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class InterviewAdapter extends RecyclerView.Adapter<InterviewAdapter.ViewHolder> {

    public interface OnTopicClickListener {
        void onTopicClick(InterviewTopic topic);
    }

    private final List<InterviewTopic> topicsList = new ArrayList<>();
    private final OnTopicClickListener clickListener;

    public InterviewAdapter(OnTopicClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setTopics(List<InterviewTopic> newTopics) {
        topicsList.clear();
        topicsList.addAll(newTopics);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interview_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InterviewTopic topic = topicsList.get(position);
        holder.bind(topic, clickListener);
    }

    @Override
    public int getItemCount() {
        return topicsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDifficulty;
        private final ChipGroup chipGroupFocus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            chipGroupFocus = itemView.findViewById(R.id.chip_group_focus_areas);
        }

        public void bind(final InterviewTopic topic, final OnTopicClickListener listener) {
            Context context = itemView.getContext();
            tvTitle.setText(topic.getTitle());
            tvDifficulty.setText(topic.getDifficulty());


            chipGroupFocus.removeAllViews();
            for (String area : topic.getFocusAreas()) {
                Chip chip = new Chip(context);
                chip.setText(area);
                chip.setChipBackgroundColorResource(R.color.card_background);
                chip.setTextColor(context.getColor(R.color.text_primary));
                chip.setTextAppearance(R.style.TextStyle_16sp);
                chip.setChipCornerRadius(32f);
                chip.setEnsureMinTouchTargetSize(false);
                chipGroupFocus.addView(chip);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTopicClick(topic);
                }
            });
        }
    }
}