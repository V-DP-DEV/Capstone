package com.example.capstone.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentAuthLoginBinding;
import com.example.capstone.ui.user.UserActivity;

public class AuthLoginFragment extends Fragment {

  private FragmentAuthLoginBinding binding;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState
  ) {

    binding = FragmentAuthLoginBinding.inflate(inflater, container, false);
    binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(requireContext(), UserActivity.class);
        startActivity(intent);
      }
    });

    return binding.getRoot();

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Navigate to Sign Up Fragment when prompt is clicked
    binding.tvSignUpPrompt.setOnClickListener(v -> {
      Navigation.findNavController(v)
              .navigate(R.id.action_authLoginFragment_to_authSignUpFragment);
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}