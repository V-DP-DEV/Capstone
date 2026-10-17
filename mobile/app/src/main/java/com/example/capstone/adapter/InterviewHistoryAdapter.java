package com.example.capstone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserQuestionAttempt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InterviewHistoryAdapter extends RecyclerView.Adapter<InterviewHistoryAdapter.ViewHolder> {

    private List<UserInterviewAttempt> attemptList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onInterviewClick(UserInterviewAttempt attempt);
    }

    public InterviewHistoryAdapter(List<UserInterviewAttempt> attemptList, OnItemClickListener listener) {
        this.attemptList = attemptList;
        this.listener = listener;
    }

    public void updateData(List<UserInterviewAttempt> newData) {
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
        UserInterviewAttempt attempt = attemptList.get(position);


        holder.tvInterviewName.setText(attempt.getName() != null ? attempt.getName() : "Interview");
        holder.tvDifficulty.setText(attempt.getDifficulty() != null ? attempt.getDifficulty() : "");


        double totalEarnedScore = 0;
        double totalMaxScore = 0;
        if (attempt.getQuestions() != null && !attempt.getQuestions().isEmpty()) {
            for (UserQuestionAttempt qa : attempt.getQuestions()) {
                totalEarnedScore += qa.getScore();
                totalMaxScore += qa.getMaxScore();
            }
        }
        int percentage = totalMaxScore > 0 ? (int) Math.round((totalEarnedScore / totalMaxScore) * 100) : 0;
        holder.tvScoreValue.setText(percentage + "%");


        if (attempt.isCompletedInTime()) {
            holder.tvCompletionStatus.setText("Completed in time");
            holder.tvCompletionStatus.setTextColor(0xFF08B425); // Green
        } else {
            holder.tvCompletionStatus.setText("Completed late");
            holder.tvCompletionStatus.setTextColor(0xFFE49596); // Accent / Warning
        }


        if (attempt.getCompletedAt() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            holder.tvDate.setText(sdf.format(new Date(attempt.getCompletedAt())));
        } else {
            holder.tvDate.setText("Unknown Date");
        }


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onInterviewClick(attempt);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attemptList != null ? attemptList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInterviewName, tvDifficulty, tvScoreValue, tvCompletionStatus, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInterviewName = itemView.findViewById(R.id.tv_interview_name);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            tvScoreValue = itemView.findViewById(R.id.tv_score_value);
            tvCompletionStatus = itemView.findViewById(R.id.tv_completion_status);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
