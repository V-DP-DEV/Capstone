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
import com.example.capstone.domainModels.UserQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MockInterviewQuestionsFragment extends Fragment {

    private FragmentMockInterviewQuestionsBinding binding;
    private List<UserQuestion> questionsList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private CountDownTimer countDownTimer;
    private boolean isRecording = false;
    private String interviewTitle = "Java";

    private static final int DEFAULT_QUESTION_DURATION_SECONDS = 180; // 3 minutes per question

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMockInterviewQuestionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(MockInterviewInstructionsFragment.ARG_INTERVIEW_NAME)) {
            interviewTitle = getArguments().getString(MockInterviewInstructionsFragment.ARG_INTERVIEW_NAME, "Java");
        }

        binding.tvHeaderTitle.setText(interviewTitle);

        setupClickListeners();

        // If questions weren't passed in, load initial sample set matching UserQuestion model
        if (questionsList.isEmpty()) {
            loadSampleQuestions();
        }

        if (!questionsList.isEmpty()) {
            displayQuestionAtIndex(currentQuestionIndex);
        }
    }

    /**
     * Set questions dynamically from a ViewModel, Repository, or Bundle argument.
     */
    public void setQuestions(List<UserQuestion> questions) {
        this.questionsList = questions != null ? questions : new ArrayList<>();
        this.currentQuestionIndex = 0;
        if (binding != null && !questionsList.isEmpty()) {
            displayQuestionAtIndex(currentQuestionIndex);
        }
    }

    private void loadSampleQuestions() {
        // Mock fallback list using your domain model structure
        // Note: Replace this with database/ViewModel data when connected
        questionsList = new ArrayList<>();
    }

    private void displayQuestionAtIndex(int index) {
        if (index < 0 || index >= questionsList.size()) {
            finishInterview();
            return;
        }

        UserQuestion currentQuestion = questionsList.get(index);

        // Update question counter & progress bar
        int totalQuestions = questionsList.size();
        binding.tvQuestionProgress.setText(String.format(Locale.getDefault(), "Question %d / %d", index + 1, totalQuestions));

        int progressPercentage = (int) (((float) (index + 1) / totalQuestions) * 100);
        binding.progressBar.setProgress(progressPercentage);

        // Category / Type display
        String category = currentQuestion.getType() != null ? currentQuestion.getType() : currentQuestion.getDifficulty();
        binding.tvCategory.setText(category != null ? category.toUpperCase(Locale.getDefault()) : "TECHNICAL");

        // Code snippet check based on UserQuestion properties
        if (isCodeQuestion(currentQuestion)) {
            binding.tvQuestionText.setVisibility(View.GONE);
            binding.scrollCodeSnippet.setVisibility(View.VISIBLE);
            binding.tvCodeSnippet.setText(currentQuestion.getText());
        } else {
            binding.tvQuestionText.setVisibility(View.VISIBLE);
            binding.scrollCodeSnippet.setVisibility(View.GONE);
            binding.tvQuestionText.setText(currentQuestion.getText());
        }

        // Reset inputs and recording state
        binding.etAnswer.setText("");
        stopRecordingState();

        // Start per-question timer
        startTimer(DEFAULT_QUESTION_DURATION_SECONDS);
    }

    /**
     * Determines whether a question contains code based on its type or text formatting.
     */
    private boolean isCodeQuestion(UserQuestion question) {
        if (question == null) return false;

        boolean matchesCodeType = question.getType() != null &&
                (question.getType().equalsIgnoreCase("CODING") || question.getType().equalsIgnoreCase("CODE"));

        boolean hasCodeFormatting = question.getText() != null &&
                (question.getText().contains(";\n") || question.getText().contains("{\n"));

        return matchesCodeType || hasCodeFormatting;
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
                if (binding != null) {
                    binding.tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, secs));
                }
            }

            @Override
            public void onFinish() {
                if (binding != null) {
                    binding.tvTimer.setText("00:00");
                }
                if (getContext() != null) {
                    Toast.makeText(requireContext(), "Time's up for this question!", Toast.LENGTH_SHORT).show();
                }
                nextQuestion();
            }
        }.start();
    }

    private void setupClickListeners() {
        binding.btnMic.setOnClickListener(v -> toggleRecording());
        binding.btnSkip.setOnClickListener(v -> nextQuestion());
        binding.btnContinue.setOnClickListener(v -> nextQuestion());
    }

    private void toggleRecording() {
        isRecording = !isRecording;
        if (isRecording) {
            binding.tvMicStatus.setText("Recording... (Tap to stop)");
            binding.tvMicStatus.setTextColor(Color.parseColor("#E53935")); // Red
            binding.btnMic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E53935")));
            startMicPulseAnimation();
        } else {
            stopRecordingState();
        }
    }

    private void stopRecordingStatus() {
        isRecording = false;
        if (binding != null) {
            binding.tvMicStatus.setText("Tap to record");
            binding.tvMicStatus.setTextColor(Color.parseColor("#B3B3C0"));
            binding.btnMic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A14078")));
            binding.btnMic.setScaleX(1.0f);
            binding.btnMic.setScaleY(1.0f);
        }
    }

    private void stopRecordingState() {
        stopRecordingStatus();
    }

    private void startMicPulseAnimation() {
        if (!isRecording || binding == null) return;

        binding.btnMic.animate()
                .scaleX(1.15f)
                .scaleY(1.15f)
                .setDuration(600)
                .withEndAction(() -> {
                    if (isRecording && binding != null) {
                        binding.btnMic.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(600)
                                .withEndAction(this::startMicPulseAnimation)
                                .start();
                    } else if (binding != null) {
                        binding.btnMic.setScaleX(1.0f);
                        binding.btnMic.setScaleY(1.0f);
                    }
                }).start();
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
        args.putString(MockInterviewResult.ARG_INTERVIEW_TITLE, interviewTitle);
        args.putInt(MockInterviewResult.ARG_ANSWERED_COUNT, currentQuestionIndex);
        args.putInt(MockInterviewResult.ARG_TOTAL_QUESTIONS, questionsList.size());

        if (getParentFragmentManager() != null && isAdded()) {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_mockInterviewQuestions_to_mockInterviewResult, args);
        }
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