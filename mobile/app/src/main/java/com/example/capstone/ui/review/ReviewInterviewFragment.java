package com.example.capstone.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.capstone.R;
import com.example.capstone.adapter.ReviewInterviewAdapter;
import com.example.capstone.databinding.FragmentReviewInterviewBinding;
import com.example.capstone.domainModels.UserQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewInterviewFragment extends Fragment {
    public static final String ARG_INTERVIEW_TITLE = "arg_interview_title";
    public static final String ARG_DIFFICULTY = "arg_difficulty";
    public static final String ARG_TOTAL_QUESTIONS = "arg_total_questions";
    public static final String ARG_DURATION = "arg_duration";
    public static final String ARG_ANSWERED_COUNT = "arg_answered_count";

    private FragmentReviewInterviewBinding binding;
    private ReviewInterviewAdapter adapter;
    private List<UserQuestion> reviewQuestionsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewInterviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displaySessionDetails();
        setupRecyclerView();
        setupClickListeners();

    }

    private void displaySessionDetails() {
        if (getArguments() == null || binding == null) return;
        String title = getArguments().getString(ARG_INTERVIEW_TITLE);
        String difficulty = getArguments().getString(ARG_DIFFICULTY);
        int answered = getArguments().getInt(ARG_ANSWERED_COUNT);
        int total = getArguments().getInt(ARG_TOTAL_QUESTIONS);
String duration = getArguments().getString(ARG_DURATION);

        if (binding.hard != null) {
            binding.hard.setText(difficulty);
        }
        if (binding.tv10Questions != null) {
            binding.tv10Questions.setText(String.format(Locale.getDefault(), "%d questions", total));
        }
        if (binding.tvDuration != null) {
            binding.tvDuration.setText(duration);
        }
        if (binding.tvScorePercent != null) {
            binding.tvScorePercent.setText("80%");
        }
        if (binding.tvTotalCount != null) {
            binding.tvTotalCount.setText(String.format(Locale.getDefault(), "Total %d", answered));
        }
    }
    private void setupRecyclerView() {
        adapter = new ReviewInterviewAdapter(reviewQuestionsList);
        if (binding.rvQList != null) {
            binding.rvQList.setAdapter(adapter);
            binding.rvQList.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
    }

    private void setupClickListeners() {
        if (binding == null) return;
        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> navigateBack());
        }
        if (binding.divPillBtn != null) {
            binding.divPillBtn.setOnClickListener(v -> {
                if (isAdded()) {
                    NavHostFragment.findNavController(this)
                            .popBackStack(R.id.selectDifficultyFragment, false);
                }
            });
        }
    }
    private void navigateBack() {
        if (isAdded()) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
