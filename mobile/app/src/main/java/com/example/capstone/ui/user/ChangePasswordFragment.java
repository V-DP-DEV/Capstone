package com.example.capstone.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentChangePasswordBinding;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupClickListeners();
    }

    private void setupClickListeners() {
        // Back Button Navigation
        if (binding.getRoot().findViewById(R.id.btnBack) != null) {
            binding.getRoot().findViewById(R.id.btnBack).setOnClickListener(v -> {
                Navigation.findNavController(v).popBackStack();
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}