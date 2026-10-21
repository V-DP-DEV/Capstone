
package com.example.capstone.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentUserProfileBinding;
import com.example.capstone.ui.auth.AuthActivity;
import com.example.capstone.viewmodel.SettingsViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private SettingsViewModel settingsViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentUserProfileBinding.inflate(
                inflater,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        AppContainer appContainer =
                ((MyApplication) requireActivity().getApplication())
                        .getAppContainer();

        settingsViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory<>(
                        appContainer::createSettingsViewModel
                )
        ).get(SettingsViewModel.class);

        setupClickListeners();
        observeViewModel();
    }

    private void setupClickListeners() {

        // Edit profile.
        View editProfile = binding.getRoot()
                .findViewById(R.id.btn_edit_profile);

        if (editProfile != null) {
            editProfile.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(
                            R.id.action_userProfileFragment_to_editProfileFragment
                    )
            );
        }

        // Change password.
        View changePassword = binding.getRoot()
                .findViewById(R.id.btn_change_password);

        if (changePassword != null) {
            changePassword.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(
                            R.id.action_userProfileFragment_to_changePasswordFragment
                    )
            );
        }

        // Log out of the current session.
        View logoutButton = binding.getRoot()
                .findViewById(R.id.btn_logout);

        if (logoutButton != null) {
            logoutButton.setOnClickListener(v ->
                    showLogoutConfirmation(false)
            );
        }

        // Log out all devices.
        View logoutAllButton = binding.getRoot()
                .findViewById(R.id.btn_logout_all_devices);

        if (logoutAllButton != null) {
            logoutAllButton.setOnClickListener(v ->
                    showLogoutConfirmation(true)
            );
        }

        // Bottom navigation.
        if (binding.bottomNavContainer != null) {

            View navHome = binding.bottomNavContainer.getRoot()
                    .findViewById(R.id.nav_home);

            if (navHome != null) {
                navHome.setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.userDashboardFragment)
                );
            }

            View navSettings = binding.bottomNavContainer.getRoot()
                    .findViewById(R.id.nav_settings);

            if (navSettings != null) {
                navSettings.setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.userProfileFragment)
                );
            }

            View navAnalytics = binding.bottomNavContainer.getRoot()
                    .findViewById(R.id.nav_analytics);

            if (navAnalytics != null) {
                navAnalytics.setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.interviewHistoryFragment)
                );
            }

            View navPresentation = binding.bottomNavContainer.getRoot()
                    .findViewById(R.id.nav_presentation);

            if (navPresentation != null) {
                navPresentation.setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.tipsLibraryFragment)
                );
            }

            View navProfile = binding.bottomNavContainer.getRoot()
                    .findViewById(R.id.nav_profile);

            if (navProfile != null) {
                navProfile.setOnClickListener(v -> {
                    // Already on the profile page.
                });
            }
        }
    }

    private void showLogoutConfirmation(boolean allDevices) {

        if (!isAdded()) {
            return;
        }

        String title = allDevices
                ? "Log out all devices?"
                : "Log out?";

        String message = allDevices
                ? "This will log you out on all devices. You may need to sign in again."
                : "Are you sure you want to log out?";

        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Cancel", (dialog, which) ->
                        dialog.dismiss()
                )
                .setPositiveButton("Log out", (dialog, which) -> {
                    if (allDevices) {
                        settingsViewModel.logoutAllDevices();
                    } else {
                        settingsViewModel.logout();
                    }
                })
                .show();
    }

    private void observeViewModel() {

        // Prevent repeated logout requests while one is in progress.
        settingsViewModel.getLoading().observe(
                getViewLifecycleOwner(),
                loading -> {
                    if (binding == null || loading == null) {
                        return;
                    }

                    View logoutButton = binding.getRoot()
                            .findViewById(R.id.btn_logout);

                    View logoutAllButton = binding.getRoot()
                            .findViewById(R.id.btn_logout_all_devices);

                    if (logoutButton != null) {
                        logoutButton.setEnabled(!loading);
                    }

                    if (logoutAllButton != null) {
                        logoutAllButton.setEnabled(!loading);
                    }
                }
        );

        // Display API or network errors.
        settingsViewModel.getGeneralError().observe(
                getViewLifecycleOwner(),
                error -> {
                    if (binding == null
                            || error == null
                            || error.trim().isEmpty()) {
                        return;
                    }

                    Toast.makeText(
                            requireContext(),
                            error,
                            Toast.LENGTH_LONG
                    ).show();
                }
        );

        // Navigate to authentication after a successful normal logout.
        settingsViewModel.getLogoutSuccess().observe(
                getViewLifecycleOwner(),
                success -> {
                    if (Boolean.TRUE.equals(success) && binding != null) {
                        openAuthenticationScreen();
                    }
                }
        );

        // Navigate to authentication after logging out all devices.
        settingsViewModel.getLogoutAllDevicesSuccess().observe(
                getViewLifecycleOwner(),
                success -> {
                    if (Boolean.TRUE.equals(success) && binding != null) {
                        openAuthenticationScreen();
                    }
                }
        );
    }

    private void openAuthenticationScreen() {

        if (!isAdded()) {
            return;
        }

        Toast.makeText(
                requireContext(),
                "You have been logged out.",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                requireActivity(),
                AuthActivity.class
        );

        // Clear the user area so Back cannot reopen the profile.
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
