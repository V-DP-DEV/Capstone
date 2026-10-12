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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectInterviewFragment extends Fragment {

    public static final String ARG_DIFFICULTY = "selected_difficulty";

    private FragmentSelectInterviewBinding binding;
    private InterviewAdapter adapter;
    private List<UserInterview> difficultyFilteredList;
    private UserInterview selectedInterview = null;

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
        if (binding.tvSubHeader != null) {
            binding.tvSubHeader.setText("Difficulty: " + selectedDifficulty);
        }
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

    private void updateContinueButtonState() {
        boolean isSelected = (selectedInterview != null);
        binding.btnContinue.setEnabled(isSelected);
        binding.btnContinue.setAlpha(isSelected ? 1.0f : 0.5f);
    }

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

    private List<UserInterview> getAllSampleData() {
        String jsonMockData = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Java Core & OOP Fundamentals\",\n" +
                "    \"difficulty\": \"Easy\",\n" +
                "    \"totalQuestions\": 4,\n" +
                "    \"duration\": 15,\n" +
                "    \"questions\": [\n" +
                "      {\"id\": \"q1\", \"text\": \"What is the difference between final, finally, and finalize in Java?\", \"difficulty\": \"Easy\", \"type\": \"Core Java\"},\n" +
                "      {\"id\": \"q2\", \"text\": \"Explain OOP concepts in Java with real-world examples.\", \"difficulty\": \"Easy\", \"type\": \"OOP\"}\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"Android Jetpack & Architecture\",\n" +
                "    \"difficulty\": \"Medium\",\n" +
                "    \"totalQuestions\": 5,\n" +
                "    \"duration\": 20,\n" +
                "    \"questions\": [\n" +
                "      {\"id\": \"q3\", \"text\": \"How does LiveData prevent memory leaks during lifecycle changes?\", \"difficulty\": \"Medium\", \"type\": \"Architecture\"},\n" +
                "      {\"id\": \"q4\", \"text\": \"What is the purpose of ViewModel and how does it survive rotation?\", \"difficulty\": \"Medium\", \"type\": \"Jetpack\"}\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"name\": \"SQL & Database Optimization\",\n" +
                "    \"difficulty\": \"Hard\",\n" +
                "    \"totalQuestions\": 6,\n" +
                "    \"duration\": 25,\n" +
                "    \"questions\": [\n" +
                "      {\"id\": \"q5\", \"text\": \"How do you optimize a query joining three large database tables?\", \"difficulty\": \"Hard\", \"type\": \"SQL\"}\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<UserInterview>>(){}.getType();
            List<UserInterview> sampleList = gson.fromJson(jsonMockData, listType);
            return sampleList != null ? sampleList : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}