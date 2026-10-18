package com.example.capstone.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentUserDashboardBinding;
import com.example.capstone.ui.components.BottomNavHelper;
import com.example.capstone.domainModels.User;
import com.example.capstone.model.request.UpdateUserRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.repository.IUserRepository;
import com.example.capstone.ui.components.BottomNavHelper;

import java.util.List;

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

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    View bottomBar = view.findViewById(R.id.bottom_nav_container);
    BottomNavHelper.setupNavListeners(this, bottomBar);

    binding.btnStartMockInterview.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_selectDifficultyFragment)
    );

    binding.btnStartInterview.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_selectDifficultyFragment)
    );

    binding.btnTipsLibrary.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_tipsLibraryFragment)
    );
    binding.btnViewInterviews.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_interviewHistoryFragment)
    );
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}