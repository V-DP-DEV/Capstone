package com.example.capstone.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.capstone.R;
import com.example.capstone.adapter.ReviewInterviewAdapter;
import com.example.capstone.databinding.FragmentReviewInterviewBinding;
import com.example.capstone.domainModels.UserAttemptCompetencyFeedback;
import com.example.capstone.domainModels.UserQuestion;
import com.example.capstone.ui.components.BottomNavHelper;

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
        setupDropdownListeners();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        BottomNavHelper.setupNavListeners(this, bottomBar);
    }

    private boolean isStrengthsExpanded = false;
    private boolean isAreasExpanded = true;

    private void setupDropdownListeners() {
        if (binding == null) return;

        // Strengths Toggle
        if (binding.sectionStrengths != null) {
            binding.sectionStrengths.setOnClickListener(v -> {
                isStrengthsExpanded = !isStrengthsExpanded;
                if (binding.layoutStrengthsContent != null) {
                    binding.layoutStrengthsContent.setVisibility(isStrengthsExpanded ? View.VISIBLE : View.GONE);
                }
                if (binding.tvStrengthsPlus != null) {
                    binding.tvStrengthsPlus.setText(isStrengthsExpanded ? "-" : "+");
                }
            });
        }

        // Areas to Improve Toggle
        if (binding.sectionAreasImprove != null) {
            binding.sectionAreasImprove.setOnClickListener(v -> {
                isAreasExpanded = !isAreasExpanded;
                if (binding.layoutAreasContent != null) {
                    binding.layoutAreasContent.setVisibility(isAreasExpanded ? View.VISIBLE : View.GONE);
                }
                if (binding.tvAreasPlus != null) {
                    binding.tvAreasPlus.setText(isAreasExpanded ? "-" : "+");
                }
            });
        }
    }

    private void populateCompetencyFeedback(UserAttemptCompetencyFeedback feedbackData) {
        if (feedbackData == null || binding == null) return;

        // Set overall score percentage using model getter
        if (binding.tvScorePercent != null) {
            binding.tvScorePercent.setText(String.format(Locale.getDefault(), "%.0f%%", feedbackData.getCompetencyPercentage()));
        }
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
    }

    private void setupRecyclerView() {
        // Pass click listener to handle navigation when a question item is clicked
        adapter = new ReviewInterviewAdapter(reviewQuestionsList, (question, position) -> {
            if (isAdded()) {
                NavController navController = NavHostFragment.findNavController(this);
                if (navController.getCurrentDestination() != null
                        && navController.getCurrentDestination().getId() == R.id.reviewInterviewFragment) {
                    Bundle args = new Bundle();

                    // Pass along session metadata arguments
                    if (getArguments() != null) {
                        args.putString(ReviewQuestionFragment.ARG_INTERVIEW_TITLE, getArguments().getString(ARG_INTERVIEW_TITLE));
                        args.putString(ReviewQuestionFragment.ARG_DIFFICULTY, getArguments().getString(ARG_DIFFICULTY));
                        args.putString(ReviewQuestionFragment.ARG_DURATION, getArguments().getString(ARG_DURATION));
                    }

                    // Navigate to ReviewQuestionFragment using action defined in nav graph
                    navController.navigate(R.id.action_reviewInterviewFragment_to_reviewQuestionFragment, args);
                }
            }
        });

        if (binding.rvQList != null) {
            binding.rvQList.setAdapter(adapter);
            binding.rvQList.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
    }

    private void setupClickListeners() {
        if (binding == null) return;

        // Top back arrow button
        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> navigateBack());
        }

        // The "Review questions" pill button (div_pill_btn) should navigate to ReviewQuestionFragment
        if (binding.divPillBtn != null) {
            binding.divPillBtn.setOnClickListener(v -> {
                if (isAdded()) {
                    NavController navController = NavHostFragment.findNavController(this);
                    if (navController.getCurrentDestination() != null
                            && navController.getCurrentDestination().getId() == R.id.reviewInterviewFragment) {
                        Bundle args = new Bundle();

                        // Pass along session metadata arguments
                        if (getArguments() != null) {
                            args.putString(ReviewQuestionFragment.ARG_INTERVIEW_TITLE, getArguments().getString(ARG_INTERVIEW_TITLE));
                            args.putString(ReviewQuestionFragment.ARG_DIFFICULTY, getArguments().getString(ARG_DIFFICULTY));
                            args.putString(ReviewQuestionFragment.ARG_DURATION, getArguments().getString(ARG_DURATION));
                        }

                        // Navigate forward to the question review screen
                        navController.navigate(R.id.action_reviewInterviewFragment_to_reviewQuestionFragment, args);
                    }
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