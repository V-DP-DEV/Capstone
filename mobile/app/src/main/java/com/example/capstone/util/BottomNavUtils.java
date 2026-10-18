package com.example.capstone.util;


import android.view.View;

import androidx.navigation.Navigation;

import com.example.capstone.R;

public class BottomNavUtils {

    // Private constructor to prevent instantiation of a utility class
    private BottomNavUtils() {}

    public static void setupBottomNavigation(View bottomNavView) {
        if (bottomNavView == null) return;

        // 1. Home -> User Dashboard
        View navHome = bottomNavView.findViewById(R.id.nav_home);
        if (navHome != null) {
            navHome.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.userDashboardFragment)
            );
        }

        // 2. Settings -> User Profile
        View navSettings = bottomNavView.findViewById(R.id.nav_settings);
        if (navSettings != null) {
            navSettings.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.userProfileFragment)
            );
        }

        // 3. Analytics -> Skills Analysis
        View navAnalytics = bottomNavView.findViewById(R.id.nav_analytics);
        if (navAnalytics != null) {
            navAnalytics.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.skillsAnalysisFragment)
            );
        }

        // 4. Presentation -> Interview History
        View navPresentation = bottomNavView.findViewById(R.id.nav_presentation);
        if (navPresentation != null) {
            navPresentation.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.interviewHistoryFragment)
            );
        }

        // 5. Profile -> User Profile
        View navProfile = bottomNavView.findViewById(R.id.nav_profile);
        if (navProfile != null) {
            navProfile.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.userProfileFragment)
            );
        }
    }
}
