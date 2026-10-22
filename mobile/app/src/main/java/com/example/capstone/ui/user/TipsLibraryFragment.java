package com.example.capstone.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.ui.components.BottomNavHelper;
import com.example.capstone.util.BottomNavUtils;

public class TipsLibraryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tips_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Header Back Button Navigation
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (isAdded()) {
                    NavHostFragment.findNavController(this).navigateUp();
                }
            });
        }

        // Setup Bottom Navigation Bar Actions
        View bottomBar = view.findViewById(R.id.bottom_nav_container);
        if (bottomBar != null) {
            BottomNavUtils.setupBottomNavigation(bottomBar);
        }
    }

    }