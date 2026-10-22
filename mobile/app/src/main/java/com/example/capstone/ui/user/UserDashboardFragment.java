package com.example.capstone.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentUserDashboardBinding;
import com.example.capstone.domainModels.UserDashboardMetrics;
import com.example.capstone.repository.IAnalyticsRepository;
import com.example.capstone.util.BottomNavUtils;
import com.example.capstone.viewmodel.UserDashboardViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class UserDashboardFragment extends Fragment {

  private FragmentUserDashboardBinding binding;
  private UserDashboardViewModel viewModel;

  @Override
  public View onCreateView(
          @NonNull LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState
  ) {
    binding = FragmentUserDashboardBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    MyApplication app = (MyApplication) requireActivity().getApplication();
    AppContainer appContainer = app.getAppContainer();
    IAnalyticsRepository analyticsRepository = appContainer.analyticsRepository;

    ViewModelFactory<UserDashboardViewModel> factory = new ViewModelFactory<>(() ->
            new UserDashboardViewModel(analyticsRepository)
    );

    viewModel = new ViewModelProvider(this, factory).get(UserDashboardViewModel.class);

    View bottomBar = view.findViewById(R.id.bottom_nav_container);
    if (bottomBar != null) {
      BottomNavUtils.setupBottomNavigation(bottomBar);
    }

    binding.btnStartMockInterview.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_selectDifficultyFragment)
    );
    if (binding.btnStartInterview != null) {
      binding.btnStartInterview.setOnClickListener(v ->
              Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_selectDifficultyFragment)
      );
    }

    if (binding.btnViewInterviews != null) {
      binding.btnViewInterviews.setOnClickListener(v ->
              Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_interviewHistoryFragment)
      );
    }

    if (binding.btnTipsLibrary != null) {
      binding.btnTipsLibrary.setOnClickListener(v ->
              Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_tipsLibraryFragment)
      );
    }

    if (binding.btnSkillsAnaylsis != null) {
      binding.btnSkillsAnaylsis.setOnClickListener(v ->
              Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_skillAnalysisFragment)
      );
    }

    ImageButton btnProfileAvatar = view.findViewById(R.id.img_profile_avatar);
    if (btnProfileAvatar != null) {
      btnProfileAvatar.setOnClickListener(v ->
              NavHostFragment.findNavController(this).navigate(R.id.action_userDashboardFragment_to_userProfileFragment)
      );
    }

    observeViewModel();

    viewModel.fetchDashboardData();
  }

  private void observeViewModel() {
    viewModel.getDashboardMetrics().observe(getViewLifecycleOwner(), metrics -> {
      if (metrics != null) {
        updateDashboardMetrics(metrics);
      }
    });

    viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
      if (errorMsg != null && !errorMsg.isEmpty()) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void updateDashboardMetrics(UserDashboardMetrics metrics) {
    if (metrics == null || binding == null) return;

    if (binding.totalMonth != null) {
      binding.totalMonth.setProgressDrawable(R.drawable.circle_progress_green);
      binding.totalMonth.setProgressData(metrics.getTotalMonth(), 4, "Monthly Total");
    }

    if (binding.totalWeek != null) {
      binding.totalWeek.setProgressDrawable(R.drawable.circle_progress_blue);
      binding.totalWeek.setProgressData(metrics.getTotalWeek(), 1, "Weekly Total");
    }

    if (binding.totalDifficult != null) {
      binding.totalDifficult.setProgressDrawable(R.drawable.circle_progress_orange);
      binding.totalDifficult.setProgressData(metrics.getTotalDifficult(), 1, "Difficulty");
    }
  }
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}