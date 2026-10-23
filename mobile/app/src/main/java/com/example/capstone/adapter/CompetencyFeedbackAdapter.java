package com.example.capstone.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.domainModels.UserAttemptCompetencyBreakdownFeedback;
import com.example.capstone.domainModels.UserAttemptCompetencyFeedback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter responsible for displaying structured competency feedback cards,
 * including score percentages, dynamic lists of strengths, areas for improvement,
 * and interactive expand/collapse toggle sections.
 */
public class CompetencyFeedbackAdapter extends RecyclerView.Adapter<CompetencyFeedbackAdapter.ViewHolder> {

    private List<UserAttemptCompetencyFeedback> feedbackList = new ArrayList<>();

    public void setData(List<UserAttemptCompetencyFeedback> data) {
        this.feedbackList = data != null ? data : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_competency_feedback_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(feedbackList.get(position));
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryTitle, tvScorePercent;
        View sectionStrengths, sectionAreasImprove;
        TextView tvStrengthsPlus, tvAreasPlus;
        LinearLayout layoutStrengthsContent, layoutAreasContent;

        boolean isStrengthsExpanded = false;
        boolean isAreasExpanded = true;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryTitle = itemView.findViewById(R.id.tv_category_title);
            tvScorePercent = itemView.findViewById(R.id.tv_score_percent);
            sectionStrengths = itemView.findViewById(R.id.section_strengths);
            sectionAreasImprove = itemView.findViewById(R.id.section_areas_improve);
            tvStrengthsPlus = itemView.findViewById(R.id.tv_strengths_plus);
            tvAreasPlus = itemView.findViewById(R.id.tv_areas_plus);
            layoutStrengthsContent = itemView.findViewById(R.id.layout_strengths_content);
            layoutAreasContent = itemView.findViewById(R.id.layout_areas_content);
        }

        void bind(UserAttemptCompetencyFeedback item) {
            Context context = itemView.getContext();


            if (tvCategoryTitle != null) {
                tvCategoryTitle.setText(item.getName() != null ? item.getName() : "Competency");
            }

            double percentage = item.getCompetencyPercentage();
            double normalized = percentage <= 1.0 ? percentage * 100.0 : percentage;
            if (tvScorePercent != null) {
                tvScorePercent.setText(String.format(Locale.getDefault(), "%.0f%%", normalized));
                tvScorePercent.setTextColor(getColorForPercentage(normalized));
            }

            layoutStrengthsContent.removeAllViews();
            List<UserAttemptCompetencyBreakdownFeedback> strengths = item.getStrengths();
            if (strengths != null && !strengths.isEmpty()) {
                for (UserAttemptCompetencyBreakdownFeedback b : strengths) {
                    addBreakdownRow(context, layoutStrengthsContent, b);
                }
            } else {
                addEmptyText(context, layoutStrengthsContent, "No clear strength demonstrated.");
            }

            layoutAreasContent.removeAllViews();
            List<UserAttemptCompetencyBreakdownFeedback> improvements = item.getImprovements();
            if (improvements != null && !improvements.isEmpty()) {
                for (UserAttemptCompetencyBreakdownFeedback b : improvements) {
                    addBreakdownRow(context, layoutAreasContent, b);
                }
            } else {
                addEmptyText(context, layoutAreasContent, "No areas to improve recorded.");
            }


            sectionStrengths.setOnClickListener(v -> {
                isStrengthsExpanded = !isStrengthsExpanded;
                layoutStrengthsContent.setVisibility(isStrengthsExpanded ? View.VISIBLE : View.GONE);
                tvStrengthsPlus.setText(isStrengthsExpanded ? "-" : "+");
            });

            sectionAreasImprove.setOnClickListener(v -> {
                isAreasExpanded = !isAreasExpanded;
                layoutAreasContent.setVisibility(isAreasExpanded ? View.VISIBLE : View.GONE);
                tvAreasPlus.setText(isAreasExpanded ? "-" : "+");
            });
        }


        private void addBreakdownRow(Context context, ViewGroup container, UserAttemptCompetencyBreakdownFeedback breakdown) {
            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setPadding(0, 8, 0, 12);

            TextView tvTitle = new TextView(context);
            double percentage = breakdown.getBreakdownPercentage();
            double normalized = percentage <= 1.0 ? percentage * 100.0 : percentage;

            tvTitle.setText(String.format(Locale.getDefault(), "• %s (%.0f%%)", breakdown.getName(), normalized));
            tvTitle.setTextSize(14f);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setTextColor(context.getResources().getColor(android.R.color.black, null));
            row.addView(tvTitle);

            TextView tvDesc = new TextView(context);
            tvDesc.setText(breakdown.getFeedback() != null ? breakdown.getFeedback() : "No qualitative feedback provided.");
            tvDesc.setTextSize(12f);
            tvDesc.setTextColor(context.getResources().getColor(android.R.color.darker_gray, null));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.topMargin = 4;
            tvDesc.setLayoutParams(lp);

            row.addView(tvDesc);
            container.addView(row);
        }

        private void addEmptyText(Context context, ViewGroup container, String text) {
            TextView tvEmpty = new TextView(context);
            tvEmpty.setText(text);
            tvEmpty.setTextColor(context.getResources().getColor(android.R.color.darker_gray, null));
            tvEmpty.setTextSize(14f);
            container.addView(tvEmpty);
        }



        private int getColorForPercentage(double percentage) {
            if (percentage < 40.0) return Color.parseColor("#FFEF5350");
            if (percentage < 70.0) return Color.parseColor("#FFF7A93B");
            return Color.parseColor("#FF08B425");
        }
    }
}