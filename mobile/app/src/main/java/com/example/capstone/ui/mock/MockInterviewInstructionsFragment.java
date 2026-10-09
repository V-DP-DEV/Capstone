package com.example.capstone.ui.mock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentMockInterviewInstructionsBinding;

public class MockInterviewInstructionsFragment extends Fragment {

    public static final String ARG_INTERVIEW_NAME = "arg_interview_name";
    public static final String ARG_INTERVIEW_DIFFICULTY = "arg_interview_difficulty";
    public static final String ARG_QUESTION_COUNT = "arg_question_count";
    public static final String ARG_DURATION = "arg_duration";
    public static final String ARG_INPUT_TYPE = "arg_input_type";

    private FragmentMockInterviewInstructionsBinding binding;

    public static MockInterviewInstructionsFragment newInstance(
            String name, String difficulty, int questionCount, String duration, String inputType) {
        MockInterviewInstructionsFragment fragment = new MockInterviewInstructionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INTERVIEW_NAME, name);
        args.putString(ARG_INTERVIEW_DIFFICULTY, difficulty);
        args.putInt(ARG_QUESTION_COUNT, questionCount);
        args.putString(ARG_DURATION, duration);
        args.putString(ARG_INPUT_TYPE, inputType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMockInterviewInstructionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayInstructionsDetails();
        setupClickListeners();
    }

    private void displayInstructionsDetails() {
        if (getArguments() == null || binding == null) return;

        String name = getArguments().getString(ARG_INTERVIEW_NAME, "Interview");
        String difficulty = getArguments().getString(ARG_INTERVIEW_DIFFICULTY, "Medium");
        int questionCount = getArguments().getInt(ARG_QUESTION_COUNT, 8);
        String duration = getArguments().getString(ARG_DURATION, "~25m");
        String inputType = getArguments().getString(ARG_INPUT_TYPE, "Mic");

        if (binding.tvInterviewName != null) {
            binding.tvInterviewName.setText(name);
        }

        if (binding.tvInterviewDifficulty != null) {
            binding.tvInterviewDifficulty.setText(difficulty);
        }

        if (binding.tvStatQuestionsVal != null) {
            binding.tvStatQuestionsVal.setText(String.valueOf(questionCount));
        }

        if (binding.tvStatDurationVal.length() > 0 || binding.tvStatDurationVal != null) {
            binding.tvStatDurationVal.setText(duration);
        }

        if (binding.tvStatInputVal != null) {
            binding.tvStatInputVal.setText(inputType);
        }
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        binding.btnStart.setOnClickListener(v -> startInterviewSession());
    }

    private void startInterviewSession() {
        if (isAdded()) {
            Bundle args = getArguments() != null ? new Bundle(getArguments()) : new Bundle();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_mockInterviewInstructionsFragment_to_mockInterviewQuestionsFragment, args);
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