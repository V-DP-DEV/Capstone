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
import com.example.capstone.domainModels.CompetenciesBreakdownAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter responsible for displaying individual sub-metric breakdown items
 *  mapped from {@link CompetenciesBreakdownAnalysis}.
 */
public class CompetenciesBreakdownAdapter extends RecyclerView.Adapter<CompetenciesBreakdownAdapter.ViewHolder> {

    private List<CompetenciesBreakdownAnalysis> breakdownList = new ArrayList<>();

    public void setBreakdownList(List<CompetenciesBreakdownAnalysis> list) {
        this.breakdownList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_metric_breakdown, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompetenciesBreakdownAnalysis item = breakdownList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return breakdownList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubMetricName, tvSubMetricPercentage;
        View indicatorSubMetric;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubMetricName = itemView.findViewById(R.id.tvSubMetricName);
            tvSubMetricPercentage = itemView.findViewById(R.id.tvSubMetricPercentage);
            indicatorSubMetric = itemView.findViewById(R.id.indicatorSubMetric);
        }

        void bind(CompetenciesBreakdownAnalysis item) {
            if (tvSubMetricName != null) tvSubMetricName.setText(item.getName());
            double raw = item.getPercentage();
            double pct = raw <= 1.0 ? raw * 100.0 : raw;
            if (tvSubMetricPercentage != null) {
                tvSubMetricPercentage.setText(String.format("%.0f%%", pct));
                tvSubMetricPercentage.setTextColor(getColorForPercentage(pct));
            }
            if (indicatorSubMetric != null) {
                int color = getColorForPercentage(pct);
                indicatorSubMetric.setBackgroundTintList(ColorStateList.valueOf(color));
                indicatorSubMetric.setPivotX(0f);
                float scale = (float) Math.max(0.0, Math.min(1.0, pct / 100.0));
                indicatorSubMetric.setScaleX(scale);
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
