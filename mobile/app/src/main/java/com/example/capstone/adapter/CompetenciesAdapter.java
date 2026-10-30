package com.example.capstone.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.domainModels.CompetenciesAnalysis;
import com.example.capstone.domainModels.CompetenciesBreakdownAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter responsible for displaying competency feedback cards and skill breakdown metrics.
 * Maps data from domain model {@link CompetenciesAnalysis} to card items within a RecyclerView dynamically
 * supporting any number of sub-metrics (3 or more) pulled from the backend.
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
        RecyclerView rvSubMetrics;
        CompetenciesBreakdownAdapter breakdownAdapter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardTitle = itemView.findViewById(R.id.card_title);
            tvCardStatus = itemView.findViewById(R.id.card_status);
            tvCardPercentageMain = itemView.findViewById(R.id.card_percentage_main);
            rvSubMetrics = itemView.findViewById(R.id.rv_sub_metrics);

            if (rvSubMetrics != null) {
                rvSubMetrics.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                breakdownAdapter = new CompetenciesBreakdownAdapter();
                rvSubMetrics.setAdapter(breakdownAdapter);
            }
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
            if (breakdownAdapter != null) {
                breakdownAdapter.setBreakdownList(breakdowns);
            }
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
