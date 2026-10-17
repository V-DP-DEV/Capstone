package com.example.capstone.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentAuthSignupBinding;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.viewmodel.SignupViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class AuthSignupFragment extends Fragment {

  private FragmentAuthSignupBinding binding;
  private SignupViewModel signupViewModel;

  @Override
  public View onCreateView(
          @NonNull LayoutInflater inflater,
          ViewGroup container,
          Bundle savedInstanceState
  ) {

    binding = FragmentAuthSignupBinding.inflate(
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

    // Get application container
    MyApplication app =
            (MyApplication) requireActivity().getApplication();

    AppContainer appContainer =
            app.getAppContainer();

    IAuthRepository authRepository =
            appContainer.authRepository;

    // Create ViewModel factory
    ViewModelFactory<SignupViewModel> factory =
            new ViewModelFactory<>(
                    () -> new SignupViewModel(authRepository)
            );

    // Create ViewModel
    signupViewModel =
            new ViewModelProvider(this, factory)
                    .get(SignupViewModel.class);


    // CREATE PROFILE BUTTON
    binding.btnDone.setOnClickListener(v -> {

      signupViewModel.setFirstname(
              binding.etName.getText().toString().trim()
      );

      signupViewModel.setSurname(
              binding.etSurname.getText().toString().trim()
      );

      signupViewModel.setEmail(
              binding.etEmail.getText().toString().trim()
      );

      signupViewModel.setPassword(
              binding.etPassword.getText().toString()
      );

      signupViewModel.setConfirmPassword(
              binding.etConfirmPassword.getText().toString()
      );

      signupViewModel.signup();
    });


    // FIRST NAME ERROR
    signupViewModel.getFirstnameError().observe(
            getViewLifecycleOwner(),
            error -> {

              if (error != null && !error.isEmpty()) {
                binding.tvNameError.setText(error);
                binding.tvNameError.setVisibility(View.VISIBLE);
              } else {
                binding.tvNameError.setText("");
                binding.tvNameError.setVisibility(View.GONE);
              }
            }
    );


    // SURNAME ERROR
    signupViewModel.getSurnameError().observe(
            getViewLifecycleOwner(),
            error -> {

              if (error != null && !error.isEmpty()) {
                binding.tvSurnameError.setText(error);
                binding.tvSurnameError.setVisibility(View.VISIBLE);
              } else {
                binding.tvSurnameError.setText("");
                binding.tvSurnameError.setVisibility(View.GONE);
              }
            }
    );


    // EMAIL ERROR
    signupViewModel.getEmailError().observe(
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


    // PASSWORD ERROR
    signupViewModel.getPasswordError().observe(
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


    // CONFIRM PASSWORD ERROR
    signupViewModel.getConfirmPasswordError().observe(
            getViewLifecycleOwner(),
            error -> {

              if (error != null && !error.isEmpty()) {
                binding.tvConfirmPasswordError.setText(error);
                binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
              } else {
                binding.tvConfirmPasswordError.setText("");
                binding.tvConfirmPasswordError.setVisibility(View.GONE);
              }
            }
    );


    // GENERAL ERROR
    signupViewModel.getGeneralError().observe(
            getViewLifecycleOwner(),
            error -> {

              if (error != null && !error.isEmpty()) {
                binding.tvConfirmPasswordError.setText(error);
                binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
              }
            }
    );


    // LOADING
    signupViewModel.getLoading().observe(
            getViewLifecycleOwner(),
            loading -> {

              if (Boolean.TRUE.equals(loading)) {

                binding.btnDone.setEnabled(false);
                binding.btnDone.setText("Creating profile...");

              } else {

                binding.btnDone.setEnabled(true);
                binding.btnDone.setText(
                        getString(R.string.Create_Profile)
                );
              }
            }
    );


    // SIGNUP SUCCESS
    signupViewModel.getSignUpSuccess().observe(
            getViewLifecycleOwner(),
            success -> {

              if (Boolean.TRUE.equals(success)) {

                NavHostFragment.findNavController(
                        AuthSignupFragment.this
                ).navigate(
                        R.id.action_signUpFragment_to_authLoginFragment
                );
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