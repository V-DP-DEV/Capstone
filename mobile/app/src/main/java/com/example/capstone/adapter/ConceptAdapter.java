package com.example.capstone.adapter;


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
        TextView tvConceptName, tvTimesTested, tvConceptPercentage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConceptName = itemView.findViewById(R.id.tv_skill_name);
            tvTimesTested = itemView.findViewById(R.id.tv_total_count);
            tvConceptPercentage = itemView.findViewById(R.id.tv_percentage);
        }

        void bind(ConceptAnalysis item) {
            if (tvConceptName != null) tvConceptName.setText(item.getName());
            if (tvTimesTested != null) tvTimesTested.setText(String.format("Tested: %d", item.getTimesTested()));
            if (tvConceptPercentage != null) tvConceptPercentage.setText(String.format("%.1f%%", item.getPercentage()));
        }
    }
}
