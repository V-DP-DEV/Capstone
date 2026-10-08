package com.example.capstone.ui.mock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;


public class MockInterviewResult extends Fragment {

    public static final String ARG_INTERVIEW_TITLE = "arg_interview_title";
    public static final String ARG_DIFFICULTY = "arg_difficulty";
    public static final String ARG_ANSWERED_COUNT = "arg_answered_count";
    public static final String ARG_TOTAL_QUESTIONS = "arg_total_questions";
    public static final String ARG_DURATION = "arg_duration";



    public static MockInterviewResult newInstance(
            String title, String difficulty, int answered, int total, String duration) {

        MockInterviewResult fragment = new MockInterviewResult();
        Bundle args = new Bundle();
        args.putString(ARG_INTERVIEW_TITLE, title);
        args.putString(ARG_DIFFICULTY, difficulty);
        args.putInt(ARG_ANSWERED_COUNT, answered);
        args.putInt(ARG_TOTAL_QUESTIONS, total);
        args.putString(ARG_DURATION, duration);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mock_interview_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displaySessionDetails();
        setupClickListeners();
    }

    private void displaySessionDetails() {
        if (getArguments() != null) {
            String title = getArguments().getString(ARG_INTERVIEW_TITLE, "C# Developer");
            String difficulty = getArguments().getString(ARG_DIFFICULTY, "Hard");
            int answered = getArguments().getInt(ARG_ANSWERED_COUNT, 8);
            int total = getArguments().getInt(ARG_TOTAL_QUESTIONS, 8);
            String duration = getArguments().getString(ARG_DURATION, "22m");


        }
    }

    private void setupClickListeners() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
