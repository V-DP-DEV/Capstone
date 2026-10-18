package com.example.capstone.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentUserDashboardBinding;
import com.example.capstone.ui.components.BottomNavHelper;

public class UserDashboardFragment extends Fragment {

  private FragmentUserDashboardBinding binding;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState
  ) {

    binding = FragmentUserDashboardBinding.inflate(inflater, container, false);
    return binding.getRoot();

  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    View bottomBar = view.findViewById(R.id.bottom_nav_container);
    BottomNavHelper.setupNavListeners(this, bottomBar);

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


    // Bottom Nav: Profile -> User Profile Page
    View navProfile = binding.bottomNavContainer.getRoot().findViewById(R.id.nav_profile);
    if (navProfile != null) {
      navProfile.setOnClickListener(v -> {
        // Already on profile page
      });
    }
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}