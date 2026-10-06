package com.example.capstone.ui.mockInterview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.capstone.databinding.FragmentSelectDifficultyBinding;

public class SelectDifficultyFragment extends Fragment {
    private FragmentSelectDifficultyBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
binding = FragmentSelectDifficultyBinding.inflate(inflater, container, false);
return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cardEasy.setOnClickListener(v -> startInterview("Easy"));
        binding.cardMedium.setOnClickListener(v -> startInterview("Medium"));
        binding.cardHard.setOnClickListener(v -> startInterview("Hard"));
    }
    private void startInterview(String difficulty) {
        Toast.makeText(requireContext(), "Selected Difficulty: " + difficulty, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
