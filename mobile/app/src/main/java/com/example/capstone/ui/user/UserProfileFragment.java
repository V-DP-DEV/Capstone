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
import com.example.capstone.databinding.FragmentUserProfileBinding;

public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setupClickListeners();
    }

    private void setupClickListeners() {

        if (binding.getRoot().findViewById(R.id.btn_edit_profile) != null) {
            binding.getRoot().findViewById(R.id.btn_edit_profile).setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_editProfileFragment);
            });
        }


        if (binding.getRoot().findViewById(R.id.btn_change_password) != null) {
            binding.getRoot().findViewById(R.id.btn_change_password).setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(R.id.action_userProfileFragment_to_changePasswordFragment);
            });
        }

        if (binding.bottomNavContainer != null) {

            // Bottom Nav: Home -> Dashboard
            View navHome = binding.bottomNavContainer.getRoot().findViewById(R.id.nav_home);
            if (navHome != null) {
                navHome.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(R.id.userDashboardFragment);
                });
            }

            // Bottom Nav: Settings -> User Profile
            View navSettings = binding.bottomNavContainer.getRoot().findViewById(R.id.nav_settings);
            if (navSettings != null) {
                navSettings.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(R.id.userProfileFragment);
                });
            }

            // Bottom Nav: Analytics -> Interview History
            View navAnalytics = binding.bottomNavContainer.getRoot().findViewById(R.id.nav_analytics);
            if (navAnalytics != null) {
                navAnalytics.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(R.id.interviewHistoryFragment);
                });
            }

            // Bottom Nav: Presentation -> Tips & Library
            View navPresentation = binding.bottomNavContainer.getRoot().findViewById(R.id.nav_presentation);
            if (navPresentation != null) {
                navPresentation.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(R.id.tipsLibraryFragment);
                });
            }

            // Bottom Nav: Profile -> User Profile Page
            View navProfile = binding.bottomNavContainer.getRoot().findViewById(R.id.nav_profile);
            if (navProfile != null) {
                navProfile.setOnClickListener(v -> {
                    // Already on profile page
                });
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clear binding reference to prevent memory leaks in fragments
        binding = null;
    }
}
