package com.example.capstone.ui.analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.databinding.FragmentSkillsAnalysisBinding;
import com.example.capstone.repository.IAnalyticsRepository;
import com.example.capstone.adapter.CompetenciesAdapter;
import com.example.capstone.adapter.ConceptAdapter;
import com.example.capstone.util.BottomNavUtils;
import com.example.capstone.viewmodel.SkillsAnalysisViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class SkillsAnalysisFragment extends Fragment {

    private FragmentSkillsAnalysisBinding binding;
    private SkillsAnalysisViewModel viewModel;

    private CompetenciesAdapter competenciesAdapter;
    private ConceptAdapter conceptAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSkillsAnalysisBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        MyApplication app = (MyApplication) requireActivity().getApplication();
        AppContainer container = app.getAppContainer();
        IAnalyticsRepository analyticsRepository = container.analyticsRepository;


        ViewModelFactory<SkillsAnalysisViewModel> factory = new ViewModelFactory<>(
                () -> new SkillsAnalysisViewModel(analyticsRepository)
        );

        viewModel = new ViewModelProvider(this, factory).get(SkillsAnalysisViewModel.class);

        BottomNavUtils.setupBottomNavigation(view.findViewById(R.id.bottom_nav_container));


        setupRecyclerViews();
        setupClickListeners();
        observeViewModel();


        viewModel.loadCompetencies(10, "percentage", "DESC");
    }

    private void setupRecyclerViews() {
        binding.rvSkillsBody.setLayoutManager(new LinearLayoutManager(requireContext()));
        competenciesAdapter = new CompetenciesAdapter();
        conceptAdapter = new ConceptAdapter();
        binding.rvSkillsBody.setAdapter(competenciesAdapter);
    }

    private void setupClickListeners() {
        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        }

        binding.tabCoreSkills.setOnClickListener(v -> {
            viewModel.setCoreSkillsSelected(true);
            binding.rvSkillsBody.setAdapter(competenciesAdapter);
            viewModel.loadCompetencies(10, "percentage", "DESC");
        });

        binding.tabOtherSkills.setOnClickListener(v -> {
            viewModel.setCoreSkillsSelected(false);
            binding.rvSkillsBody.setAdapter(conceptAdapter);
            viewModel.loadConcepts(10, "percentage", "DESC");
        });
    }

    private void observeViewModel() {
        // Observe Competencies Data
        viewModel.getCompetencies().observe(getViewLifecycleOwner(), competencies -> {
            if (competencies != null) {
                competenciesAdapter.setCompetenciesList(competencies);
            }
        });

        // Observe Concepts Data
        viewModel.getConcepts().observe(getViewLifecycleOwner(), concepts -> {
            if (concepts != null) {
                conceptAdapter.setConceptList(concepts);
            }
        });

        // Observe Tab Selection State
        viewModel.getIsCoreSkillsSelected().observe(getViewLifecycleOwner(), this::updateTabSelection);

        // Observe General Errors
        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTabSelection(boolean coreSelected) {
        if (coreSelected) {
            binding.tabCoreSkills.setBackgroundResource(R.drawable.bg_circle_pill);
            binding.tabCoreSkills.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

            binding.tabOtherSkills.setBackground(null);
            binding.tabOtherSkills.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_grey));
        } else {
            binding.tabOtherSkills.setBackgroundResource(R.drawable.bg_circle_pill);
            binding.tabOtherSkills.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

            binding.tabCoreSkills.setBackground(null);
            binding.tabCoreSkills.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_grey));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}