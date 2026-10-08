package com.example.capstone.ui.mock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentSelectDifficultyBinding;

public class SelectDifficultyFragment extends Fragment {

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private FragmentSelectDifficultyBinding binding;
    private Difficulty selectedDifficulty = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectDifficultyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupOptionListeners();
        setupActionButtons();
        updateContinueButtonState();
    }

    private void setupOptionListeners() {
        binding.optionEasy.setOnClickListener(v -> selectDifficulty(Difficulty.EASY));
        binding.optionMedium.setOnClickListener(v -> selectDifficulty(Difficulty.MEDIUM));
        binding.optionHard.setOnClickListener(v -> selectDifficulty(Difficulty.HARD));
    }

    private void selectDifficulty(Difficulty difficulty) {
        selectedDifficulty = difficulty;

        // Toggle stroke outline on custom views
        binding.optionEasy.setSelectedState(difficulty == Difficulty.EASY);
        binding.optionMedium.setSelectedState(difficulty == Difficulty.MEDIUM);
        binding.optionHard.setSelectedState(difficulty == Difficulty.HARD);

        updateContinueButtonState();
    }

    private void updateContinueButtonState() {
        boolean isSelected = (selectedDifficulty != null);
        binding.btnContinue.setEnabled(isSelected);
        binding.btnContinue.setAlpha(isSelected ? 1.0f : 0.5f);
    }

    private void setupActionButtons() {
        binding.btnBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp()
        );

        binding.btnContinue.setOnClickListener(v -> {
            if (selectedDifficulty == null) return;


            String difficultyName = selectedDifficulty.name().substring(0, 1).toUpperCase()
                    + selectedDifficulty.name().substring(1).toLowerCase();

            Bundle bundle = new Bundle();
            bundle.putString(SelectInterviewFragment.ARG_DIFFICULTY, difficultyName);


            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_selectDifficultyFragment_to_selectInterviewFragment, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}