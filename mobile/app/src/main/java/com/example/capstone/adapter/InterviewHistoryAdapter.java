package com.example.capstone.adapter;

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
        holder.tvDifficulty.setText(summary.getDifficulty() != null ? summary.getDifficulty() : "");

        if (summary.isCompletedInTime()) {
            holder.tvCompletionStatus.setText("Completed in time");
            holder.tvCompletionStatus.setTextColor(0xFF08B425); // Green
        } else {
            holder.tvCompletionStatus.setText("Completed late");
            holder.tvCompletionStatus.setTextColor(0xFFE49596); // Accent Warning
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInterviewName, tvDifficulty, tvCompletionStatus, tvDate;
        ImageButton btnOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInterviewName = itemView.findViewById(R.id.tv_interview_name);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            tvCompletionStatus = itemView.findViewById(R.id.tv_completion_status);
            tvDate = itemView.findViewById(R.id.tv_date);
            btnOptions = itemView.findViewById(R.id.btn_options);
        }
    }
}