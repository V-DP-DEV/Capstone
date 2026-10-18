package com.example.capstone.ui.review;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.adapter.ReviewInterviewAdapter;
import com.example.capstone.databinding.FragmentReviewInterviewBinding;
import com.example.capstone.domainModels.UserAttemptCompetencyBreakdownFeedback;
import com.example.capstone.domainModels.UserAttemptCompetencyFeedback;
import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserQuestionAttempt;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.repository.IInterviewAttemptsRepository;
import com.example.capstone.ui.BaseActivity;
import com.example.capstone.ui.components.BottomNavHelper;
import com.example.capstone.viewmodel.LoginViewModel;
import com.example.capstone.viewmodel.ReviewViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReviewInterviewFragment extends Fragment {

    private FragmentReviewInterviewBinding binding;
    private ReviewInterviewAdapter adapter;

    private UserInterviewAttempt currentAttempt;
    private boolean isStrengthsExpanded = false;
    private boolean isAreasExpanded = true;
    private boolean isAnalysisRequested = false;
    private ReviewViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewInterviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get app-wide repository
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

        Bundle args= getArguments();
        long attemptId = args.getLong("attempt_id");

        viewModel.loadInterview(attemptId);


        viewModel.getCurrentInterviewAttempt().observe(getViewLifecycleOwner(),attempt->{
            if(attempt!=null){
                //initializeAttemptData();
                currentAttempt = attempt;
                displaySessionMetadata();
                setupRecyclerView();
                if(attempt.isAnalysed()){
                    System.out.println("It is analysed");
                    bindCompetencyFeedbackData();
                    // Reveal the competency breakdown card
                    if (binding.cardCompetencies != null) {
                        binding.cardCompetencies.setVisibility(View.VISIBLE);
                    }

                    // Update button text to "Review questions"
                    if (binding.reviewQuestions != null) {
                        binding.reviewQuestions.setText("Review questions");
                    }
                }
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(),isLoading->{
            if(Boolean.TRUE.equals(isLoading)){
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).showLoadingDialog("Getting interview...");
                }
            }
            else{
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).hideLoadingDialog();
                }
            }
        });

        viewModel.getIsAnalysing().observe(getViewLifecycleOwner(),isAnalysing->{
            if(Boolean.TRUE.equals(isAnalysing)){
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).showLoadingDialog("Analysing attempt...");
                }
            }
            else{
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).hideLoadingDialog();
                }
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(),error->{
            if (error != null && !error.isEmpty()) {
                BaseActivity baseActivity = ((BaseActivity) getActivity());
            }
        });

        viewModel.getAnalysed().observe(getViewLifecycleOwner(),analysed->{
            if(analysed){
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).showAlertDialog("Analysed!","Your attempt has been analysed");
                }
                viewModel.loadInterview(attemptId);
            }
        });


        //initializeAttemptData();
        //displaySessionMetadata();
        setupRecyclerView();
        setupClickListeners();
        setupDropdownListeners();

        // Initially hide the competencies card until analysis is requested
        if (binding.cardCompetencies != null) {
            binding.cardCompetencies.setVisibility(View.GONE);
        }

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        BottomNavHelper.setupNavListeners(this, bottomBar);
    }

    /**
     * Binds session metadata directly from the root UserInterviewAttempt domain model.
     */
    private void displaySessionMetadata() {
        if (currentAttempt == null) return;

        if (binding.cDeveloper != null) {
            binding.cDeveloper.setText(currentAttempt.getName());
        }
        if (binding.hard != null) {
            binding.hard.setText(currentAttempt.getDifficulty());
        }
        if (binding.tv10Questions != null) {
            int questionCount = currentAttempt.getQuestions().size();
            binding.tv10Questions.setText(String.format(Locale.getDefault(), "%d questions", questionCount));
        }
        if (binding.tvDuration != null) {
            long totalSeconds = currentAttempt.getTotalSecondsSpent();
            long minutes = Math.floorDiv(totalSeconds,60);
            long seconds = totalSeconds -  (minutes*60);
            long estimatedMinutes = currentAttempt.getEstimatedMinutes();
            binding.tvDuration.setText(
                String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    minutes,
                    seconds
                ) + "/"+ String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    estimatedMinutes,
                    0
                )
            );
        }
        if(binding.tvDate != null){

        }
    }

    /**
     * Initializes the root UserInterviewAttempt model and nested feedback lists.
     */
    private void initializeAttemptData() {
        currentAttempt = new UserInterviewAttempt();
        currentAttempt.setName("C# Developer");
        currentAttempt.setDifficulty("Hard");
        currentAttempt.setAnalysed(false);

        List<UserQuestionAttempt> questionAttempts = new ArrayList<>();

        // Question 1
        UserQuestionAttempt qa1 = new UserQuestionAttempt();
        qa1.setType("Introduction");
        qa1.setQuestion("Tell me about yourself and your technical background.");
        qa1.setScore(5);
        qa1.setMaxScore(5);
        qa1.setSecondsSpent(84); // 01:24
        questionAttempts.add(qa1);

        // Question 2
        UserQuestionAttempt qa2 = new UserQuestionAttempt();
        qa2.setType("Technical");
        qa2.setDifficulty("Hard");
        qa2.setQuestion("Explain the four principles of OOP.");
        qa2.setScore(5);
        qa2.setMaxScore(5);
        qa2.setSecondsSpent(84); // 01:24
        questionAttempts.add(qa2);

        // Question 3
        UserQuestionAttempt qa3 = new UserQuestionAttempt();
        qa3.setType("Technical");
        qa3.setQuestion("Interface vs abstract class in C#?");
        qa3.setScore(5);
        qa3.setMaxScore(5);
        qa3.setSecondsSpent(84); // 01:24
        questionAttempts.add(qa3);

        currentAttempt.setQuestions(questionAttempts);

        // TODO FOR PERSON 2 (LIVEDATA / REPOSITORY INTEGRATION):
        // Observe LiveData from ViewModel to update these attempts dynamically.
    }

    private void setupDropdownListeners() {
        if (binding == null) return;

        // Strengths Section Toggle
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

        // Areas to Improve Section Toggle
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

    private void setupClickListeners() {
        if (binding == null) return;

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> navigateBack());
        }

        // Bottom Pill Button ("Request Analysis" -> toggles to -> "Review questions")
        if (binding.divPillBtn != null) {
            binding.divPillBtn.setOnClickListener(v -> {
                if (!isAnalysisRequested) {
                    requestAiAnalysis();
                } else {
                    navigateToQuestionReview();
                }
            });
        }
    }

    /**
     * Triggers AI Analysis generation, reveals the competencies card, binds the feedback models, and updates button text.
     */
    private void requestAiAnalysis() {
        // =========================================================================
        // TODO FOR PERSON 2: Trigger ViewModel / Repository AI analysis call here
        // =========================================================================

        viewModel.analyseAttempt();

        /*
        isAnalysisRequested = true;
        currentAttempt.setAnalysed(true);

        // Reveal the competency breakdown card
        if (binding.cardCompetencies != null) {
            binding.cardCompetencies.setVisibility(View.VISIBLE);
        }

        // Update button text to "Review questions"
        if (binding.reviewQuestions != null) {
            binding.reviewQuestions.setText("Review questions");
        }

        // Bind competency data using domain model getters
        bindCompetencyFeedbackData();

        if (getContext() != null) {
            Toast.makeText(getContext(), "AI Analysis Generated Successfully!", Toast.LENGTH_SHORT).show();
        }

         */
    }


    private void bindCompetencyFeedbackData() {
        if (currentAttempt == null || currentAttempt.getCompetencyFeedbacks() == null || currentAttempt.getCompetencyFeedbacks().isEmpty()) {
            return;
        }

        UserAttemptCompetencyFeedback cf = currentAttempt.getCompetencyFeedbacks().get(0);

        if (binding.tvScorePercent != null) {
            binding.tvScorePercent.setText(String.format(Locale.getDefault(), "%.0f%%", cf.getCompetencyPercentage()));
        }

        // Populate Strengths list from breakdown items
        if (binding.layoutStrengthsContent != null) {
            binding.layoutStrengthsContent.removeAllViews();
            List<UserAttemptCompetencyBreakdownFeedback> strengths = cf.getStrengths();
            if (strengths != null) {
                for (UserAttemptCompetencyBreakdownFeedback item : strengths) {
                    addBreakdownItemView(binding.layoutStrengthsContent, item);
                }
            }
        }

        // Populate Areas to Improve list from breakdown items
        if (binding.layoutAreasContent != null) {
            binding.layoutAreasContent.removeAllViews();
            List<UserAttemptCompetencyBreakdownFeedback> improvements = cf.getImprovements();
            if (improvements != null) {
                for (UserAttemptCompetencyBreakdownFeedback item : improvements) {
                    addBreakdownItemView(binding.layoutAreasContent, item);
                }
            }
        }
    }

    private void addBreakdownItemView(ViewGroup container, UserAttemptCompetencyBreakdownFeedback breakdownItem) {
        if (container == null || breakdownItem == null) return;

        LinearLayout itemLayout = new LinearLayout(requireContext());
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setPadding(0, 8, 0, 12);

        TextView tvTitle = new TextView(requireContext());
        tvTitle.setText(String.format(Locale.getDefault(), "• %s (%.0f%%)", breakdownItem.getName(), breakdownItem.getBreakdownPercentage()));
        tvTitle.setTextSize(14f);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setTextColor(getResources().getColor(android.R.color.black, null));
        itemLayout.addView(tvTitle);

        TextView tvDesc = new TextView(requireContext());
        tvDesc.setText(breakdownItem.getFeedback() != null ? breakdownItem.getFeedback() : "No qualitative feedback available.");
        tvDesc.setTextSize(12f);
        tvDesc.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        itemLayout.addView(tvDesc);

        container.addView(itemLayout);
    }

    private void setupRecyclerView() {
        List<UserQuestionAttempt> questions = currentAttempt != null ? currentAttempt.getQuestions() : new ArrayList<>();

        adapter = new ReviewInterviewAdapter(questions, (questionAttempt, position) -> {
            navigateToQuestionReview();
        });

        if (binding.rvQList != null) {
            binding.rvQList.setAdapter(adapter);
            binding.rvQList.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
    }

    private void navigateToQuestionReview() {
        if (isAdded()) {
            NavController navController = NavHostFragment.findNavController(this);
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() == R.id.reviewInterviewFragment) {
                // Navigate cleanly without bundles
                navController.navigate(R.id.action_reviewInterviewFragment_to_reviewQuestionFragment);
            }
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