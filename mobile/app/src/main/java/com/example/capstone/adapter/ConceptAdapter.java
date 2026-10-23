package com.example.capstone.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.domainModels.ConceptAnalysis;

import java.util.ArrayList;
import java.util.List;

public class ConceptAdapter extends RecyclerView.Adapter<ConceptAdapter.ViewHolder> {

    private List<ConceptAnalysis> conceptList = new ArrayList<>();

    public void setConceptList(List<ConceptAnalysis> list) {
        this.conceptList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill_summary_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConceptAnalysis item = conceptList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return conceptList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkillName, tvTotalCount, tvPercentage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkillName = itemView.findViewById(R.id.tv_skill_name);
            tvTotalCount = itemView.findViewById(R.id.tv_total_count);
            tvPercentage = itemView.findViewById(R.id.tv_percentage);
        }

        void bind(ConceptAnalysis item) {
            if (tvSkillName != null) {
                tvSkillName.setText(item.getName());
            }
            if (tvTotalCount != null) {
                tvTotalCount.setText(String.format("Total %d", item.getTimesTested()));
            }
            if (tvPercentage != null) {
                double percentage = item.getPercentage()*100;
                tvPercentage.setText(String.format("%.0f%%", percentage));
                // Dynamically color-code the percentage text based on score
                tvPercentage.setTextColor(getColorForPercentage(percentage));
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