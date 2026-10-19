package com.example.capstone.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.domainModels.UserInterviewAttemptSummary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InterviewHistoryAdapter extends RecyclerView.Adapter<InterviewHistoryAdapter.ViewHolder> {

    private List<UserInterviewAttemptSummary> attemptList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onInterviewClick(UserInterviewAttemptSummary summary);
        void onDeleteClick(UserInterviewAttemptSummary summary, int position);
    }

    public InterviewHistoryAdapter(List<UserInterviewAttemptSummary> attemptList, OnItemClickListener listener) {
        this.attemptList = attemptList;
        this.listener = listener;
    }

    public void updateData(List<UserInterviewAttemptSummary> newData) {
        this.attemptList = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_interview_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserInterviewAttemptSummary summary = attemptList.get(position);

        holder.tvInterviewName.setText(summary.getName() != null ? summary.getName() : "Interview");
        String difficulty = summary.getDifficulty() != null ? summary.getDifficulty() : "";
        holder.tvDifficulty.setText(difficulty);
        holder.tvDifficulty.setTextColor(getColorForDifficulty(difficulty));



        // Bind and color-code score percentage
        if (holder.tvPercentage != null) {
            double scorePercentage = summary.getScore();
            double normalizedPercentage = scorePercentage <= 1.0 ? scorePercentage * 100.0 : scorePercentage;

            holder.tvPercentage.setText(String.format(Locale.getDefault(), "%.0f%%", normalizedPercentage));
            holder.tvPercentage.setTextColor(getColorForPercentage(normalizedPercentage));
        }

        if (summary.isCompletedInTime()) {
            holder.tvCompletionStatus.setText("Completed in time");
            holder.tvCompletionStatus.setTextColor(Color.parseColor("#FF08B425")); // Green (Good)
        } else {
            holder.tvCompletionStatus.setText("Completed late");
            holder.tvCompletionStatus.setTextColor(Color.parseColor("#FFF7A93B")); // Orange / Warning
        }

        if (summary.getCompletedAt() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            holder.tvDate.setText(sdf.format(new Date(summary.getCompletedAt())));
        } else {
            holder.tvDate.setText("Unknown Date");
        }

        holder.btnOptions.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenu().add("View Interview");
            popup.getMenu().add("Delete");

            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("View Interview")) {
                    if (listener != null) listener.onInterviewClick(summary);
                    return true;
                } else if (item.getTitle().equals("Delete")) {
                    if (listener != null) listener.onDeleteClick(summary, holder.getAdapterPosition());
                    return true;
                }
                return false;
            });
            popup.show();
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onInterviewClick(summary);
        });
    }

    @Override
    public int getItemCount() {
        return attemptList != null ? attemptList.size() : 0;
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

    private int getColorForDifficulty(String difficulty) {
        if (difficulty == null) return Color.parseColor("#FF8A8A97");

        switch (difficulty.trim().toLowerCase(Locale.ROOT)) {
            case "easy":
                return Color.parseColor("#FF08B425"); // Green
            case "medium":
                return Color.parseColor("#FFF7A93B"); // Orange
            case "hard":
                return Color.parseColor("#FFEF5350"); // Red
            default:
                return Color.parseColor("#FF8A8A97"); // Default Gray
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInterviewName, tvDifficulty, tvCompletionStatus, tvDate, tvPercentage;
        ImageButton btnOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInterviewName = itemView.findViewById(R.id.tv_interview_name);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            tvCompletionStatus = itemView.findViewById(R.id.tv_completion_status);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvPercentage = itemView.findViewById(R.id.tv_percentage); // <--- THIS WAS MISSING!
            btnOptions = itemView.findViewById(R.id.btn_options);
        }
    }
}