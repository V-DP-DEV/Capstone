package com.example.capstone.ui.mock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.databinding.FragmentMockInterviewResultBinding;

import java.util.Locale;

public class MockInterviewResult extends Fragment {

    public static final String ARG_INTERVIEW_TITLE = "arg_interview_title";
    public static final String ARG_DIFFICULTY = "arg_difficulty";
    public static final String ARG_ANSWERED_COUNT = "arg_answered_count";
    public static final String ARG_TOTAL_QUESTIONS = "arg_total_questions";
    public static final String ARG_DURATION = "arg_duration";

    private FragmentMockInterviewResultBinding binding;

    public static MockInterviewResult newInstance(
            String title, String difficulty, int answered, int total, String duration) {

        MockInterviewResult fragment = new MockInterviewResult();
        Bundle args = new Bundle();
        args.putString(ARG_INTERVIEW_TITLE, title);
        args.putString(ARG_DIFFICULTY, difficulty);
        args.putInt(ARG_ANSWERED_COUNT, answered);
        args.putInt(ARG_TOTAL_QUESTIONS, total);
        args.putString(ARG_DURATION, duration);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMockInterviewResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displaySessionDetails();
        setupClickListeners();
    }

    private void displaySessionDetails() {
        if (getArguments() == null || binding == null) return;

        String title = getArguments().getString(ARG_INTERVIEW_TITLE, "Mock Interview");
        String difficulty = getArguments().getString(ARG_DIFFICULTY, "Medium");
        int answered = getArguments().getInt(ARG_ANSWERED_COUNT, 0);
        int total = getArguments().getInt(ARG_TOTAL_QUESTIONS, 0);
        String duration = getArguments().getString(ARG_DURATION, "0m");

        if (binding.tvHeaderTitle != null) {
            binding.tvHeaderTitle.setText("Interview Complete");
        }
        if (binding.tvInterviewMeta != null) {
            binding.tvInterviewMeta.setText(String.format(Locale.getDefault(), "%s · %s", title, difficulty));
        }
        if (binding.tvAnsweredVal != null) {
            binding.tvAnsweredVal.setText(
                    String.format(Locale.getDefault(), "%d/%d", answered, total)
            );
        }
        if (binding.tvDurationVal != null) {
            binding.tvDurationVal.setText(duration);
        }
    }

    private void setupClickListeners() {
        if (binding == null) return;

        if (binding.btnViewReview != null) {
            binding.btnViewReview.setOnClickListener(v -> navigateBack());
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
