
package com.example.capstone.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentEditProfileBinding;

import com.example.capstone.viewmodel.ProfileViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

import com.example.capstone.util.BottomNavUtils;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentEditProfileBinding.inflate(
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

        // Retrieve the AppContainer from the actual application class.
        AppContainer appContainer =
                ((MyApplication) requireActivity().getApplication())
                        .getAppContainer();

        // Create the ProfileViewModel using the repository in AppContainer.
        profileViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory<>(
                        appContainer::createProfileViewModel
                )
        ).get(ProfileViewModel.class);

        setupClickListeners();

        observeViewModel();

        // Load the user's existing profile information.
        profileViewModel.loadProfile();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        if (bottomBar != null) {
            BottomNavUtils.setupBottomNavigation(bottomBar);
        }
    }

    private void setupClickListeners() {

        // Navigate back.
        binding.btnBack.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );

        // Save profile changes.
        binding.btnSaveProfile.setOnClickListener(v -> {
            String firstName =
                    binding.etFirstName.getText().toString();

            String surname =
                    binding.etSurname.getText().toString();

            profileViewModel.updateProfile(firstName, surname);
        });

        // Hide field errors when the user returns to a field.
        binding.etFirstName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.tvFirstNameError.setVisibility(View.GONE);
            }
        });

        binding.etSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.tvSurnameError.setVisibility(View.GONE);
            }
        });
    }

    private void observeViewModel() {

        // Display the user's current profile information.
        profileViewModel.getProfile().observe(
                getViewLifecycleOwner(),
                user -> {
                    if (user == null || binding == null) {
                        return;
                    }

                    binding.etFirstName.setText(user.getFirstName());
                    binding.etSurname.setText(user.getSurname());
                }
        );

        // Observe first-name validation errors.
        profileViewModel.getFirstNameError().observe(
                getViewLifecycleOwner(),
                error -> {
                    if (binding == null) {
                        return;
                    }

                    if (error != null && !error.isEmpty()) {
                        binding.tvFirstNameError.setText(error);
                        binding.tvFirstNameError.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvFirstNameError.setVisibility(View.GONE);
                    }
                }
        );

        // Observe surname validation errors.
        profileViewModel.getSurnameError().observe(
                getViewLifecycleOwner(),
                error -> {
                    if (binding == null) {
                        return;
                    }

                    if (error != null && !error.isEmpty()) {
                        binding.tvSurnameError.setText(error);
                        binding.tvSurnameError.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvSurnameError.setVisibility(View.GONE);
                    }
                }
        );

        // Display general API or network errors.
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

        // Prevent repeated submissions while loading.
        profileViewModel.getLoading().observe(
                getViewLifecycleOwner(),
                loading -> {
                    if (binding == null || loading == null) {
                        return;
                    }

                    binding.btnSaveProfile.setEnabled(!loading);

                    binding.btnSaveProfile.setText(
                            loading ? "Please wait..." : "Save changes"
                    );
                }
        );

        // Notify the user when the update succeeds.
        profileViewModel.getUpdateSuccess().observe(
                getViewLifecycleOwner(),
                success -> {
                    if (Boolean.TRUE.equals(success) && binding != null) {

                        Toast.makeText(
                                requireContext(),
                                "Profile updated successfully.",
                                Toast.LENGTH_SHORT
                        ).show();

                        Navigation.findNavController(binding.getRoot())
                                .popBackStack();
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

