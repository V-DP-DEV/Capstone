package com.example.capstone.ui.components;


import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;

public class BottomNavHelper {

    /**
     * Wires up click listeners for your exact custom bottom navigation bar layout.
     *
     * @param fragment The current fragment hosting the bottom bar.
     * @param bottomNavRoot The root View of the included bottom bar (e.g., view.findViewById(R.id.bottom_nav_container)).
     */
    public static void setupNavListeners(Fragment fragment, View bottomNavRoot) {
        if (bottomNavRoot == null || !fragment.isAdded()) return;


        View navHome = bottomNavRoot.findViewById(R.id.nav_home);
        View navStartInterview = bottomNavRoot.findViewById(R.id.nav_start_interview);
        View navAnalytics = bottomNavRoot.findViewById(R.id.nav_analytics);
        View navPresentation = bottomNavRoot.findViewById(R.id.nav_presentation);
        View navProfile = bottomNavRoot.findViewById(R.id.nav_profile);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                if (fragment.isAdded()) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.userDashboardFragment) {
                        navController.navigate(R.id.userDashboardFragment);
                    }
                }
            });
        }

        if (navStartInterview != null) {
            navStartInterview.setOnClickListener(v -> {
                if (fragment.isAdded()) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.selectDifficultyFragment) {
                        navController.navigate(R.id.selectDifficultyFragment);
                    }
                }
            });
        }

        if (navAnalytics != null) {
            navAnalytics.setOnClickListener(v -> {

            });
        }

        if (navPresentation != null) {
            navPresentation.setOnClickListener(v -> {
                if (fragment.isAdded()) {
                    NavController navController = NavHostFragment.findNavController(fragment);
                    if (navController.getCurrentDestination() == null || navController.getCurrentDestination().getId() != R.id.tipsLibraryFragment) {
                        navController.navigate(R.id.tipsLibraryFragment);
                    }
                }
            });
        }


        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
            });
        }
    }
}
