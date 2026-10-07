package com.example.capstone.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentAuthLoginBinding;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.ui.user.UserActivity;
import com.example.capstone.viewmodel.LoginViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class AuthLoginFragment extends Fragment {

    private FragmentAuthLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAuthLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // Get app-wide repository
        MyApplication app =
                (MyApplication) requireActivity().getApplication();

        AppContainer container = app.getAppContainer();

        IAuthRepository authRepository = container.authRepository;

        // Create ViewModel factory
        ViewModelFactory<LoginViewModel> factory =
                new ViewModelFactory<>(
                        () -> new LoginViewModel(authRepository)
                );

        // Create ViewModel
        viewModel = new ViewModelProvider(this, factory)
                .get(LoginViewModel.class);

        // --------------------------------------------------
        // INPUT → VIEWMODEL
        // --------------------------------------------------

        binding.etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.setEmail(
                        binding.etEmail.getText().toString().trim()
                );
            }
        });

        binding.etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                viewModel.setPassword(
                        binding.etPassword.getText().toString()
                );
            }
        });

        // --------------------------------------------------
        // SIGN IN
        // --------------------------------------------------

        binding.btnSignIn.setOnClickListener(v -> {

            Log.d("LOGIN_TEST", "SIGN IN BUTTON CLICKED");

            viewModel.setEmail(
                    binding.etEmail.getText().toString().trim()
            );

            viewModel.setPassword(
                    binding.etPassword.getText().toString()
            );

            viewModel.login();
        });

        // --------------------------------------------------
        // OBSERVE EMAIL VALIDATION
        // --------------------------------------------------

        viewModel.getEmailError().observe(
                getViewLifecycleOwner(),
                error -> {

                    if (error != null && !error.isEmpty()) {
                        binding.tvEmailError.setText(error);
                        binding.tvEmailError.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvEmailError.setText("");
                        binding.tvEmailError.setVisibility(View.GONE);
                    }
                }
        );

        // --------------------------------------------------
        // OBSERVE PASSWORD VALIDATION
        // --------------------------------------------------

        viewModel.getPasswordError().observe(
                getViewLifecycleOwner(),
                error -> {

                    if (error != null && !error.isEmpty()) {
                        binding.tvPasswordError.setText(error);
                        binding.tvPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvPasswordError.setText("");
                        binding.tvPasswordError.setVisibility(View.GONE);
                    }
                }
        );

        // --------------------------------------------------
        // OBSERVE GENERAL ERROR
        // --------------------------------------------------

        viewModel.getGeneralError().observe(
                getViewLifecycleOwner(),
                error -> {

                    if (error != null && !error.isEmpty()) {
                        binding.tvPasswordError.setText(error);
                        binding.tvPasswordError.setVisibility(View.VISIBLE);
                    }
                }
        );

        // --------------------------------------------------
        // OBSERVE LOADING
        // --------------------------------------------------

        viewModel.getLoading().observe(
                getViewLifecycleOwner(),
                loading -> {

                    if (Boolean.TRUE.equals(loading)) {
                        binding.btnSignIn.setEnabled(false);
                        binding.btnSignIn.setText("Signing in...");
                    } else {
                        binding.btnSignIn.setEnabled(true);
                        binding.btnSignIn.setText(getString(R.string.sign_in));
                    }
                }
        );

        // --------------------------------------------------
        // LOGIN SUCCESS
        // --------------------------------------------------

        viewModel.getLoginSuccess().observe(
                getViewLifecycleOwner(),
                success -> {

                    if (Boolean.TRUE.equals(success)) {

                        Intent intent =
                                new Intent(
                                        requireContext(),
                                        UserActivity.class
                                );

                        startActivity(intent);
                    }
                }
        );

        // --------------------------------------------------
        // NAVIGATE TO SIGN UP
        // --------------------------------------------------

        binding.tvSignUpPrompt.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(
                                R.id.action_authLoginFragment_to_authSignUpFragment
                        )
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

