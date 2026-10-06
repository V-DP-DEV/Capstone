package com.example.capstone.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentUserDashboardBinding;

public class UserDashboardFragment extends Fragment {

  private FragmentUserDashboardBinding binding;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState
  ) {

    binding = FragmentUserDashboardBinding.inflate(inflater, container, false);
    return binding.getRoot();

  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding.btnStartInterview.setOnClickListener(v -> {
      Navigation.findNavController(v)
              .navigate(R.id.action_userDashboardFragment_to_selectDifficultyFragment);
    });

    binding.actionStartInterview.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_selectDifficultyFragment)
    );

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}