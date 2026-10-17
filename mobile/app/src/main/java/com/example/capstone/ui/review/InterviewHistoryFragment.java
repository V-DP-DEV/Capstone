package com.example.capstone.ui.review;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.adapter.InterviewHistoryAdapter;
import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.viewmodel.HistoryViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

import java.util.ArrayList;

/**
 * Fragment responsible for displaying the user's interview history, tab-based filtering,
 * and navigating to detailed review screens or initiating attempt deletion.
 */
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

        // Clean Architecture: Fragment is completely decoupled from network/data layers.
        ViewModelFactory<HistoryViewModel> factory = new ViewModelFactory<>(() ->
                HistoryViewModel.create(requireContext())
        );

        historyViewModel = new ViewModelProvider(this, factory).get(HistoryViewModel.class);

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
        adapter = new InterviewHistoryAdapter(new ArrayList<>(), new InterviewHistoryAdapter.OnItemClickListener() {
            @Override
            public void onInterviewClick(UserInterviewAttemptSummary summary) {
                Bundle args = new Bundle();
                args.putLong("attempt_id", summary.getId());
                Navigation.findNavController(requireView()).navigate(R.id.action_history_to_review, args);
            }

            @Override
            public void onDeleteClick(UserInterviewAttemptSummary summary, int position) {
                historyViewModel.deleteAttempt((int) summary.getId());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        historyViewModel.getHistoryList().observe(getViewLifecycleOwner(), attempts -> {
            if (attempts != null) {
                adapter.updateData(attempts);
            }
        });

        historyViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        historyViewModel.getDeleteSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(getContext(), "Interview attempt deleted", Toast.LENGTH_SHORT).show();
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