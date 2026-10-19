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

import com.example.capstone.viewmodel.ReviewViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewInterviewFragment extends Fragment {

    private FragmentReviewInterviewBinding binding;
    private ReviewInterviewAdapter adapter;

    private UserInterviewAttempt currentAttempt;
    private boolean isStrengthsExpanded = false;
    private boolean isAreasExpanded = true;
    private boolean isAnalysisRequested = false;
    private long attemptId;
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


        setupViewModel();

        Bundle args= getArguments();
        attemptId = args.getLong("attempt_id");

        viewModel.loadInterview(attemptId);

        setupRecyclerView();
        setupClickListeners();
        setupDropdownListeners();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        BottomNavHelper.setupNavListeners(this, bottomBar);
    }

    private void setupViewModel(){
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
                baseActivity.showAlertDialog("Error occured",error);
            }
        });

        viewModel.getAnalysed().observe(getViewLifecycleOwner(),analysed->{
            if(analysed){
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).showAlertDialog("Analysed!","Your attempt has been analysed");
                }
                viewModel.getAnalysed().setValue(false);
                viewModel.loadInterview(attemptId);
            }
        });
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
            if (currentAttempt.getCompletedAt() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                binding.tvDate.setText(sdf.format(new Date(currentAttempt.getCompletedAt())));
            }
        }
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
        viewModel.analyseAttempt();
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