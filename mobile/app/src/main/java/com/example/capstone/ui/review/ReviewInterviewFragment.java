package com.example.capstone.ui.review;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.capstone.adapter.CompetencyFeedbackAdapter;
import com.example.capstone.adapter.ReviewInterviewAdapter;
import com.example.capstone.databinding.FragmentReviewInterviewBinding;
import com.example.capstone.domainModels.UserAttemptCompetencyBreakdownFeedback;
import com.example.capstone.domainModels.UserAttemptCompetencyFeedback;
import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserQuestionAttempt;
import com.example.capstone.repository.IInterviewAttemptsRepository;
import com.example.capstone.ui.BaseActivity;
import com.example.capstone.ui.components.BottomNavHelper;
import com.example.capstone.util.BottomNavUtils;
import com.example.capstone.viewmodel.ReviewViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewInterviewFragment extends Fragment {

    private static final String TAG = "ReviewInterviewFragment";
    private FragmentReviewInterviewBinding binding;
    private ReviewInterviewAdapter adapter;

    private CompetencyFeedbackAdapter competencyFeedbackAdapter;

    private UserInterviewAttempt currentAttempt;
    private boolean isStrengthsExpanded = false;
    private boolean isAreasExpanded = true;
    private ReviewViewModel viewModel;
    private long attemptId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReviewInterviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize RecyclerView immediately
        setupRecyclerView();

        // 2. Safe Argument Extraction with Fallback Keys
        Bundle args = getArguments();
        if (args != null) {
            attemptId = args.getLong("attempt_id", -1);
            if (attemptId == -1) attemptId = args.getLong("id", -1);
            if (attemptId == -1) attemptId = args.getLong("attemptId", -1);

            Log.d(TAG, "Extracted attemptId: " + attemptId);
        } else {
            Log.w(TAG, "getArguments() is null!");
        }

        // Get app-wide repository
        MyApplication app = (MyApplication) requireActivity().getApplication();
        AppContainer container = app.getAppContainer();
        IInterviewAttemptsRepository interviewAttemptsRepository = container.interviewAttemptsRepository;

        // Create ViewModel factory
        ViewModelFactory<ReviewViewModel> factory = new ViewModelFactory<>(
                () -> new ReviewViewModel(interviewAttemptsRepository)
        );

        // Create ViewModel
        viewModel = new ViewModelProvider(requireActivity(), factory)
                .get(ReviewViewModel.class);

        if (attemptId != -1) {
            viewModel.loadInterview(attemptId);
        } else {
            Toast.makeText(requireContext(), "Error: Interview ID not found", Toast.LENGTH_SHORT).show();
        }

        // Observe Interview Attempt LiveData
        viewModel.getCurrentInterviewAttempt().observe(getViewLifecycleOwner(), attempt -> {
            if (attempt != null) {
                currentAttempt = attempt;
                displaySessionMetadata();

                boolean analysed = attempt.isAnalysed();
                List<UserQuestionAttempt> questions = attempt.getQuestions();

                // Update question cards and state flag in adapter
                if (adapter != null) {
                    adapter.updateData(questions != null ? questions : new ArrayList<>(), analysed);
                }

                // Handle State 1 vs State 2 UI updates

                if (analysed) {
                    // Populate duplicated competency cards via adapter
                    System.out.println(competencyFeedbackAdapter.getItemCount());
                    System.out.println(attempt.getCompetencyFeedbacks()!=null);
                    if (competencyFeedbackAdapter != null && attempt.getCompetencyFeedbacks() != null) {
                        competencyFeedbackAdapter.setData(attempt.getCompetencyFeedbacks());
                    }
                    System.out.println(competencyFeedbackAdapter.getItemCount());
                    if (binding.rvCompetencyCards != null) {
                        binding.rvCompetencyCards.setVisibility(View.VISIBLE);
                    }

                    binding.rvCompetencyCards.post(() -> {
                        Log.d(TAG, "Visibility: "
                            + binding.rvCompetencyCards.getVisibility());

                        Log.d(TAG, "Adapter count: "
                            + competencyFeedbackAdapter.getItemCount());

                        Log.d(TAG, "RecyclerView height: "
                            + binding.rvCompetencyCards.getHeight());

                        Log.d(TAG, "Measured height: "
                            + binding.rvCompetencyCards.getMeasuredHeight());

                        Log.d(TAG, "Layout manager: "
                            + binding.rvCompetencyCards.getLayoutManager());

                        Log.d(TAG, "Child count: "
                            + binding.rvCompetencyCards.getChildCount());
                    });

                    System.out.println(binding.rvCompetencyCards.getVisibility());
                    if (binding.reviewQuestions != null) {
                        binding.reviewQuestions.setText("Review questions");
                    }
                } else {
                    if (binding.rvCompetencyCards != null) {
                        binding.rvCompetencyCards.setVisibility(View.GONE);
                    }
                    if (binding.reviewQuestions != null) {
                        binding.reviewQuestions.setText("Request Analysis");
                    }
                }
            }
        });

        // Observe Loading State
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).showLoadingDialog("Getting interview...");
                }
            } else {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).hideLoadingDialog();
                }
            }
        });

        // Observe Analysis Loading State
        viewModel.getIsAnalysing().observe(getViewLifecycleOwner(), isAnalysing -> {
            if (Boolean.TRUE.equals(isAnalysing)) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).showLoadingDialog("Analysing attempt...");
                }
            } else {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).hideLoadingDialog();
                }
            }
        });

        // Observe Errors
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getAnalysisError().observe(getViewLifecycleOwner(), analysisError -> {
            if (analysisError != null && !analysisError.isEmpty()) {
                Toast.makeText(requireContext(), analysisError, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe Successful Analysis Completion
        viewModel.getAnalysedCallSuccesful().observe(getViewLifecycleOwner(), analysed -> {
            if (Boolean.TRUE.equals(analysed)) {
                if (getActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).showAlertDialog("Analysed!", "Your attempt has been analysed successfully.");
                    viewModel.getAnalysedCallSuccesful().setValue(false);
                }
                if (attemptId != -1) {
                    viewModel.loadInterview(attemptId);
                }
            }
        });

        setupClickListeners();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        if (bottomBar != null) {
            BottomNavUtils.setupBottomNavigation(bottomBar);
        }
    }

    private void setupRecyclerView() {
        if (binding == null || binding.rvQList == null) return;

        if (adapter == null) {
            adapter = new ReviewInterviewAdapter(new ArrayList<>(), false, (questionAttempt, position) -> {
                if (currentAttempt != null && currentAttempt.isAnalysed()) {
                } else {
                    Toast.makeText(requireContext(), "Request analysis to review individual answers.", Toast.LENGTH_SHORT).show();
                }
                viewModel.moveToQuestion(position);
                navigateToQuestionReview();
            });
        }
        binding.rvQList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvQList.setAdapter(adapter);
        binding.rvQList.setNestedScrollingEnabled(false);
        if (competencyFeedbackAdapter == null) {
            competencyFeedbackAdapter = new CompetencyFeedbackAdapter();
        }

        binding.rvCompetencyCards.setLayoutManager(new LinearLayoutManager(requireContext()));binding.rvCompetencyCards.setAdapter(competencyFeedbackAdapter);
        binding.rvCompetencyCards.setNestedScrollingEnabled(false);
    }

    private void displaySessionMetadata() {
        if (currentAttempt == null || binding == null) return;

        if (binding.cDeveloper != null) {
            binding.cDeveloper.setText(currentAttempt.getName() != null ? currentAttempt.getName() : "Interview");
        }
        if (binding.hard != null) {
            binding.hard.setText(currentAttempt.getDifficulty() != null ? currentAttempt.getDifficulty() : "");
        }
        if (binding.tv10Questions != null) {
            int questionCount = currentAttempt.getQuestions() != null ? currentAttempt.getQuestions().size() : 0;
            binding.tv10Questions.setText(String.format(Locale.getDefault(), "%d questions", questionCount));
        }
        if (binding.hard != null) {
            String difficulty = currentAttempt.getDifficulty() != null ? currentAttempt.getDifficulty() : "";
            binding.hard.setText(difficulty);
            binding.hard.setTextColor(getColorForDifficulty(difficulty));
        }
        if (binding.tvDuration != null) {
            long totalSeconds = currentAttempt.getTotalSecondsSpent();
            long minutes = Math.floorDiv(totalSeconds, 60);
            long seconds = totalSeconds - (minutes * 60);
            long estimatedMinutes = currentAttempt.getEstimatedMinutes();
            binding.tvDuration.setText(
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds) + "/" +
                            String.format(Locale.getDefault(), "%02d:%02d", estimatedMinutes, 0)
            );
        }
        if (binding.tvDate != null && currentAttempt.getCompletedAt() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            binding.tvDate.setText(sdf.format(new Date(currentAttempt.getCompletedAt())));
        }
        if (binding.tvAnalysedTag != null) {
            if (currentAttempt.isAnalysed()) {
                binding.tvAnalysedTag.setVisibility(View.VISIBLE);
                binding.tvAnalysedTag.setText("Analysed");
                binding.tvAnalysedTag.setTextColor(Color.parseColor("#FF08B425")); // Green tag
            } else {
                binding.tvAnalysedTag.setVisibility(View.GONE); // Hide until analysis is requested & completed
            }
        }
    }



    private void setupClickListeners() {
        if (binding == null) return;

        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> navigateBack());
        }

        // Bottom Pill Button ("Request Analysis" vs "Review questions")
        if (binding.divPillBtn != null) {
            binding.divPillBtn.setOnClickListener(v -> {
                if (currentAttempt != null) {
                    if (!currentAttempt.isAnalysed()) {
                        viewModel.analyseAttempt();
                    } else {
                        navigateToQuestionReview();
                    }
                }
            });
        }
    }


    private void addBreakdownItemView(ViewGroup container, UserAttemptCompetencyBreakdownFeedback breakdownItem) {
        if (container == null || breakdownItem == null) return;

        LinearLayout itemLayout = new LinearLayout(requireContext());
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setPadding(0, 12, 0, 16);

        TextView tvTitle = new TextView(requireContext());
        double percentage = breakdownItem.getBreakdownPercentage();
        double normalizedPercentage = percentage <= 1.0 ? percentage * 100.0 : percentage;

        tvTitle.setText(String.format(Locale.getDefault(), "• %s (%.0f%%)", breakdownItem.getName(), normalizedPercentage));
        tvTitle.setTextSize(14f);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setTextColor(getResources().getColor(android.R.color.black, null));
        itemLayout.addView(tvTitle);

        TextView tvDesc = new TextView(requireContext());
        tvDesc.setText(breakdownItem.getFeedback() != null ? breakdownItem.getFeedback() : "No qualitative feedback available.");
        tvDesc.setTextSize(12f);
        tvDesc.setTextColor(getResources().getColor(android.R.color.darker_gray, null));

        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        descParams.topMargin = 4;
        tvDesc.setLayoutParams(descParams);

        itemLayout.addView(tvDesc);
        container.addView(itemLayout);
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
    private int getColorForPercentage(double percentage) {
        if (percentage < 40.0) {
            return Color.parseColor("#FFEF5350"); // Red
        } else if (percentage < 70.0) {
            return Color.parseColor("#FFF7A93B"); // Orange
        } else {
            return Color.parseColor("#FF08B425"); // Green
        }
    }

    private void navigateToQuestionReview() {
        if (isAdded()) {
            NavController navController = NavHostFragment.findNavController(this);
            if (navController.getCurrentDestination() != null
                    && navController.getCurrentDestination().getId() == R.id.reviewInterviewFragment) {
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