package com.example.capstone.ui.mock;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.capstone.R;
import com.example.capstone.adapter.InterviewAdapter;
import com.example.capstone.databinding.FragmentSelectInterviewBinding;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserQuestion;
import com.example.capstone.ui.components.BottomNavHelper;

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
    private List<UserInterview> difficultyFilteredList;
    private UserInterview selectedInterview = null;

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
        // Inflate the layout using ViewBinding
        binding = FragmentSelectInterviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve difficulty argument passed from SelectDifficultyFragment
        if (getArguments() != null && getArguments().containsKey(ARG_DIFFICULTY)) {
            selectedDifficulty = getArguments().getString(ARG_DIFFICULTY, "All");
        }

        setupHeader();
        setupRecyclerView();
        setupSearchFilter();
        setupActionButtons();
        updateContinueButtonState();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        BottomNavHelper.setupNavListeners(this, bottomBar);
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
        difficultyFilteredList = getInterviewsByDifficulty(selectedDifficulty);

        adapter = new InterviewAdapter(new ArrayList<>(difficultyFilteredList), interview -> {
            selectedInterview = interview;
            updateContinueButtonState();
        });

        binding.rvInterviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInterviews.setAdapter(adapter);

        // =========================================================================
        // TODO FOR PERSON 2 (LIVEDATA INTEGRATION):
        // Initialize your ViewModel here and observe the LiveData stream for interviews:
        //
        //   viewModel.getInterviewsLiveData().observe(getViewLifecycleOwner(), allInterviews -> {
        //       if (allInterviews != null) {
        //           // Update local cache and refresh filtered list based on selectedDifficulty
        //           difficultyFilteredList = filterByDifficulty(allInterviews, selectedDifficulty);
        //           adapter.updateData(new ArrayList<>(difficultyFilteredList));
        //       }
        //   });
        //
        // This ensures the UI automatically reacts to asynchronous database/API updates.
        // =========================================================================
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
     * Filters the interview list dynamically against the user's search query (matching name, category, or questions).
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
     * Configures back navigation and forward navigation with bundled interview metadata.
     */
    private void setupActionButtons() {
        binding.btnBack.setOnClickListener(v -> {
            if (isAdded()) {
                NavHostFragment.findNavController(this).navigateUp();
            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            if (selectedInterview == null) return;

            int questionCount = selectedInterview.getQuestions() != null
                    ? selectedInterview.getQuestions().size()
                    : selectedInterview.getTotalQuestions();

            String durationText = "~" + (selectedInterview.getDuration() > 0 ? selectedInterview.getDuration() : 25) + "m";

            // Package selected interview details into a bundle for the instructions screen
            Bundle args = new Bundle();
            args.putString(MockInterviewInstructionsFragment.ARG_INTERVIEW_NAME, selectedInterview.getName());
            args.putString(MockInterviewInstructionsFragment.ARG_INTERVIEW_DIFFICULTY, selectedInterview.getDifficulty());
            args.putInt(MockInterviewInstructionsFragment.ARG_QUESTION_COUNT, questionCount);
            args.putString(MockInterviewInstructionsFragment.ARG_DURATION, durationText);
            args.putString(MockInterviewInstructionsFragment.ARG_INPUT_TYPE, "Mic / Text");

            if (isAdded()) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_selectInterviewFragment_to_mockInterviewInstructionsFragment, args);
            }
        });
    }

    /**
     * Filters sample or repository data by the specified difficulty level.
     */
    private List<UserInterview> getInterviewsByDifficulty(String difficulty) {
        List<UserInterview> allInterviews = getAllSampleData();
        if (difficulty == null || difficulty.equalsIgnoreCase("All")) {
            return allInterviews;
        }

        List<UserInterview> filtered = new ArrayList<>();
        for (UserInterview item : allInterviews) {
            if (item.getDifficulty() != null && item.getDifficulty().equalsIgnoreCase(difficulty)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    /**
     * Placeholder method returning sample data. To be replaced with ViewModel/Repository data by Person 2.
     */
    private List<UserInterview> getAllSampleData() {
        List<UserInterview> list = new ArrayList<>();

        // =========================================================================
        // TODO FOR PERSON 2: Replace this placeholder sample data list with a live query
        // to your Repository / ViewModel fetching available UserInterview items from Room or API.
        // =========================================================================
        list.add(new UserInterview("C# Software Engineer", "Hard"));
        list.add(new UserInterview("Android Developer", "Medium"));
        list.add(new UserInterview("Full Stack Web Developer", "Intermediate"));

        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear binding reference to prevent memory leaks in fragments
        binding = null;
    }
}