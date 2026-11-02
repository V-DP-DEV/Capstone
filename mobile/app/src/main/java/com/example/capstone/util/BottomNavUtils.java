package com.example.capstone.util;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.capstone.R;

import java.util.function.Consumer;

public class BottomNavUtils {

    // Private constructor to prevent instantiation of a utility class
    private BottomNavUtils() {}

    public static void setupBottomNavigation(View bottomNavView) {
        if (bottomNavView == null) return;
        NavController navController;
        try {
            navController = Navigation.findNavController(bottomNavView);
        } catch (Exception e) {
            return;
        }

        //containers
        View navHome = bottomNavView.findViewById(R.id.nav_home);
        View navStartInterview = bottomNavView.findViewById(R.id.nav_start_interview);
        View navAnalytics = bottomNavView.findViewById(R.id.nav_analytics);
        View navPresentation = bottomNavView.findViewById(R.id.nav_presentation);
        View navProfile = bottomNavView.findViewById(R.id.nav_profile);

        ImageView iconHome = bottomNavView.findViewById(R.id.icon_home);
        ImageView iconInterview = bottomNavView.findViewById(R.id.icon_start_interview);
        ImageView iconAnalytics = bottomNavView.findViewById(R.id.icon_analytics);
        ImageView iconPresentation = bottomNavView.findViewById(R.id.icon_presentation);
        ImageView iconProfile = bottomNavView.findViewById(R.id.icon_profile);

        View indicatorHome = bottomNavView.findViewById(R.id.indicator_home);
        View indicatorInterview = bottomNavView.findViewById(R.id.indicator_interview);
        View indicatorAnalytics = bottomNavView.findViewById(R.id.indicator_analytics);
        View indicatorHistory = bottomNavView.findViewById(R.id.indicator_history);
        View indicatorProfile = bottomNavView.findViewById(R.id.indicator_profile);

        //User Dashboard
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.userDashboardFragment) {
                    navController.navigate(R.id.userDashboardFragment);
                }
            });
        }

        if (navStartInterview != null) {
            navStartInterview.setOnClickListener(v -> {
                if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.selectDifficultyFragment) {
                    navController.navigate(R.id.selectDifficultyFragment);
                }
            });
        }

        if (navAnalytics != null) {
            navAnalytics.setOnClickListener(v -> {
                if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.skillsAnalysisFragment) {
                    navController.navigate(R.id.skillsAnalysisFragment);
                }
            });
        }

        if (navPresentation != null) {
            navPresentation.setOnClickListener(v -> {
                if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.interviewHistoryFragment) {
                    navController.navigate(R.id.interviewHistoryFragment);
                }
            });
        }

        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.userProfileFragment) {
                    navController.navigate(R.id.userProfileFragment);
                }
            });
        }

        Consumer<Integer> updateSelection = (destId) -> {
            if (indicatorHome != null) indicatorHome.setVisibility(View.GONE);
            if (indicatorInterview != null) indicatorInterview.setVisibility(View.GONE);
            if (indicatorAnalytics != null) indicatorAnalytics.setVisibility(View.GONE);
            if (indicatorHistory != null) indicatorHistory.setVisibility(View.GONE);
            if (indicatorProfile != null) indicatorProfile.setVisibility(View.GONE);

            int inactiveColor = Color.parseColor("#FF868DA6");
            if (iconHome != null) iconHome.setColorFilter(inactiveColor);
            if (iconInterview != null) iconInterview.setColorFilter(inactiveColor);
            if (iconAnalytics != null) iconAnalytics.setColorFilter(inactiveColor);
            if (iconPresentation != null) iconPresentation.setColorFilter(inactiveColor);
            if (iconProfile != null) iconProfile.setColorFilter(inactiveColor);

            int activeColor = bottomNavView.getContext().getResources().getColor(R.color.primary_blue);

            if (destId == R.id.userDashboardFragment) {
                if (indicatorHome != null) indicatorHome.setVisibility(View.VISIBLE);
                if (iconHome != null) iconHome.setColorFilter(activeColor);
            } else if (destId == R.id.selectDifficultyFragment || destId == R.id.selectInterviewFragment || destId == R.id.mockInterviewInstructionsFragment || destId == R.id.mockInterviewQuestionsFragment || destId == R.id.mockInterviewResult) {
                if (indicatorInterview != null) indicatorInterview.setVisibility(View.VISIBLE);
                if (iconInterview != null) iconInterview.setColorFilter(activeColor);
            } else if (destId == R.id.skillsAnalysisFragment) {
                if (indicatorAnalytics != null) indicatorAnalytics.setVisibility(View.VISIBLE);
                if (iconAnalytics != null) iconAnalytics.setColorFilter(activeColor);
            } else if (destId == R.id.interviewHistoryFragment) {
                if (indicatorHistory != null) indicatorHistory.setVisibility(View.VISIBLE);
                if (iconPresentation != null) iconPresentation.setColorFilter(activeColor);
            } else if (destId == R.id.userProfileFragment || destId == R.id.editProfileFragment || destId == R.id.changePasswordFragment) {
                if (indicatorProfile != null) indicatorProfile.setVisibility(View.VISIBLE);
                if (iconProfile != null) iconProfile.setColorFilter(activeColor);
            }
        };

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            updateSelection.accept(destination.getId());
        });

        if (navController.getCurrentDestination() != null) {
            updateSelection.accept(navController.getCurrentDestination().getId());
        }
    }
}
