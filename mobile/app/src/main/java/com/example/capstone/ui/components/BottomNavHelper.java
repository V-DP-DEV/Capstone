package com.example.capstone.ui.components;


import android.view.View;
import androidx.fragment.app.Fragment;
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

        // Find each ImageView using the exact IDs from your XML
        View navHome = bottomNavRoot.findViewById(R.id.nav_home);
        View navSettings = bottomNavRoot.findViewById(R.id.nav_settings);
        View navAnalytics = bottomNavRoot.findViewById(R.id.nav_analytics);
        View navPresentation = bottomNavRoot.findViewById(R.id.nav_presentation);
        View navProfile = bottomNavRoot.findViewById(R.id.nav_profile);

        // Wire up Home icon
        if (navHome != null) {
            navHome.setOnClickListener(v ->
                    NavHostFragment.findNavController(fragment).navigate(R.id.userDashboardFragment)
            );
        }

        // Wire up Settings icon
        if (navSettings != null) {
            navSettings.setOnClickListener(v -> {
                // Add destination ID when ready, e.g.:
                // NavHostFragment.findNavController(fragment).navigate(R.id.settingsFragment);
            });
        }

        // Wire up Analytics icon
        if (navAnalytics != null) {
            navAnalytics.setOnClickListener(v -> {
                // Add destination ID when ready
            });
        }

        // Wire up Presentation / Tips & Library icon
        if (navPresentation != null) {
            navPresentation.setOnClickListener(v ->
                    NavHostFragment.findNavController(fragment).navigate(R.id.tipsLibraryFragment)
            );
        }

        // Wire up Profile icon
        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                // Add destination ID when ready
            });
        }
    }
}
