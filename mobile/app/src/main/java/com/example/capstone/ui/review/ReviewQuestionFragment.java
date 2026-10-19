package com.example.capstone.ui.review;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentReviewQuestionBinding;
import com.example.capstone.domainModels.UserConceptScores;
import com.example.capstone.domainModels.UserQuestionAttempt;
import com.example.capstone.repository.IInterviewAttemptsRepository;
import com.example.capstone.ui.components.BottomNavHelper;
import com.example.capstone.viewmodel.ReviewViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewQuestionFragment extends Fragment {

    private FragmentReviewQuestionBinding binding;
    private List<UserQuestionAttempt> reviewQuestionsList = new ArrayList<>();
    private int currentReviewIndex = 0;
    private ReviewViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewQuestionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadReviewData();
        setupClickListeners();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        BottomNavHelper.setupNavListeners(this, bottomBar);

        MyApplication app =
            (MyApplication) requireActivity().getApplication();

        AppContainer container = app.getAppContainer();

        IInterviewAttemptsRepository interviewAttemptsRepository = container.interviewAttemptsRepository;

        // Create ViewModel factory
        ViewModelFactory<ReviewViewModel> factory =
            new ViewModelFactory<>(
                () -> new ReviewViewModel(interviewAttemptsRepository)
            );

        // Create ViewModel
        viewModel = new ViewModelProvider(requireActivity(), factory)
            .get(ReviewViewModel.class);

        reviewQuestionsList = viewModel.getQuestions();

        viewModel.getCurrentQuestionIndex().observe(getViewLifecycleOwner(), index->{
            if(index>= 0){
                System.out.println(currentReviewIndex);
                displayQuestionReview(index);
                displaySessionMetadata();
            }
        });

        binding.btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.moveToNextQuestion();
            }
        });

        binding.btnPrevQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.moveToPreviousQuestion();
            }
        });
    }

    private void displaySessionMetadata() {
        if (binding == null || viewModel.getCurrentInterviewAttempt().getValue() == null) return;
        int index = viewModel.getCurrentQuestionIndex().getValue() != null ? viewModel.getCurrentQuestionIndex().getValue() : 0;
        if (index < 0 || index >= reviewQuestionsList.size()) return;

        UserQuestionAttempt q = reviewQuestionsList.get(index);

        if (binding.tvRoleTitle != null) {
            binding.tvRoleTitle.setText(viewModel.getCurrentInterviewAttempt().getValue().getName());
        }
        if (binding.tvDifficulty != null) {
            String difficulty = viewModel.getCurrentInterviewAttempt().getValue().getDifficulty();
            binding.tvDifficulty.setText(difficulty != null ? difficulty : "");
            binding.tvDifficulty.setTextColor(getColorForDifficulty(difficulty));
        }
        if (binding.tvTimer != null) {
            long totalSeconds = q.getSecondsSpent();
            long minutes = Math.floorDiv(totalSeconds,60);
            long seconds = totalSeconds -  (minutes*60);
            binding.tvTimer.setText(String.format(
                Locale.getDefault(),
                "%02d:%02d",
                minutes,
                seconds
            ));
        }
        if(binding.tvDate!=null){
            if (viewModel.getCurrentInterviewAttempt().getValue().getCompletedAt()> 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                binding.tvDate.setText(sdf.format(new Date(viewModel.getCurrentInterviewAttempt().getValue().getCompletedAt())));
            }
        }
        if(binding.tvScoreVal!= null){
            double score = q.getScore();
            double maxScore = q.getMaxScore();
            binding.tvScoreVal.setText(String.format("%.2f/%.2f", score, maxScore));
        }
    }
    private int getColorForDifficulty(String difficulty) {
        if (difficulty == null) return Color.parseColor("#FF8A8A97");

        switch (difficulty.trim().toLowerCase(Locale.ROOT)) {
            case "easy":
                return Color.parseColor("#FF08B425"); // Green
            case "medium":
                return Color.parseColor("#FFF7A93B"); // Orange
            case "hard":
                return Color.parseColor("#FFEF5350"); // Red
            default:
                return Color.parseColor("#FF8A8A97"); // Default Gray
        }
    }
    private void loadReviewData() {
        reviewQuestionsList = new ArrayList<>();

        // =========================================================================
        // TODO FOR PERSON 2: Replace this mock implementation with a call to your
        // ViewModel or Repository to fetch the actual UserQuestionAttempt data
        // for the selected interview attempt (bundle-free architecture).
        // =========================================================================

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

        UserQuestionAttempt mockAttempt = new UserQuestionAttempt();
        mockAttempt.setType("Technical");
        mockAttempt.setQuestion("Explain how polymorphism works in C# and give a practical example.");
        mockAttempt.setAnswer("Polymorphism allows objects to be treated as instances of their parent class...");
        mockAttempt.setExpectedAnswer("Polymorphism is categorized into compile-time and runtime...");
        mockAttempt.setScore(4);
        mockAttempt.setMaxScore(5);
        mockAttempt.setConceptScores(mockScores);

        reviewQuestionsList.add(mockAttempt);
    }

    /**
     * Public injection hook for Person 2 / ViewModel to supply the list of question attempts directly.
     */
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

        if (binding.tvQuestionCounter != null) {
            binding.tvQuestionCounter.setText(String.format(Locale.getDefault(), "Question %d / %d", index + 1, total));
        }

        String category = q.getType() != null ? q.getType().toUpperCase(Locale.getDefault()) : "TECHNICAL";
        if (binding.tvCategoryTitle != null) {
            binding.tvCategoryTitle.setText(category);
        }
        if (binding.tvQuestionText != null) {
            binding.tvQuestionText.setText(q.getQuestion() != null ? q.getQuestion() : "No question text available.");
        }

        if (binding.tvUserAnswerText != null) {
            binding.tvUserAnswerText.setText(q.getAnswer() != null ? q.getAnswer() : "No answer provided.");
        }
        if (binding.tvExpectedAnswerText != null) {
            binding.tvExpectedAnswerText.setText(q.getExpectedAnswer() != null ? q.getExpectedAnswer() : "No expected answer available.");
        }

        populateConceptScores(q.getConceptScores());
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

            String conceptText = String.format(Locale.getDefault(), "• %s: %.1f / %d",
                    concept.getName(),
                    concept.getScore(),
                    concept.getMaxScore()
            );

            tvConcept.setText(conceptText);
            binding.layoutConceptsContent.addView(tvConcept);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}