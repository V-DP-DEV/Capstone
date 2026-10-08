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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.capstone.adapter.InterviewAdapter;
import com.example.capstone.databinding.FragmentSelectInterviewBinding;
import com.example.capstone.model.Interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectInterviewFragment extends Fragment {

    public static final String ARG_DIFFICULTY = "selected_difficulty";

    private FragmentSelectInterviewBinding binding;
    private InterviewAdapter adapter;
    private List<Interview> difficultyFilteredList;
    private Interview selectedInterview = null;

    private String selectedDifficulty = "All";

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

        if (getArguments() != null && getArguments().containsKey(ARG_DIFFICULTY)) {
            selectedDifficulty = getArguments().getString(ARG_DIFFICULTY, "All");
        }

        setupHeader();
        setupRecyclerView();
        setupSearchFilter();
        setupActionButtons();
        updateContinueButtonState();
    }

    private void setupHeader() {
        binding.tvSubHeader.setText("Difficulty: " + selectedDifficulty);
    }

    private void setupRecyclerView() {
        difficultyFilteredList = getInterviewsByDifficulty(selectedDifficulty);

        adapter = new InterviewAdapter(new ArrayList<>(difficultyFilteredList), interview -> {
            selectedInterview = interview;
            updateContinueButtonState();
        });

        binding.rvInterviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInterviews.setAdapter(adapter);
    }

    private void setupSearchFilter() {
        if (binding.includeSearch == null) return;

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

    private void filterInterviews(String query) {
        List<Interview> searchResults = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (Interview item : difficultyFilteredList) {
            boolean matchesTitle = item.getTitle() != null &&
                    item.getTitle().toLowerCase().contains(lowerCaseQuery);

            boolean matchesFocusArea = false;
            if (item.getFocusAreas() != null) {
                for (String area : item.getFocusAreas()) {
                    if (area.toLowerCase().contains(lowerCaseQuery)) {
                        matchesFocusArea = true;
                        break;
                    }
                }
            }

            if (matchesTitle || matchesFocusArea) {
                searchResults.add(item);
            }
        }

        adapter.updateData(searchResults);
    }

    private void updateContinueButtonState() {
        boolean isSelected = (selectedInterview != null);
        binding.btnContinue.setEnabled(isSelected);
        binding.btnContinue.setAlpha(isSelected ? 1.0f : 0.5f);
    }

    private void setupActionButtons() {
        binding.btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                NavHostFragment.findNavController(this).navigateUp();
            } else {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            if (selectedInterview == null) return;

            Toast.makeText(requireContext(),
                    "Starting " + selectedInterview.getTitle() + " interview",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private List<Interview> getInterviewsByDifficulty(String difficulty) {
        List<Interview> allInterviews = getAllSampleData();
        if (difficulty == null || difficulty.equalsIgnoreCase("All")) {
            return allInterviews;
        }

        List<Interview> filtered = new ArrayList<>();
        for (Interview item : allInterviews) {
            if (item.getDifficulty().equalsIgnoreCase(difficulty)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    private List<Interview> getAllSampleData() {
        List<Interview> list = new ArrayList<>();
        // Hard
        list.add(new Interview("1", "Java", "Hard", Arrays.asList("SQL", "Problem solving", "Communication", "System design")));
        list.add(new Interview("2", "C#", "Hard", Arrays.asList("SQL", "Problem solving", "Communication", "System design")));
        list.add(new Interview("3", "Python", "Hard", Arrays.asList("SQL", "Problem solving", "Communication", "System design")));

        // Medium
        list.add(new Interview("4", "Spring Boot", "Medium", Arrays.asList("REST API", "JPA", "Dependency Injection")));
        list.add(new Interview("5", "React Native", "Medium", Arrays.asList("State Management", "Hooks", "UI Components")));

        // Easy
        list.add(new Interview("6", "HTML / CSS", "Easy", Arrays.asList("Flexbox", "Responsive Layouts", "Grid")));
        list.add(new Interview("7", "Git Basics", "Easy", Arrays.asList("Branching", "Merging", "Commits")));
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}