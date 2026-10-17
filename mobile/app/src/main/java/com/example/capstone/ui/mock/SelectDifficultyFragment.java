package com.example.capstone.ui.mock;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentSelectDifficultyBinding;
import com.example.capstone.ui.components.BottomNavHelper;

/**
 * SelectDifficultyFragment presents static options (Easy, Medium, Hard) for the user to choose
 * their preferred mock interview difficulty before proceeding to interview selection.
 */
public class SelectDifficultyFragment extends Fragment {

    /**
     * Enum defining available difficulty tiers and their UI display labels.
     */
    public enum Difficulty {
        EASY("Easy"),
        MEDIUM("Medium"),
        HARD("Hard");

        private final String displayName;

        Difficulty(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private FragmentSelectDifficultyBinding binding;
    private Difficulty selectedDifficulty = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout using ViewBinding
        binding = FragmentSelectDifficultyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupOptionListeners();
        setupActionButtons();
        updateContinueButtonState();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        BottomNavHelper.setupNavListeners(this, bottomBar);
    }

    /**
     * Attaches click listeners to the difficulty card option views.
     */
    private void setupOptionListeners() {
        binding.optionEasy.setOnClickListener(v -> selectDifficulty(Difficulty.EASY));
        binding.optionMedium.setOnClickListener(v -> selectDifficulty(Difficulty.MEDIUM));
        binding.optionHard.setOnClickListener(v -> selectDifficulty(Difficulty.HARD));
    }

    /**
     * Updates selected difficulty state and highlights the chosen option container visually.
     */
    private void selectDifficulty(Difficulty difficulty) {
        selectedDifficulty = difficulty;

        // Highlight selection state on option views using standard View setSelected state
        binding.optionEasy.setSelected(difficulty == Difficulty.EASY);
        binding.optionMedium.setSelected(difficulty == Difficulty.MEDIUM);
        binding.optionHard.setSelected(difficulty == Difficulty.HARD);


        updateCardHighlight(binding.optionEasy, difficulty == Difficulty.EASY);
        updateCardHighlight(binding.optionMedium, difficulty == Difficulty.MEDIUM);
        updateCardHighlight(binding.optionHard, difficulty == Difficulty.HARD);
        updateContinueButtonState();
    }
    /**
     * Toggles explicit card background color to provide immediate visual selection feedback.
     */
    private void updateCardHighlight(View cardView, boolean isSelected) {
        cardView.setSelected(isSelected);

        int backgroundColor = isSelected
                ? ContextCompat.getColor(requireContext(), R.color.color_icon_inactive)
                : Color.WHITE;
        if (cardView instanceof CardView) {
            ((CardView) cardView).setCardBackgroundColor(backgroundColor);
        } else {
            cardView.setBackgroundColor(backgroundColor);
        }
    }
    /**
     * Toggles the Continue button state and visual opacity based on whether a difficulty is selected.
     */
    private void updateContinueButtonState() {
        boolean isSelected = (selectedDifficulty != null);
        binding.btnContinue.setEnabled(isSelected);
        binding.btnContinue.setAlpha(isSelected ? 1.0f : 0.5f);
    }

    /**
     * Configures back navigation and passes selected difficulty string to SelectInterviewFragment via Bundle.
     */
    private void setupActionButtons() {
        binding.btnBack.setOnClickListener(v -> {
            if (isAdded()) {
                NavHostFragment.findNavController(this).navigateUp();
            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            if (selectedDifficulty == null) return;

            // Bundle selected difficulty display name for downstream filtering
            Bundle bundle = new Bundle();
            bundle.putString(SelectInterviewFragment.ARG_DIFFICULTY, selectedDifficulty.getDisplayName());

            if (isAdded()) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_selectDifficultyFragment_to_selectInterviewFragment, bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear binding reference to prevent memory leaks
        binding = null;
    }
}