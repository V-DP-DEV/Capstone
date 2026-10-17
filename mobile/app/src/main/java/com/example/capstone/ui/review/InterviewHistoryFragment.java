package com.example.capstone.ui.review;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.adapter.InterviewHistoryAdapter;
import com.example.capstone.viewmodel.HistoryViewModel;

import java.util.ArrayList;

public class InterviewHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private InterviewHistoryAdapter adapter;
    private HistoryViewModel historyViewModel;

    private TextView tabAll, tabHighest, tabLowest;
    private ImageButton btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interview_history, container, false);

        // Scope HistoryViewModel to requireActivity() so it is shared with the Review screen
        historyViewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);

        recyclerView = view.findViewById(R.id.rv_interview_history);
        tabAll = view.findViewById(R.id.tab_all);
        tabHighest = view.findViewById(R.id.tab_highest);
        tabLowest = view.findViewById(R.id.tab_lowest);
        btnBack = view.findViewById(R.id.btnBack);

        setupRecyclerView();
        setupTabs();
        setupBackNavigation();
        observeViewModel();

        historyViewModel.loadHistory();

        return view;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new InterviewHistoryAdapter(new ArrayList<>(), attempt -> {
            historyViewModel.setSelectedAttempt(attempt);
            Navigation.findNavController(getView()).navigate(R.id.action_history_to_review);
        });

        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        historyViewModel.getHistoryList().observe(getViewLifecycleOwner(), attempts -> {
            if (attempts != null) {
                adapter.updateData(attempts);
            }
        });
    }

    private void setupTabs() {
        tabAll.setOnClickListener(v -> {
            highlightTab(tabAll);
            historyViewModel.setFilter(HistoryViewModel.FilterType.ALL);
        });

        tabHighest.setOnClickListener(v -> {
            highlightTab(tabHighest);
            historyViewModel.setFilter(HistoryViewModel.FilterType.HIGHEST);
        });

        tabLowest.setOnClickListener(v -> {
            highlightTab(tabLowest);
            historyViewModel.setFilter(HistoryViewModel.FilterType.LOWEST);
        });
    }

    private void setupBackNavigation() {
        btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }

    private void highlightTab(TextView activeTab) {
        tabAll.setBackgroundColor(Color.TRANSPARENT);
        tabAll.setTextColor(0xFF8A8A97);
        tabHighest.setBackgroundColor(Color.TRANSPARENT);
        tabHighest.setTextColor(0xFF8A8A97);
        tabLowest.setBackgroundColor(Color.TRANSPARENT);
        tabLowest.setTextColor(0xFF8A8A97);

        activeTab.setBackgroundResource(R.drawable.bg_rounded);
        activeTab.setBackgroundTintList(getResources().getColorStateList(R.color.color_purple_primary, null));
        activeTab.setTextColor(0xFFFFFFFF);
    }
}