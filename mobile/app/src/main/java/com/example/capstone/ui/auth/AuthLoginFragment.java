package com.example.capstone.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentAuthLoginBinding;

public class AuthLoginFragment extends Fragment {

  private FragmentAuthLoginBinding binding;

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