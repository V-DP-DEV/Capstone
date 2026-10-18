package com.example.capstone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.capstone.R;
import com.example.capstone.domainModels.CompetenciesAnalysis;
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
        TextView tvSkillName, tvSkillPercentage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkillName = itemView.findViewById(R.id.card_title);
            tvSkillPercentage = itemView.findViewById(R.id.card_percentage_main);
        }

        void bind(CompetenciesAnalysis item) {
            if (tvSkillName != null) tvSkillName.setText(item.getName());
            if (tvSkillPercentage != null) tvSkillPercentage.setText(String.format("%.1f%%", item.getPercentage()));
        }
    }
}
