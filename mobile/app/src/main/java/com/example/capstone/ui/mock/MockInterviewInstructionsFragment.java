package com.example.capstone.ui.mock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.capstone.R;

public class MockInterviewInstructionsFragment extends Fragment {
    public static final String ARG_INTERVIEW_NAME = "arg_interviewId";
    public static final String ARG_INTERVIEW_DIFFICULTY = "arg_interview_Difficulty";
    public static final String ARG_DURATION = "arg_duration";
    public static final String ARG_INPUT_TYPE = "arg_input_type";
    public static final String ARG_QUESTION_COUNT = "arg_question_count";

    private TextView tvInterviewName;
    private TextView tvInterviewDifficulty;
    private TextView tvStatQuestionsVal;
    private TextView tvStatDurationVal;
    private TextView tvStatInputVal;

    private AppCompatButton btnBack;
    private AppCompatButton btnStart;

    public MockInterviewInstructionsFragment() {

    }

    public static MockInterviewInstructionsFragment newInstance(String name, String difficulty, int questions, String duration, String inputType) {
        MockInterviewInstructionsFragment fragment = new MockInterviewInstructionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_INTERVIEW_NAME, name);
        args.putString(ARG_INTERVIEW_DIFFICULTY, difficulty);
        args.putInt(ARG_QUESTION_COUNT, questions);
        args.putString(ARG_DURATION, duration);
        args.putString(ARG_INPUT_TYPE, inputType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mock_interview_instructions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        displayInterviewDetails();
        setupClickListeners();
    }
private void initViews(View view) {
    tvInterviewName = view.findViewById(R.id.tvInterviewName);
    tvInterviewDifficulty = view.findViewById(R.id.tvInterviewDifficulty);
    tvStatQuestionsVal = view.findViewById(R.id.tvStatQuestionsVal);
    tvStatDurationVal = view.findViewById(R.id.tvStatDurationVal);
    tvStatInputVal = view.findViewById(R.id.tvStatInputVal);
    btnBack = view.findViewById(R.id.btnBack);
    btnStart = view.findViewById(R.id.btnStart);

}
private void displayInterviewDetails() {
    if (getArguments() != null) {
        String name = getArguments().getString(ARG_INTERVIEW_NAME, "Java");
        String difficulty = getArguments().getString(ARG_INTERVIEW_DIFFICULTY, "Hard");
        int questions = getArguments().getInt(ARG_QUESTION_COUNT, 8);
        String duration = getArguments().getString(ARG_DURATION, "~25m");
        String inputType = getArguments().getString(ARG_INPUT_TYPE, "Mic");

        tvInterviewName.setText(name);
        tvInterviewDifficulty.setText(difficulty);
        tvStatQuestionsVal.setText(String.valueOf(questions));
        tvStatDurationVal.setText(duration);
        tvStatInputVal.setText(inputType);
    }
}
private void setupClickListeners() {
    View.OnClickListener backListener = v -> popBackStackOrFinish();

    btnBack.setOnClickListener(backListener);

    btnStart.setOnClickListener(v -> startInterview());
}
    private void popBackStackOrFinish() {
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        } else if (getActivity() != null) {
            getActivity().getOnBackPressedDispatcher().onBackPressed();
        }
    }
    private void startInterview() {

    }
}





