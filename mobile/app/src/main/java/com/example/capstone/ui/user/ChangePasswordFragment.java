
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

import com.example.capstone.MyApplication;
import com.example.capstone.databinding.FragmentChangePasswordBinding;
import com.example.capstone.viewmodel.ChangePasswordViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;
    private ChangePasswordViewModel changePasswordViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentChangePasswordBinding.inflate(
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

        MyApplication application =
                (MyApplication) requireActivity().getApplication();

        changePasswordViewModel = new ViewModelProvider(
                this,
                new ViewModelFactory<>(
                        () -> new ChangePasswordViewModel(
                                application.getAppContainer().userRepository
                        )
                )
        ).get(ChangePasswordViewModel.class);

        setupClickListeners();
        observeViewModel();
    }

    private void setupClickListeners() {

        binding.btnBack.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );

        binding.btnSavePassword.setOnClickListener(v -> {
            String oldPassword =
                    binding.etOldPassword.getText().toString();

            String newPassword =
                    binding.etNewPassword.getText().toString();

            String confirmPassword =
                    binding.etConfirmPassword.getText().toString();

            changePasswordViewModel.changePassword(
                    oldPassword,
                    newPassword,
                    confirmPassword
            );
        });

        binding.etOldPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.tvOldPasswordError.setVisibility(View.GONE);
            }
        });

        binding.etNewPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.tvNewPasswordError.setVisibility(View.GONE);
            }
        });

        binding.etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.tvConfirmPasswordError.setVisibility(View.GONE);
            }
        });
    }

    private void observeViewModel() {

        changePasswordViewModel.getOldPasswordError().observe(
                getViewLifecycleOwner(),
                error -> {
                    if (binding == null) {
                        return;
                    }

                    if (error != null && !error.isEmpty()) {
                        binding.tvOldPasswordError.setText(error);
                        binding.tvOldPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvOldPasswordError.setVisibility(View.GONE);
                    }
                }
        );

        changePasswordViewModel.getNewPasswordError().observe(
                getViewLifecycleOwner(),
                error -> {
                    if (binding == null) {
                        return;
                    }

                    if (error != null && !error.isEmpty()) {
                        binding.tvNewPasswordError.setText(error);
                        binding.tvNewPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvNewPasswordError.setVisibility(View.GONE);
                    }
                }
        );

        changePasswordViewModel.getConfirmPasswordError().observe(
                getViewLifecycleOwner(),
                error -> {
                    if (binding == null) {
                        return;
                    }

                    if (error != null && !error.isEmpty()) {
                        binding.tvConfirmPasswordError.setText(error);
                        binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvConfirmPasswordError.setVisibility(View.GONE);
                    }
                }
        );

        changePasswordViewModel.getGeneralError().observe(
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

        changePasswordViewModel.getLoading().observe(
                getViewLifecycleOwner(),
                loading -> {
                    if (binding == null || loading == null) {
                        return;
                    }

                    binding.btnSavePassword.setEnabled(!loading);

                    binding.btnSavePassword.setText(
                            loading
                                    ? "Please wait..."
                                    : "Update password"
                    );
                }
        );

        changePasswordViewModel.getChangePasswordSuccess().observe(
                getViewLifecycleOwner(),
                success -> {
                    if (Boolean.TRUE.equals(success) && binding != null) {
                        Toast.makeText(
                                requireContext(),
                                "Password changed successfully.",
                                Toast.LENGTH_SHORT
                        ).show();

                        binding.etOldPassword.setText("");
                        binding.etNewPassword.setText("");
                        binding.etConfirmPassword.setText("");

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
