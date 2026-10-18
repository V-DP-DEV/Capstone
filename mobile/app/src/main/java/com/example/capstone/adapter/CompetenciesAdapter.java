package com.example.capstone.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.domainModels.CompetenciesAnalysis;
import com.example.capstone.domainModels.CompetenciesBreakdownAnalysis;

import java.util.ArrayList;
import java.util.List;

public class CompetenciesAdapter extends RecyclerView.Adapter<CompetenciesAdapter.ViewHolder> {

    private List<CompetenciesAnalysis> competenciesList = new ArrayList<>();

    public void setCompetenciesList(List<CompetenciesAnalysis> list) {
        this.competenciesList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill_breakdown_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompetenciesAnalysis item = competenciesList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return competenciesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardTitle, tvCardStatus, tvCardPercentageMain;
        TextView tvValueClarity, tvValueStructure, tvValueVocabulary;
        View indicatorClarity, indicatorStructure, indicatorVocabulary;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardTitle = itemView.findViewById(R.id.card_title);
            tvCardStatus = itemView.findViewById(R.id.card_status);
            tvCardPercentageMain = itemView.findViewById(R.id.card_percentage_main);

            tvValueClarity = itemView.findViewById(R.id.value_clarity);
            tvValueStructure = itemView.findViewById(R.id.value_structure);
            tvValueVocabulary = itemView.findViewById(R.id.value_vocabulary);

            indicatorClarity = itemView.findViewById(R.id.indicator_clarity);
            indicatorStructure = itemView.findViewById(R.id.indicator_structure);
            indicatorVocabulary = itemView.findViewById(R.id.indicator_vocabulary);
        }

        void bind(CompetenciesAnalysis item) {
            if (tvCardTitle != null) {
                tvCardTitle.setText(item.getName());
            }
            if (tvCardPercentageMain != null) {
                tvCardPercentageMain.setText(String.format("%.0f%%", item.getPercentage()));
                // Optionally color-code main card percentage too if desired:
                tvCardPercentageMain.setTextColor(getColorForPercentage(item.getPercentage()));
            }

            // Populate breakdown metrics and apply dynamic color coding
            if (item.getCompetenciesBreakdownAnalysisList() != null) {
                for (CompetenciesBreakdownAnalysis breakdown : item.getCompetenciesBreakdownAnalysisList()) {
                    if (breakdown.getName() == null) continue;
                    String name = breakdown.getName().toLowerCase();
                    double percentage = breakdown.getPercentage();
                    String formattedVal = String.format("%.0f%%", percentage);
                    int dynamicColor = getColorForPercentage(percentage);

                    if (name.contains("clarity")) {
                        if (tvValueClarity != null) tvValueClarity.setText(formattedVal);
                        if (indicatorClarity != null) indicatorClarity.setBackgroundColor(dynamicColor);
                    } else if (name.contains("structure")) {
                        if (tvValueStructure != null) tvValueStructure.setText(formattedVal);
                        if (indicatorStructure != null) indicatorStructure.setBackgroundColor(dynamicColor);
                    } else if (name.contains("vocabulary")) {
                        if (tvValueVocabulary != null) tvValueVocabulary.setText(formattedVal);
                        if (indicatorVocabulary != null) indicatorVocabulary.setBackgroundColor(dynamicColor);
                    }
                }
            }
        }

        /**
         * Returns Red for very low marks (< 40%),
         * Orange for mid-range (40% - 69%),
         * Green for higher marks (>= 70%).
         */
        private int getColorForPercentage(double percentage) {
            if (percentage < 40.0) {
                return Color.parseColor("#FFEF5350"); // Red
            } else if (percentage < 70.0) {
                return Color.parseColor("#FFF7A93B"); // Orange
            } else {
                return Color.parseColor("#FF08B425"); // Green
            }
        }
    }
}