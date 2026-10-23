
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

        AppContainer appContainer =
                ((MyApplication) requireActivity().getApplication())
                        .getAppContainer();

        profileViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory<>(
                        appContainer::createProfileViewModel
                )
        ).get(ProfileViewModel.class);

        setupClickListeners();

        observeViewModel();

        profileViewModel.loadProfile();

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        if (bottomBar != null) {
            BottomNavUtils.setupBottomNavigation(bottomBar);
        }
    }

    private void setupClickListeners() {

        binding.btnBack.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );

        binding.btnSaveProfile.setOnClickListener(v -> {
            String firstName =
                    binding.etFirstName.getText().toString();

            String surname =
                    binding.etSurname.getText().toString();

            profileViewModel.updateProfile(firstName, surname);
        });

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

        profileViewModel.getProfile().observe(
                getViewLifecycleOwner(),
                user -> {
                    if (user == null || binding == null) {
                        return;
                    }

                    binding.etFirstName.setText(user.getFirstname());
                    binding.etSurname.setText(user.getSurname());
                }
        );

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

