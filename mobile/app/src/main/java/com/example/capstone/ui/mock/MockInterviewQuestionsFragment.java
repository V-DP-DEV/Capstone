package com.example.capstone.ui.mock;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentMockInterviewQuestionsBinding;
import com.example.capstone.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MockInterviewQuestionsFragment extends Fragment {
    private FragmentMockInterviewQuestionsBinding binding;
    private List<Question> questionsList;
    private int currentQuestionIndex = 0;

    private CountDownTimer countDownTimer;
    private boolean isRecording = false;
    private String interviewTitle = "Java";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMockInterviewQuestionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(MockInterviewInstructionsFragment.ARG_INTERVIEW_NAME)) {
                interviewTitle = getArguments().getString(MockInterviewInstructionsFragment.ARG_INTERVIEW_NAME);
            }
        }
        binding.tvHeaderTitle.setText(interviewTitle);
        loadSampleQuestions();
        setupClickListeners();
        displayQuestionAtIndex(currentQuestionIndex);
    }

    private void loadSampleQuestions() {
        questionsList = new ArrayList<>();
        questionsList.add(new Question("1", "TECHNICAL",
                "How would you optimise a SQL query that joins three large tables and runs slowly in production?", null, 167));
        questionsList.add(new Question("2", "CODING",
                "What is the output of the following C# code block and how would you rewrite it safely?",
                "string name = null;\nint length = name.Length;", 180));
        questionsList.add(new Question("3", "SYSTEM DESIGN",
                "Explain the difference between synchronous and asynchronous processing in backend API endpoints.", null, 200));
        questionsList.add(new Question("4", "BEHAVIORAL",
                "Describe a situation where you had to refactor legacy code under tight deadlines.", null, 120));
    }

    private void displayQuestionAtIndex(int index) {
        if (index < 0 || index >= questionsList.size()) {
            finishInterview();
            return;
        }

        Question currentQuestion = questionsList.get(index);


        int totalQuestions = questionsList.size();
        binding.tvQuestionProgress.setText(String.format(Locale.getDefault(), "Question %d / %d", index + 1, totalQuestions));

        int progressPercentage = (int) (((float) (index + 1) / totalQuestions) * 100);
        binding.progressBar.setProgress(progressPercentage);


        binding.tvCategory.setText(currentQuestion.getCategory().toUpperCase(Locale.getDefault()));


        if (currentQuestion.hasCodeSnippet()) {
            binding.tvQuestionText.setVisibility(View.GONE);
            binding.scrollCodeSnippet.setVisibility(View.VISIBLE);
            binding.tvCodeSnippet.setText(currentQuestion.getCodeSnippet());
        } else {
            binding.tvQuestionText.setVisibility(View.VISIBLE);
            binding.scrollCodeSnippet.setVisibility(View.GONE);
            binding.tvQuestionText.setText(currentQuestion.getQuestionText());
        }


        binding.etAnswer.setText("");
        stopRecordingState();


        startTimer(currentQuestion.getDurationInSeconds());
    }

    private void startTimer(int seconds) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(seconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long secs = (millisUntilFinished / 1000) % 60;
                binding.tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, secs));
            }

            @Override
            public void onFinish() {
                binding.tvTimer.setText("00:00");
                Toast.makeText(requireContext(), "Time's up for this question!", Toast.LENGTH_SHORT).show();
                nextQuestion();
            }
        }.start();
    }

    private void setupClickListeners() {
        binding.btnMic.setOnClickListener(v -> toggleRecording());
        binding.btnSkip.setOnClickListener(v -> nextQuestion());
        binding.btnContinue.setOnClickListener(v -> {
            String answer = binding.etAnswer.getText().toString().trim();
            // Perform answer saving logic here if needed
            nextQuestion();
        });
    }

    private void toggleRecording() {
        isRecording = !isRecording;
        if (isRecording) {
            binding.tvMicStatus.setText("Recording... (Tap to stop)");
            binding.tvMicStatus.setTextColor(Color.parseColor("#E53935")); // Red for recording
            binding.btnMic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E53935")));
        } else {
            stopRecordingState();
        }
    }

    private void stopRecordingStatus() {
        isRecording = false;
        binding.tvMicStatus.setText("Tap to record");
        binding.tvMicStatus.setTextColor(Color.parseColor("#B3B3C0"));
        binding.btnMic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A14078")));
    }

    // alias method for compatibility if called elsewhere as stopRecordingState
    private void stopRecordingState() {
        stopRecordingStatus();
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionsList.size()) {
            displayQuestionAtIndex(currentQuestionIndex);
        } else {
            finishInterview();
        }
    }

    private void finishInterview() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }


        Bundle args = new Bundle();
        args.putString("INTERVIEW_TITLE", interviewTitle);
        args.putInt("TOTAL_QUESTIONS", questionsList.size());


        // Navigate to the Completion / Result Fragment
        if (getParentFragmentManager() != null && isAdded()) {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_mockInterviewQuestions_to_mockInterviewResult, args);
        }
    }
    private void startMicPulseAnimation() {
        binding.btnMic.animate()
                .scaleX(1.15f)
                .scaleY(1.15f)
                .setDuration(600)
                .withEndAction(() -> {
                    if (isRecording) {
                        binding.btnMic.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(600)
                                .withEndAction(this::startMicPulseAnimation)
                                .start();
                    } else {
                        binding.btnMic.setScaleX(1.0f);
                        binding.btnMic.setScaleY(1.0f);
                    }
                }).start();
    }


    private void navigateBack() {
        if (getParentFragmentManager() != null && isAdded()) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding = null;
    }
}
