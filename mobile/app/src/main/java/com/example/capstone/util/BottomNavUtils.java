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

        // 2. Start Interview -> Select Difficulty
        View navStartInterview = bottomNavView.findViewById(R.id.nav_start_interview);
        if (navStartInterview != null) {
            navStartInterview.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(R.id.selectDifficultyFragment)
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
