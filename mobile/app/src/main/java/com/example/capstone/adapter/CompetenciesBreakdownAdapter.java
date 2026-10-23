package com.example.capstone.adapter;

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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubMetricName = itemView.findViewById(R.id.tvSubMetricName);
            tvSubMetricPercentage = itemView.findViewById(R.id.tvSubMetricPercentage);
        }

        void bind(CompetenciesBreakdownAnalysis item) {
            if (tvSubMetricName != null) tvSubMetricName.setText(item.getName());
            if (tvSubMetricPercentage != null) tvSubMetricPercentage.setText(String.format("%.1f%%", item.getPercentage()));
        }
    }
}