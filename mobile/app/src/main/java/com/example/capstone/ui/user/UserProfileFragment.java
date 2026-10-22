
package com.example.capstone.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.capstone.viewmodel.ProfileViewModel;
import com.example.capstone.viewmodel.SettingsViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private SettingsViewModel settingsViewModel;
    private ProfileViewModel profileViewModel;

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

        profileViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory<>(
                        appContainer::createProfileViewModel
                )
        ).get(ProfileViewModel.class);

        setupClickListeners();
        observeViewModel();
        observeProfile();

        profileViewModel.loadProfile();
    }

    private void setupClickListeners() {

        View editProfile = binding.getRoot()
                .findViewById(R.id.btn_edit_profile);

        if (editProfile != null) {
            editProfile.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(
                            R.id.action_userProfileFragment_to_editProfileFragment
                    )
            );
        }

        View changePassword = binding.getRoot()
                .findViewById(R.id.btn_change_password);

        if (changePassword != null) {
            changePassword.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate(
                            R.id.action_userProfileFragment_to_changePasswordFragment
                    )
            );
        }

        View logoutButton = binding.getRoot()
                .findViewById(R.id.btn_logout);

        if (logoutButton != null) {
            logoutButton.setOnClickListener(v ->
                    showLogoutConfirmation(false)
            );
        }

        View logoutAllButton = binding.getRoot()
                .findViewById(R.id.btn_logout_all_devices);

        if (logoutAllButton != null) {
            logoutAllButton.setOnClickListener(v ->
                    showLogoutConfirmation(true)
            );
        }

        View deleteAccountButton = binding.getRoot()
                .findViewById(R.id.btn_delete_account);

        if (deleteAccountButton != null) {
            deleteAccountButton.setOnClickListener(v ->
                    showDeleteAccountConfirmation()
            );
        }

        // Bottom navigation
        if (binding.bottomNavContainer != null) {

            View navHome = binding.bottomNavContainer.getRoot()
                    .findViewById(R.id.nav_home);

            if (navHome != null) {
                navHome.setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.userDashboardFragment)
                );
            }

            View navStartInterview =
                    binding.bottomNavContainer.getRoot()
                            .findViewById(R.id.nav_start_interview);

            if (navStartInterview != null) {
                navStartInterview.setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.selectDifficultyFragment)
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

    private void showDeleteAccountConfirmation() {

        if (!isAdded()) {
            return;
        }

        EditText passwordInput = new EditText(requireContext());
        passwordInput.setHint("Enter your password");
        passwordInput.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        int padding = (int) (24 * getResources()
                .getDisplayMetrics().density);

        passwordInput.setPadding(
                padding, padding / 2, padding, padding / 2
        );

        new AlertDialog.Builder(requireContext())
                .setTitle("Delete account?")
                .setMessage(
                        "This action is permanent. Enter your password " +
                                "to confirm that you want to delete your account."
                )
                .setView(passwordInput)
                .setNegativeButton("Cancel", (dialog, which) ->
                        dialog.dismiss()
                )
                .setPositiveButton("Delete account", (dialog, which) -> {

                    String password = passwordInput.getText()
                            .toString();

                    if (password.trim().isEmpty()) {
                        Toast.makeText(
                                requireContext(),
                                "Please enter your password.",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    settingsViewModel.deleteAccount(password);
                })
                .show();
    }

    private void observeViewModel() {

        settingsViewModel.getLoading().observe(
                getViewLifecycleOwner(),
                loading -> {
                    if (binding == null || loading == null) {
                        return;
                    }

                    setButtonEnabled(R.id.btn_logout, !loading);
                    setButtonEnabled(
                            R.id.btn_logout_all_devices, !loading
                    );
                    setButtonEnabled(
                            R.id.btn_delete_account, !loading
                    );
                }
        );

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

        settingsViewModel.getLogoutSuccess().observe(
                getViewLifecycleOwner(),
                success -> {
                    if (Boolean.TRUE.equals(success) && binding != null) {
                        openAuthenticationScreen(
                                "You have been logged out."
                        );
                    }
                }
        );

        settingsViewModel.getLogoutAllDevicesSuccess().observe(
                getViewLifecycleOwner(),
                success -> {
                    if (Boolean.TRUE.equals(success) && binding != null) {
                        openAuthenticationScreen(
                                "You have been logged out."
                        );
                    }
                }
        );

        settingsViewModel.getDeleteAccountSuccess().observe(
                getViewLifecycleOwner(),
                success -> {
                    if (Boolean.TRUE.equals(success) && binding != null) {
                        openAuthenticationScreen(
                                "Your account has been deleted."
                        );
                    }
                }
        );
    }

    private void observeProfile() {

        profileViewModel.getProfile().observe(
                getViewLifecycleOwner(),
                user -> {
                    if (binding == null || user == null) {
                        return;
                    }

                    String firstName = user.getFirstName() == null
                            ? "" : user.getFirstName().trim();

                    String surname = user.getSurname() == null
                            ? "" : user.getSurname().trim();

                    String fullName =
                            (firstName + " " + surname).trim();

                    binding.tvUserFullname.setText(
                            fullName.isEmpty() ? "User" : fullName
                    );

                    String email = user.getEmail();

                    binding.tvUserEmail.setText(
                            email == null ? "" : email
                    );
                }
        );

        profileViewModel.getGeneralError().observe(
                getViewLifecycleOwner(),
                error -> {
                    if (binding != null
                            && error != null
                            && !error.trim().isEmpty()) {
                        Toast.makeText(
                                requireContext(),
                                error,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void setButtonEnabled(int viewId, boolean enabled) {

        if (binding == null) {
            return;
        }

        View button = binding.getRoot().findViewById(viewId);

        if (button != null) {
            button.setEnabled(enabled);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (profileViewModel != null) {
            profileViewModel.loadProfile();
        }
    }

    private void openAuthenticationScreen(String message) {

        if (!isAdded()) {
            return;
        }

        Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                requireActivity(),
                AuthActivity.class
        );

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
