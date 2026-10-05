package com.example.capstone.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentAuthSignupBinding;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.viewmodel.LoginViewModel;
import com.example.capstone.viewmodel.SignupViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class AuthSignupFragment extends Fragment {

  private FragmentAuthSignupBinding binding;
  private SignupViewModel signupViewModel;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState
  ) {

    binding = FragmentAuthSignupBinding.inflate(inflater, container, false);
    return binding.getRoot();

  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    MyApplication app = (MyApplication) requireActivity().getApplication();
    AppContainer container = app.getAppContainer();
    IAuthRepository authRepository = container.authRepository;
    //create factory
    ViewModelFactory<SignupViewModel> factory = new ViewModelFactory<>(
        ()-> new SignupViewModel(authRepository));
    signupViewModel = new ViewModelProvider(this,factory).get(SignupViewModel.class);
    binding.buttonSecond.setOnClickListener(v ->
        NavHostFragment.findNavController(AuthSignupFragment.this)
            .navigate(R.id.action_signup_to_login)
    );
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}