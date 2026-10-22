package com.example.capstone.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentUserDashboardBinding;
import com.example.capstone.util.BottomNavUtils;

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
    if (bottomBar != null) {
      BottomNavUtils.setupBottomNavigation(bottomBar);


    }


    // Your dashboard button click listeners...
    binding.btnStartMockInterview.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_selectDifficultyFragment)
    );
    if (binding.btnStartInterview != null) {
      binding.btnStartInterview.setOnClickListener(v ->
              Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_selectDifficultyFragment)
      );
    }

    // 3. View Previous Interviews Quick Action Grid Item
    if (binding.btnViewInterviews != null) {
      binding.btnViewInterviews.setOnClickListener(v ->
              Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_interviewHistoryFragment)
      );
    }

    // 4. Tips & Library Quick Action Grid Item
    if (binding.btnTipsLibrary != null) {
      binding.btnTipsLibrary.setOnClickListener(v -> {
        Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_tipsLibraryFragment);
      });
    }

    // 5. Skills Analysis Quick Action Grid Item (matches XML id: btn_skills_anaylsis)
    if (binding.btnSkillsAnaylsis != null) {
      binding.btnSkillsAnaylsis.setOnClickListener(v ->
              Navigation.findNavController(v).navigate(R.id.action_userDashboardFragment_to_skillAnalysisFragment)
      );
    }
    ImageButton btnProfileAvatar = view.findViewById(R.id.img_profile_avatar);
    if (btnProfileAvatar != null) {
      btnProfileAvatar.setOnClickListener(v -> {
        // Navigate to your profile destination
        NavHostFragment.findNavController(this).navigate(R.id.action_userDashboardFragment_to_userProfileFragment);
      });
    }
  }


  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}