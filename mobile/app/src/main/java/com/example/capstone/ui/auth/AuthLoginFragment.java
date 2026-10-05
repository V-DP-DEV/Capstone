package com.example.capstone.ui.auth;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

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
import com.example.capstone.databinding.FragmentAuthLoginBinding;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.viewmodel.LoginViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class AuthLoginFragment extends Fragment {

  private FragmentAuthLoginBinding binding;
  private LoginViewModel viewModel;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState
  ) {

    binding = FragmentAuthLoginBinding.inflate(inflater, container, false);
    return binding.getRoot();

  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //get app wide repository
    MyApplication app = (MyApplication) requireActivity().getApplication();
    AppContainer container = app.getAppContainer();
    IAuthRepository authRepository = container.authRepository;
    //create factory
    ViewModelFactory<LoginViewModel> factory = new ViewModelFactory<>(
        ()-> new LoginViewModel(authRepository));

    //create viewmodel
    viewModel = new ViewModelProvider(this,factory).get(LoginViewModel.class);

    binding.buttonFirst.setOnClickListener(v ->
        NavHostFragment.findNavController(AuthLoginFragment.this)
            .navigate(R.id.action_login_to_signup)
    );
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}