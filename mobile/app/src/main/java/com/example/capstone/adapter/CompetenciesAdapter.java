package com.example.capstone.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.domainModels.CompetenciesAnalysis;
import com.example.capstone.domainModels.CompetenciesBreakdownAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter responsible for displaying competency feedback cards and skill breakdown metrics.
 * Maps data from domain model {@link CompetenciesAnalysis} to card items within a RecyclerView.
 */

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


        TextView tvLabelClarity, tvValueClarity;
        View indicatorClarity;


        TextView tvLabelStructure, tvValueStructure;
        View indicatorStructure;


        TextView tvLabelVocabulary, tvValueVocabulary;
        View indicatorVocabulary;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardTitle = itemView.findViewById(R.id.card_title);
            tvCardStatus = itemView.findViewById(R.id.card_status);
            tvCardPercentageMain = itemView.findViewById(R.id.card_percentage_main);


            tvLabelClarity = itemView.findViewById(R.id.label_clarity);
            tvValueClarity = itemView.findViewById(R.id.value_clarity);
            indicatorClarity = itemView.findViewById(R.id.indicator_clarity);


            tvLabelStructure = itemView.findViewById(R.id.label_structure);
            tvValueStructure = itemView.findViewById(R.id.value_structure);
            indicatorStructure = itemView.findViewById(R.id.indicator_structure);


            tvLabelVocabulary = itemView.findViewById(R.id.label_vocabulary);
            tvValueVocabulary = itemView.findViewById(R.id.value_vocabulary);
            indicatorVocabulary = itemView.findViewById(R.id.indicator_vocabulary);
        }

        private double getNormalizedPercentage(double rawValue) {

            return rawValue <= 1.0 ? rawValue * 100.0 : rawValue;
        }


        void bind(CompetenciesAnalysis item) {
            if (tvCardTitle != null) {
                tvCardTitle.setText(item.getName());
            }


            double mainPercentage = getNormalizedPercentage(item.getPercentage());

            if (tvCardStatus != null) {
                tvCardStatus.setText(mainPercentage >= 70 ? "Strong" : (mainPercentage >= 40 ? "Moderate" : "Needs Improvement"));
            }
            if (tvCardPercentageMain != null) {
                tvCardPercentageMain.setText(String.format("%.0f%%", mainPercentage));
                tvCardPercentageMain.setTextColor(getColorForPercentage(mainPercentage));
            }

            List<CompetenciesBreakdownAnalysis> breakdowns = item.getCompetenciesBreakdownAnalysisList();
            if (breakdowns != null) {
                if (breakdowns.size() > 0) {
                    CompetenciesBreakdownAnalysis b0 = breakdowns.get(0);
                    double p0 = getNormalizedPercentage(b0.getPercentage());
                    if (tvLabelClarity != null && b0.getName() != null)
                        tvLabelClarity.setText(b0.getName());
                    if (tvValueClarity != null) tvValueClarity.setText(String.format("%.0f%%", p0));
                    applyDynamicIndicator(indicatorClarity, p0);
                }

                if (breakdowns.size() > 1) {
                    CompetenciesBreakdownAnalysis b1 = breakdowns.get(1);
                    double p1 = getNormalizedPercentage(b1.getPercentage());
                    if (tvLabelStructure != null && b1.getName() != null)
                        tvLabelStructure.setText(b1.getName());
                    if (tvValueStructure != null)
                        tvValueStructure.setText(String.format("%.0f%%", p1));
                    applyDynamicIndicator(indicatorStructure, p1);
                }

                if (breakdowns.size() > 2) {
                    CompetenciesBreakdownAnalysis b2 = breakdowns.get(2);
                    double p2 = getNormalizedPercentage(b2.getPercentage());
                    if (tvLabelVocabulary != null && b2.getName() != null)
                        tvLabelVocabulary.setText(b2.getName());
                    if (tvValueVocabulary != null)
                        tvValueVocabulary.setText(String.format("%.0f%%", p2));
                    applyDynamicIndicator(indicatorVocabulary, p2);
                }
            }
        }

        private void applyDynamicIndicator(View indicator, double percentage) {
            if (indicator == null) return;


            int dynamicColor = getColorForPercentage(percentage);
            indicator.setBackgroundTintList(ColorStateList.valueOf(dynamicColor));


            indicator.setPivotX(0f);
            float scale = (float) Math.max(0.0, Math.min(1.0, percentage / 100.0));
            indicator.setScaleX(scale);
        }


        private int getColorForPercentage(double percentage) {
            if (percentage < 40.0) {
                return Color.parseColor("#FFEF5350");
            } else if (percentage < 70.0) {
                return Color.parseColor("#FFF7A93B");
            } else {
                return Color.parseColor("#FF08B425");
            }
        }
    }
}