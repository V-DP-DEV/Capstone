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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentMockInterviewInstructionsBinding;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.repository.IInterviewAttemptsRepository;
import com.example.capstone.repository.IInterviewRepository;
import com.example.capstone.ui.BaseActivity;
import com.example.capstone.util.BottomNavUtils;
import com.example.capstone.viewmodel.AnswerViewModel;
import com.example.capstone.viewmodel.LoginViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;
import com.example.capstone.ui.components.BottomNavHelper;

/**
 * MockInterviewInstructionsFragment displays the pre-interview briefing screen,
 * showing details like interview title, difficulty, question count, duration,
 * and input type before the user officially starts the session.
 */
public class MockInterviewInstructionsFragment extends Fragment {
    public static final String ARG_INPUT_ID = "arg_input_it";
    private AnswerViewModel answerViewModel;

    private FragmentMockInterviewInstructionsBinding binding;

    /**
     * Factory method to create a new instance of this fragment packed with the required interview details.
     */
    public static MockInterviewInstructionsFragment newInstance(int id) {
        MockInterviewInstructionsFragment fragment = new MockInterviewInstructionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INPUT_ID,id);
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

        MyApplication app =
            (MyApplication) requireActivity().getApplication();

        AppContainer container = app.getAppContainer();

        IInterviewRepository interviewRepository = container.interviewRepository;
        IInterviewAttemptsRepository interviewAttemptsRepository = container.interviewAttemptsRepository;


        ViewModelFactory<AnswerViewModel> factory =
            new ViewModelFactory<>(
                () -> new AnswerViewModel(interviewRepository,interviewAttemptsRepository)
            );


        answerViewModel = new ViewModelProvider(requireActivity(), factory)
            .get(AnswerViewModel.class);


        answerViewModel.getCurrentInterview().observe(
            getViewLifecycleOwner(), interview -> {
                if (interview != null) {
                    if (getActivity() instanceof BaseActivity) {
                        // 1. Show loading spinner
                        ((BaseActivity) getActivity()).hideLoadingDialog();
                    }
                    displayInstructionsDetails();
                }
            });

        answerViewModel.getIsLoading().observe(getViewLifecycleOwner(),isLoading->{
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

        answerViewModel.getError().observe(getViewLifecycleOwner(),error->{
            if (error != null && !error.isEmpty()) {
                BaseActivity baseActivity = ((BaseActivity) getActivity());
                baseActivity.showAlertDialog("Error",error,()->{
                    NavHostFragment.findNavController(this).navigateUp();
                });
            }
        });

        Bundle args = getArguments();

        if (args != null) {
            int id = args.getInt(MockInterviewInstructionsFragment.ARG_INPUT_ID);
            System.out.println(id);
            answerViewModel.loadInterview(id);
        }

        setupClickListeners();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        if (bottomBar != null) {
            BottomNavUtils.setupBottomNavigation(bottomBar);
        }
    }



    /**
     * Extracts bundle arguments and binds the interview metadata to the corresponding instruction TextViews.
     */
    private void displayInstructionsDetails() {
        UserInterview interview = answerViewModel.getCurrentInterview().getValue();
        if (interview == null) return;
        String name = interview.getName() != null ? interview.getName() : "Mock Interview";
        String difficulty = interview.getDifficulty() != null ? interview.getDifficulty() : "Medium";
        int questionCount = interview.getTotalQuestions();
        String duration = String.valueOf(interview.getDuration());
        String inputType = "Mic";

        if (binding.tvInterviewName != null) {
            binding.tvInterviewName.setText(name);
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
            answerViewModel.clearViewModel();
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}