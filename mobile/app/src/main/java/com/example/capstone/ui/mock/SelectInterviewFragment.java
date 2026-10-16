package com.example.capstone.ui.mock;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.adapter.InterviewAdapter;
import com.example.capstone.databinding.FragmentSelectInterviewBinding;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserInterviewSummary;
import com.example.capstone.domainModels.UserQuestion;
import com.example.capstone.repository.IInterviewRepository;
import com.example.capstone.ui.BaseActivity;
import com.example.capstone.ui.components.BottomNavHelper;
import com.example.capstone.viewmodel.SetupViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectInterviewFragment displays a filtered list of available mock interviews
 * based on the difficulty chosen in the previous screen. It includes real-time
 * search filtering and handles user selection for downstream navigation.
 */
public class SelectInterviewFragment extends Fragment {

    // Bundle argument key for receiving the selected difficulty level
    public static final String ARG_DIFFICULTY = "selected_difficulty";

    private FragmentSelectInterviewBinding binding;
    private InterviewAdapter adapter;
    private List<UserInterview> difficultyFilteredList = new ArrayList<>();
    private UserInterview selectedInterview = null;

    private SetupViewModel setupViewModel;
    private String selectedDifficulty = "All";

    /**
     * Factory method to create a new instance of this fragment with a specified difficulty.
     */
    public static SelectInterviewFragment newInstance(String difficulty) {
        SelectInterviewFragment fragment = new SelectInterviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DIFFICULTY, difficulty);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectInterviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Retrieve difficulty argument passed from SelectDifficultyFragment
        if (getArguments() != null && getArguments().containsKey(ARG_DIFFICULTY)) {
            selectedDifficulty = getArguments().getString(ARG_DIFFICULTY, "All");
        }

        // 2. Instantiate SetupViewModel using AppContainer and ViewModelFactory
        MyApplication app = (MyApplication) requireActivity().getApplication();
        AppContainer container = app.getAppContainer();
        IInterviewRepository interviewRepository = container.interviewRepository;

        ViewModelFactory<SetupViewModel> factory = new ViewModelFactory<>(
                () -> new SetupViewModel(interviewRepository)
        );
        setupViewModel = new ViewModelProvider(this, factory).get(SetupViewModel.class);

        // 3. Setup UI components
        setupHeader();
        setupRecyclerView();
        setupSearchFilter();
        setupActionButtons();
        updateContinueButtonState();

        // 4. Setup Bottom Navigation
        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        if (bottomBar != null) {
            BottomNavHelper.setupNavListeners(this, bottomBar);
        }

        // 5. Setup Observers for ViewModel
        setupObservers();

        // 6. Trigger data fetch
        String filterDifficulty = "All".equalsIgnoreCase(selectedDifficulty) ? "" : selectedDifficulty;
        setupViewModel.loadInterviews(0, "", filterDifficulty);
    }

    /**
     * Observes LiveData from SetupViewModel
     */
    private void setupObservers() {
        // Observe interview list
        setupViewModel.getInterviews().observe(getViewLifecycleOwner(), interviewSummaries -> {
            if (interviewSummaries != null && !interviewSummaries.isEmpty()) {
                List<UserInterview> mappedList = new ArrayList<>();
                for (UserInterviewSummary summary : interviewSummaries) {
                    UserInterview ui = new UserInterview();
                    ui.setId((int) summary.getId());
                    ui.setName(summary.getName());
                    ui.setDifficulty(summary.getDifficulty());
                    mappedList.add(ui);
                }
                difficultyFilteredList = mappedList;
                adapter.updateData(new ArrayList<>(difficultyFilteredList));
            }
        });

        // Observe loading state
        setupViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (getActivity() instanceof BaseActivity) {
                if (Boolean.TRUE.equals(isLoading)) {
                    ((BaseActivity) getActivity()).showLoadingDialog("Loading interviews...");
                } else {
                    ((BaseActivity) getActivity()).hideLoadingDialog();
                }
            }
        });

        // Observe errors
        setupViewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.trim().isEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Updates the header title to reflect the active difficulty filter.
     */
    private void setupHeader() {
        if (binding.tvSubHeader != null) {
            binding.tvSubHeader.setText("Difficulty: " + selectedDifficulty);
        }
    }

    /**
     * Initializes the RecyclerView with filtered interview data and sets up the item selection listener.
     */
    private void setupRecyclerView() {
        adapter = new InterviewAdapter(new ArrayList<>(difficultyFilteredList), interview -> {
            selectedInterview = interview;
            updateContinueButtonState();
        });

        binding.rvInterviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInterviews.setAdapter(adapter);
    }

    /**
     * Attaches a text watcher to the search input to filter interviews in real-time.
     */
    private void setupSearchFilter() {
        if (binding.includeSearch == null || binding.includeSearch.etSearch == null) return;

        binding.includeSearch.etSearch.setHint("Search topic or focus area...");
        binding.includeSearch.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterInterviews(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Filters the interview list dynamically against the user's search query.
     */
    private void filterInterviews(String query) {
        List<UserInterview> searchResults = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (UserInterview item : difficultyFilteredList) {
            boolean matchesName = item.getName() != null &&
                    item.getName().toLowerCase().contains(lowerCaseQuery);

            boolean matchesCategory = item.getCategory() != null && item.getCategory().getName() != null &&
                    item.getCategory().getName().toLowerCase().contains(lowerCaseQuery);

            boolean matchesTypeOrQuestion = false;
            if (item.getQuestions() != null) {
                for (UserQuestion q : item.getQuestions()) {
                    if (q != null) {
                        if (q.getType() != null && q.getType().toLowerCase().contains(lowerCaseQuery)) {
                            matchesTypeOrQuestion = true;
                            break;
                        }
                        if (q.getText() != null && q.getText().toLowerCase().contains(lowerCaseQuery)) {
                            matchesTypeOrQuestion = true;
                            break;
                        }
                    }
                }
            }

            if (matchesName || matchesCategory || matchesTypeOrQuestion) {
                searchResults.add(item);
            }
        }

        adapter.updateData(searchResults);
    }

    /**
     * Toggles the Continue button enabled state and visual opacity based on whether an interview is selected.
     */
    private void updateContinueButtonState() {
        boolean isSelected = (selectedInterview != null);
        binding.btnContinue.setEnabled(isSelected);
        binding.btnContinue.setAlpha(isSelected ? 1.0f : 0.5f);
    }

    /**
     * Configures back navigation and forward navigation with bundled interview ID.
     */
    private void setupActionButtons() {
        binding.btnBack.setOnClickListener(v -> {
            if (isAdded()) {
                NavHostFragment.findNavController(this).navigateUp();
            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            if (selectedInterview == null) return;

            // Package the selected interview ID into bundle using ARG_INPUT_ID
            Bundle args = new Bundle();
            args.putInt(MockInterviewInstructionsFragment.ARG_INPUT_ID, (int) selectedInterview.getId());

            if (isAdded()) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_selectInterviewFragment_to_mockInterviewInstructionsFragment, args);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}