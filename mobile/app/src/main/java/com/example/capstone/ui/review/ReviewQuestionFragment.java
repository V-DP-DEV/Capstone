package com.example.capstone.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentReviewQuestionBinding;
import com.example.capstone.domainModels.UserConceptScores;
import com.example.capstone.domainModels.UserQuestion;
import com.example.capstone.ui.components.BottomNavHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * ReviewQuestionFragment displays a detailed breakdown of each answered mock interview question,
 * showing the category, question text, user's answer, expected answer, and AI concept evaluations.
 * Supports pagination through the question list using stepper controls.
 */
public class ReviewQuestionFragment extends Fragment {

    // Bundle argument keys for session metadata
    public static final String ARG_INTERVIEW_TITLE = "arg_interview_title";
    public static final String ARG_DIFFICULTY = "arg_difficulty";
    public static final String ARG_DURATION = "arg_duration";

    private FragmentReviewQuestionBinding binding;
    private List<UserQuestion> reviewQuestionsList = new ArrayList<>();
    private int currentReviewIndex = 0;

    private boolean isConceptsExpanded = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout using ViewBinding
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

    /**
     * Extracts session metadata (title, difficulty, duration) passed through navigation bundles.
     */
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

    /**
     * Loads review questions. Can be populated via ViewModel/Repository by Person 2 or fallback sample data.
     */
    private void loadReviewData() {
        reviewQuestionsList = new ArrayList<>();

        // Create mock concept scores using UserConceptScores
        List<UserConceptScores> mockScores = new ArrayList<>();

        UserConceptScores score1 = new UserConceptScores();
        score1.setName("Object-Oriented Principles");
        score1.setScore(4);
        score1.setMaxScore(5);
        mockScores.add(score1);

        UserConceptScores score2 = new UserConceptScores();
        score2.setName("Exception Handling");
        score2.setScore(3);
        score2.setMaxScore(5);
        mockScores.add(score2);

        // Create a mock UserQuestion and attach the concept scores
        UserQuestion mockQuestion = new UserQuestion();
        mockQuestion.setType("Technical");
        mockQuestion.setText("Explain how polymorphism works in Java and give a practical example.");
        // mockQuestion.setUserAnswer("Polymorphism allows objects to be treated as instances of their parent class...");
        // mockQuestion.setExpectedAnswer("Polymorphism is categorized into compile-time and runtime...");
        // mockQuestion.setConceptScores(mockScores); // Ensure your UserQuestion model supports this list

        reviewQuestionsList.add(mockQuestion);
    }

    /**
     * Public injection hook for Person 2 / ViewModel to pass evaluated question models into this review screen.
     */
    public void setReviewQuestions(List<UserQuestion> questions) {
        this.reviewQuestionsList = questions != null ? questions : new ArrayList<>();
        this.currentReviewIndex = 0;
        if (binding != null && !reviewQuestionsList.isEmpty()) {
            displayQuestionReview(currentReviewIndex);
        }
    }

    /**
     * Binds click listeners for back navigation, stepper pagination, concept card toggles, and finishing the review.
     */
    private void setupClickListeners() {
        if (binding == null) return;

        // Top back arrow button
        binding.btnBack.setOnClickListener(v -> navigateBack());

        // Stepper: Previous Question
        binding.btnPrevQuestion.setOnClickListener(v -> {
            if (currentReviewIndex > 0) {
                currentReviewIndex--;
                displayQuestionReview(currentReviewIndex);
            }
        });

        // Stepper: Next Question
        binding.btnNextQuestion.setOnClickListener(v -> {
            if (currentReviewIndex < reviewQuestionsList.size() - 1) {
                currentReviewIndex++;
                displayQuestionReview(currentReviewIndex);
            }
        });

        // Collapsible AI Concepts / Evaluation Card Toggle
        binding.cardConcepts.setOnClickListener(v -> toggleConceptsDropdown());

        // Finish Review bottom pill button
        binding.divPillBtn.setOnClickListener(v -> navigateBackToDashboard());
    }

    /**
     * Renders the question review details at the specified index.
     */
    private void displayQuestionReview(int index) {
        if (index < 0 || index >= reviewQuestionsList.size()) return;

        UserQuestion q = reviewQuestionsList.get(index);
        int total = reviewQuestionsList.size();

        // Update Counter Display (< Question X / Y >)
        binding.tvQuestionCounter.setText(String.format(Locale.getDefault(), "Question %d / %d", index + 1, total));

        // Bind Category & Question Text
        String category = q.getType() != null ? q.getType().toUpperCase(Locale.getDefault()) : "INTRODUCTION";
        binding.tvCategoryTitle.setText(category);
        binding.tvQuestionText.setText(q.getText() != null ? q.getText() : "Tell me a bit about your background and what drew you to backend development.");

        // Bind User Answer & Expected Answer (stubbed or pulled from evaluated model)
        binding.tvUserAnswerText.setText("Adsadsadasdasdsa dsadsa dsadsa dsadas");
        binding.tvExpectedAnswerText.setText("dsadasdadsa");
    }

    /**
     * Toggles the visibility of the AI concepts evaluation breakdown list and updates the '+' / '-' indicator.
     */
    private void toggleConceptsDropdown() {
        isConceptsExpanded = !isConceptsExpanded;
        binding.layoutConceptsContent.setVisibility(isConceptsExpanded ? View.VISIBLE : View.GONE);
        binding.tvConceptsToggle.setText(isConceptsExpanded ? "-" : "+");
    }

    /**
     * Navigates back to the previous screen in the navigation stack.
     */
    private void navigateBack() {
        if (isAdded()) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    /**
     * Returns the user back to difficulty selection or dashboard upon finishing the review.
     */
    private void navigateBackToDashboard() {
        if (isAdded()) {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_reviewQuestionFragment_to_selectDifficultyFragment);
        }
    }

    /**
     * Dynamically populates concept scores inside layoutConceptsContent using the existing UserConceptScores model.
     */
    private void populateConceptScores(List<UserConceptScores> conceptScores) {
        if (binding == null || binding.layoutConceptsContent == null) return;

        // Clear any previous views inside the container
        binding.layoutConceptsContent.removeAllViews();

        if (conceptScores == null || conceptScores.isEmpty()) {
            return;
        }

        for (UserConceptScores concept : conceptScores) {
            // Create a simple text view or row view for each concept score
            android.widget.TextView tvConcept = new android.widget.TextView(requireContext());
            tvConcept.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            tvConcept.setPadding(0, 8, 0, 8);
            tvConcept.setTextColor(getResources().getColor(android.R.color.black, null));
            tvConcept.setTextSize(14f);

            // Use the exact pre-existing model getters: getName(), getScore(), and getMaxScore()
            String conceptText = String.format(Locale.getDefault(), "• %s: %d / %d",
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