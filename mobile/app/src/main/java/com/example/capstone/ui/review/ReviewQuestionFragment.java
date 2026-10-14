package com.example.capstone.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentReviewQuestionBinding;
import com.example.capstone.domainModels.UserConceptScores;
import com.example.capstone.domainModels.UserQuestionAttempt;
import com.example.capstone.ui.components.BottomNavHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewQuestionFragment extends Fragment {

    public static final String ARG_INTERVIEW_TITLE = "arg_interview_title";
    public static final String ARG_DIFFICULTY = "arg_difficulty";
    public static final String ARG_DURATION = "arg_duration";

    private FragmentReviewQuestionBinding binding;
    private List<UserQuestionAttempt> reviewQuestionsList = new ArrayList<>();
    private int currentReviewIndex = 0;

    private boolean isConceptsExpanded = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewQuestionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        extractSessionMetadata();
        loadReviewData();
        setupClickListeners();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        BottomNavHelper.setupNavListeners(this, bottomBar);

        if (!reviewQuestionsList.isEmpty()) {
            displayQuestionReview(currentReviewIndex);
        }
    }

    private void extractSessionMetadata() {
        if (getArguments() == null || binding == null) return;

        String title = getArguments().getString(ARG_INTERVIEW_TITLE, "C# Developer");
        String difficulty = getArguments().getString(ARG_DIFFICULTY, "Hard");
        String duration = getArguments().getString(ARG_DURATION, "01:24 / 20 minutes");

        if (binding.tvRoleTitle != null) {
            binding.tvRoleTitle.setText(title);
        }
        if (binding.tvDifficulty != null) {
            binding.tvDifficulty.setText(difficulty);
        }
        if (binding.tvTimer != null) {
            binding.tvTimer.setText(duration);
        }
    }

    private void loadReviewData() {
        reviewQuestionsList = new ArrayList<>();

        // Create mock concept scores using UserConceptScores
        List<UserConceptScores> mockScores = new ArrayList<>();

        UserConceptScores score1 = new UserConceptScores();
        score1.setName("Object-Oriented Principles");
        score1.setScore(4.0);
        score1.setMaxScore(5);
        mockScores.add(score1);

        UserConceptScores score2 = new UserConceptScores();
        score2.setName("Exception Handling");
        score2.setScore(3.0);
        score2.setMaxScore(5);
        mockScores.add(score2);

        // Create a mock UserQuestionAttempt
        UserQuestionAttempt mockAttempt = new UserQuestionAttempt();
        mockAttempt.setType("Technical");
        mockAttempt.setQuestion("Explain how polymorphism works in Java and give a practical example.");
        mockAttempt.setAnswer("Polymorphism allows objects to be treated as instances of their parent class...");
        mockAttempt.setExpectedAnswer("Polymorphism is categorized into compile-time and runtime...");
        mockAttempt.setConceptScores(mockScores);

        reviewQuestionsList.add(mockAttempt);
    }

    public void setReviewQuestions(List<UserQuestionAttempt> questions) {
        this.reviewQuestionsList = questions != null ? questions : new ArrayList<>();
        this.currentReviewIndex = 0;
        if (binding != null && !reviewQuestionsList.isEmpty()) {
            displayQuestionReview(currentReviewIndex);
        }
    }

    private void setupClickListeners() {
        if (binding == null) return;

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> navigateBack());
        }

        if (binding.divPillBtn != null) {
            binding.divPillBtn.setOnClickListener(v -> navigateBackToDashboard());
        }
    }

    private void displayQuestionReview(int index) {
        if (index < 0 || index >= reviewQuestionsList.size()) return;

        UserQuestionAttempt q = reviewQuestionsList.get(index);
        int total = reviewQuestionsList.size();

        binding.tvQuestionCounter.setText(String.format(Locale.getDefault(), "Question %d / %d", index + 1, total));

        String category = q.getType() != null ? q.getType().toUpperCase(Locale.getDefault()) : "INTRODUCTION";
        binding.tvCategoryTitle.setText(category);
        binding.tvQuestionText.setText(q.getQuestion() != null ? q.getQuestion() : "No question text available.");

        // Bind actual user and expected answers from the Attempt model
        binding.tvUserAnswerText.setText(q.getAnswer() != null ? q.getAnswer() : "No answer provided.");
        binding.tvExpectedAnswerText.setText(q.getExpectedAnswer() != null ? q.getExpectedAnswer() : "No expected answer available.");

        // Populate concept scores if present
        populateConceptScores(q.getConceptScores());
    }

    private void navigateBack() {
        if (isAdded()) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    private void navigateBackToDashboard() {
        if (isAdded()) {
            try {
                NavController navController = NavHostFragment.findNavController(this);
                if (navController.getCurrentDestination() != null
                        && navController.getCurrentDestination().getId() == R.id.reviewQuestionFragment) {
                    navController.navigate(R.id.action_reviewQuestionFragment_to_userDashboardFragment);
                }
            } catch (Exception e) {
                try {
                    NavHostFragment.findNavController(this).popBackStack();
                } catch (Exception ex) {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                }
            }
        }
    }

    private void populateConceptScores(List<UserConceptScores> conceptScores) {
        if (binding == null || binding.layoutConceptsContent == null) return;

        binding.layoutConceptsContent.removeAllViews();

        if (conceptScores == null || conceptScores.isEmpty()) {
            return;
        }

        for (UserConceptScores concept : conceptScores) {
            TextView tvConcept = new TextView(requireContext());
            tvConcept.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            tvConcept.setPadding(0, 8, 0, 8);
            tvConcept.setTextColor(getResources().getColor(android.R.color.black, null));
            tvConcept.setTextSize(14f);

            // Fixed double score formatting (%.1f) vs int maxScore (%d)
            String conceptText = String.format(Locale.getDefault(), "• %s: %.1f / %d",
                    concept.getName(),
                    concept.getScore(),
                    concept.getMaxScore()
            );

            tvConcept.setText(conceptText);
            binding.layoutConceptsContent.addView(tvConcept);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}