package com.example.capstone.ui.mock;

import static com.example.capstone.ui.mock.MockInterviewResult.ARG_TOTAL_QUESTIONS;
import static com.example.capstone.ui.mock.SelectInterviewFragment.ARG_DIFFICULTY;

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
import com.example.capstone.ui.components.BottomNavHelper;

/**
 * MockInterviewInstructionsFragment displays the pre-interview briefing screen,
 * showing details like interview title, difficulty, question count, duration,
 * and input type before the user officially starts the session.
 */
public class MockInterviewInstructionsFragment extends Fragment {

    // Bundle argument keys for receiving interview metadata from the selection screen
    public static final String ARG_INTERVIEW_TITLE = "arg_interview_title";
    public static final String ARG_INTERVIEW_NAME = "arg_interview_name";
    public static final String ARG_INTERVIEW_DIFFICULTY = "arg_interview_difficulty";
    public static final String ARG_QUESTION_COUNT = "arg_question_count";
    public static final String ARG_DURATION = "arg_duration";
    public static final String ARG_INPUT_TYPE = "arg_input_type";

    private FragmentMockInterviewInstructionsBinding binding;

    /**
     * Factory method to create a new instance of this fragment packed with the required interview details.
     */
    public static MockInterviewInstructionsFragment newInstance(
            String title, String difficulty, int questionCount, String duration, String inputType) {
        MockInterviewInstructionsFragment fragment = new MockInterviewInstructionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INTERVIEW_TITLE, title);
        args.putString(ARG_DIFFICULTY, difficulty);
        args.putInt(ARG_TOTAL_QUESTIONS, questionCount);
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

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        BottomNavHelper.setupNavListeners(this, bottomBar);
    }

    /**
     * Extracts bundle arguments and binds the interview metadata to the corresponding instruction TextViews.
     */
    private void displayInstructionsDetails() {
        if (getArguments() == null || binding == null) return;

        // Use aligned keys
        String title = getArguments().getString(ARG_INTERVIEW_TITLE, "Interview");
        String difficulty = getArguments().getString(ARG_DIFFICULTY, "Medium");
        int questionCount = getArguments().getInt(ARG_TOTAL_QUESTIONS, 8);
        String duration = getArguments().getString(ARG_DURATION, "~25m");
        String inputType = getArguments().getString(ARG_INPUT_TYPE, "Mic");

        if (binding.tvInterviewName != null) {
            binding.tvInterviewName.setText(title);
        }

        if (binding.tvInterviewDifficulty != null) {
            binding.tvInterviewDifficulty.setText(difficulty);
        }

        if (binding.tvStatQuestionsVal != null) {
            binding.tvStatQuestionsVal.setText(String.valueOf(questionCount));
        }

        if (binding.tvStatDurationVal != null) {
            binding.tvStatDurationVal.setText(duration);
        }

        if (binding.tvStatInputVal != null) {
            binding.tvStatInputVal.setText(inputType);
        }
    }

    /**
     * Sets up click listeners for back navigation and starting the interview session.
     */
    private void setupClickListeners() {
        if (binding == null) return;
        binding.btnBack.setOnClickListener(v -> navigateBack());
        binding.btnStart.setOnClickListener(v -> startInterviewSession());
    }

    /**
     * Forwards the current argument bundle forward to the MockInterviewQuestionsFragment via Jetpack Navigation.
     */
    private void startInterviewSession() {
        if (isAdded()) {
            Bundle args = getArguments() != null ? new Bundle(getArguments()) : new Bundle();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_mockInterviewInstructionsFragment_to_mockInterviewQuestionsFragment, args);
        }
    }

    /**
     * Navigates back to the previous screen.
     */
    private void navigateBack() {
        if (isAdded()) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear binding reference to prevent memory leaks
        binding = null;
    }
}