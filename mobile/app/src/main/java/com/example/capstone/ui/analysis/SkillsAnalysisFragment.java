package com.example.capstone.ui.analysis;

import android.graphics.Color;
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
import com.example.capstone.adapter.CompetenciesAdapter;
import com.example.capstone.adapter.ConceptAdapter;
import com.example.capstone.databinding.FragmentSkillsAnalysisBinding;
import com.example.capstone.repository.IAnalyticsRepository;
import com.example.capstone.ui.BaseActivity;
import com.example.capstone.util.BottomNavUtils;
import com.example.capstone.viewmodel.SkillsAnalysisViewModel;
import com.example.capstone.viewmodel.ViewModelFactory;

public class SkillsAnalysisFragment extends Fragment {

    private FragmentSkillsAnalysisBinding binding;
    private SkillsAnalysisViewModel viewModel;

    private CompetenciesAdapter competenciesAdapter;
    private ConceptAdapter conceptAdapter;

    private int currentLastInterviews = 3;
    private String currentSortBy = "percentage";
    private String currentSortOrder = "ASC"; // Weakest first

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

        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        if (bottomBar != null) {
            BottomNavUtils.setupBottomNavigation(bottomBar);
        }

        setupRecyclerViews();
        setupClickListeners();
        observeViewModel();

        // 4. Initial Data Fetch
        fetchData();
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
            fetchData();
        });

        binding.tabOtherSkills.setOnClickListener(v -> {
            viewModel.setCoreSkillsSelected(false);
            binding.rvSkillsBody.setAdapter(conceptAdapter);
            fetchData();
        });

        binding.tabInterviewLast3.setOnClickListener(v -> {
            currentLastInterviews = 3;
            updateInterviewFilterUI(0);
            fetchData();
        });
        binding.tabInterviewLast5.setOnClickListener(v -> {
            currentLastInterviews = 5;
            updateInterviewFilterUI(1);
            fetchData();
        });
        binding.tabInterviewAll.setOnClickListener(v -> {
            currentLastInterviews = 100; // All interviews
            updateInterviewFilterUI(2);
            fetchData();
        });

        binding.tabSkillWeakest.setOnClickListener(v -> {
            currentSortOrder = "ASC";
            updateSkillFilterUI(0);
            fetchData();
        });
        binding.tabSkillStrongest.setOnClickListener(v -> {
            currentSortOrder = "DESC";
            updateSkillFilterUI(1);
            fetchData();
        });
        binding.tabSkillAll.setOnClickListener(v -> {
            currentSortOrder = "DESC"; // Default fallback for all
            updateSkillFilterUI(2);
            fetchData();
        });
    }

    private void fetchData() {
        Boolean isCore = viewModel.getIsCoreSkillsSelected().getValue();
        if (isCore == null || isCore) {
            viewModel.loadCompetencies(currentLastInterviews, currentSortBy, currentSortOrder);
        } else {
            viewModel.loadConcepts(currentLastInterviews, currentSortBy, currentSortOrder);
        }
    }

    private void observeViewModel() {
        viewModel.getCompetencies().observe(getViewLifecycleOwner(), list -> {
            if (list != null) competenciesAdapter.setCompetenciesList(list);
        });

        viewModel.getConcepts().observe(getViewLifecycleOwner(), list -> {
            if (list != null) conceptAdapter.setConceptList(list);
        });

        viewModel.getIsCoreSkillsSelected().observe(getViewLifecycleOwner(), this::updateTabSelectionUI);

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                BaseActivity baseActivity = ((BaseActivity) getActivity());
                baseActivity.showAlertDialog("Error occured",error,()->{
                });
            }
        });
    }

    private void updateTabSelectionUI(boolean coreSelected) {
        int activeBg = R.drawable.bg_circle_pill;
        int white = ContextCompat.getColor(requireContext(), android.R.color.white);
        int mutedSlate = Color.parseColor("#82889E");

        if (coreSelected) {
            binding.tabCoreSkills.setBackgroundResource(activeBg);
            binding.tabCoreSkills.setTextColor(white);

            binding.tabOtherSkills.setBackground(null);
            binding.tabOtherSkills.setTextColor(mutedSlate);
        } else {
            binding.tabOtherSkills.setBackgroundResource(activeBg);
            binding.tabOtherSkills.setTextColor(white);

            binding.tabCoreSkills.setBackground(null);
            binding.tabCoreSkills.setTextColor(mutedSlate);
        }
    }

    private void updateInterviewFilterUI(int selectedIndex) {
        int activeBg = R.drawable.bg_pill_grey;
        int activeText = ContextCompat.getColor(requireContext(), android.R.color.white);
        int inactiveText = ContextCompat.getColor(requireContext(), R.color.color_grey);

        binding.tabInterviewLast3.setBackground(selectedIndex == 0 ? ContextCompat.getDrawable(requireContext(), activeBg) : null);
        binding.tabInterviewLast3.setTextColor(selectedIndex == 0 ? activeText : inactiveText);

        binding.tabInterviewLast5.setBackground(selectedIndex == 1 ? ContextCompat.getDrawable(requireContext(), activeBg) : null);
        binding.tabInterviewLast5.setTextColor(selectedIndex == 1 ? activeText : inactiveText);

        binding.tabInterviewAll.setBackground(selectedIndex == 2 ? ContextCompat.getDrawable(requireContext(), activeBg) : null);
        binding.tabInterviewAll.setTextColor(selectedIndex == 2 ? activeText : inactiveText);

        if (selectedIndex == 0) binding.pillInterviewFilter.setText("Last 3 interviews");
        else if (selectedIndex == 1) binding.pillInterviewFilter.setText("Last 5 interviews");
        else binding.pillInterviewFilter.setText("All interviews");
    }

    private void updateSkillFilterUI(int selectedIndex) {
        int activeBg = R.drawable.bg_pill_grey;
        int activeText = ContextCompat.getColor(requireContext(), android.R.color.white);
        int inactiveText = ContextCompat.getColor(requireContext(), R.color.color_grey);

        binding.tabSkillWeakest.setBackground(selectedIndex == 0 ? ContextCompat.getDrawable(requireContext(), activeBg) : null);
        binding.tabSkillWeakest.setTextColor(selectedIndex == 0 ? activeText : inactiveText);

        binding.tabSkillStrongest.setBackground(selectedIndex == 1 ? ContextCompat.getDrawable(requireContext(), activeBg) : null);
        binding.tabSkillStrongest.setTextColor(selectedIndex == 1 ? activeText : inactiveText);

        binding.tabSkillAll.setBackground(selectedIndex == 2 ? ContextCompat.getDrawable(requireContext(), activeBg) : null);
        binding.tabSkillAll.setTextColor(selectedIndex == 2 ? activeText : inactiveText);

        if (selectedIndex == 0) binding.pillSkillFilter.setText("Weakest skills");
        else if (selectedIndex == 1) binding.pillSkillFilter.setText("Strongest skills");
        else binding.pillSkillFilter.setText("All skills");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}