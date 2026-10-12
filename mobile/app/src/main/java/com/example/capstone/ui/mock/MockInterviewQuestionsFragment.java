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

/**
 * MockInterviewQuestionsFragment manages the active interview session UI, including
 * question stepping, per-question timers, audio recording animation triggers,
 * and adaptive rendering for standard vs. code snippet questions.
 */
public class MockInterviewQuestionsFragment extends Fragment {

    private FragmentMockInterviewQuestionsBinding binding;
    private List<UserQuestion> questionsList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private CountDownTimer countDownTimer;
    private boolean isRecording = false;
    private String interviewTitle = "Java";

    // Standard 3-minute limit per question
    private static final int DEFAULT_QUESTION_DURATION_SECONDS = 180;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentMockInterviewQuestionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the interview title passed from the instructions fragment
        if (getArguments() != null && getArguments().containsKey(MockInterviewInstructionsFragment.ARG_INTERVIEW_NAME)) {
            interviewTitle = getArguments().getString(MockInterviewInstructionsFragment.ARG_INTERVIEW_NAME, "Java");
        }

        binding.tvHeaderTitle.setText(interviewTitle);

        setupClickListeners();

        // Load fallback sample list if no questions were injected beforehand
        if (questionsList.isEmpty()) {
            loadSampleQuestions();
        }

        if (!questionsList.isEmpty()) {
            displayQuestionAtIndex(currentQuestionIndex);
        }
    }

    /**
     * Public injection hook used by Person 2 / ViewModel to pass dynamic question sets into this fragment.
     */
    public void setQuestions(List<UserQuestion> questions) {
        this.questionsList = questions != null ? questions : new ArrayList<>();
        this.currentQuestionIndex = 0;
        if (binding != null && !questionsList.isEmpty()) {
            displayQuestionAtIndex(currentQuestionIndex);
        }
    }

    /**
     * Fallback method to populate sample data until connected toViewModel/Repository data.
     */
    private void loadSampleQuestions() {
        questionsList = new ArrayList<>();
    }

    /**
     * Renders the question at the specified index, updates progress visuals,
     * toggles code snippet visibility, resets input fields, and starts the timer.
     */
    private void displayQuestionAtIndex(int index) {
        if (index < 0 || index >= questionsList.size()) {
            finishInterview();
            return;
        }

        UserQuestion currentQuestion = questionsList.get(index);

        // Update question counter & progress bar UI
        int totalQuestions = questionsList.size();
        binding.tvQuestionProgress.setText(String.format(Locale.getDefault(), "Question %d / %d", index + 1, totalQuestions));

        int progressPercentage = (int) (((float) (index + 1) / totalQuestions) * 100);
        binding.progressBar.setProgress(progressPercentage);

        // Display category or difficulty tag
        String category = currentQuestion.getType() != null ? currentQuestion.getType() : currentQuestion.getDifficulty();
        binding.tvCategory.setText(category != null ? category.toUpperCase(Locale.getDefault()) : "TECHNICAL");

        // Dynamically toggle between standard text view and scrollable code snippet view
        if (isCodeQuestion(currentQuestion)) {
            binding.tvQuestionText.setVisibility(View.GONE);
            binding.scrollCodeSnippet.setVisibility(View.VISIBLE);
            binding.tvCodeSnippet.setText(currentQuestion.getText());
        } else {
            binding.tvQuestionText.setVisibility(View.VISIBLE);
            binding.scrollCodeSnippet.setVisibility(View.GONE);
            binding.tvQuestionText.setText(currentQuestion.getText());
        }

        // Reset text answer input and recording visual state
        binding.etAnswer.setText("");
        stopRecordingState();

        // Start countdown timer for the current question
        startTimer(DEFAULT_QUESTION_DURATION_SECONDS);
    }

    /**
     * Helper method to determine if a question should be formatted as code based on type or contents.
     */
    private boolean isCodeQuestion(UserQuestion question) {
        if (question == null) return false;

        boolean matchesCodeType = question.getType() != null &&
                (question.getType().equalsIgnoreCase("CODING") || question.getType().equalsIgnoreCase("CODE"));

        boolean hasCodeFormatting = question.getText() != null &&
                (question.getText().contains(";\n") || question.getText().contains("{\n"));

        return matchesCodeType || hasCodeFormatting;
    }

    /**
     * Starts or resets the CountDownTimer for the current question.
     */
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

    /**
     * Binds user click actions for mic recording toggles, question skipping, and continuing.
     */
    private void setupClickListeners() {
        binding.btnMic.setOnClickListener(v -> toggleRecording());
        binding.btnSkip.setOnClickListener(v -> nextQuestion());
        binding.btnContinue.setOnClickListener(v -> nextQuestion());
    }

    /**
     * Toggles mic recording state and updates visual feedback (color, text, animation).
     */
    private void toggleRecording() {
        isRecording = !isRecording;
        if (isRecording) {
            binding.tvMicStatus.setText("Recording... (Tap to stop)");
            binding.tvMicStatus.setTextColor(Color.parseColor("#E53935")); // Red accent
            binding.btnMic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E53935")));
            startMicPulseAnimation();
        } else {
            stopRecordingState();
        }
    }

    /**
     * Resets mic button and status text to their idle default states.
     */
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

    /**
     * Runs a repeating pulse scale animation on the microphone button while active.
     */
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

    /**
     * Advances to the next question or finishes the interview session if completed.
     */
    private void nextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionsList.size()) {
            displayQuestionAtIndex(currentQuestionIndex);
        } else {
            finishInterview();
        }
    }

    /**
     * Cancels active timers, packages session result metadata, and navigates to the result screen.
     */
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

    /**
     * Navigates back to the previous screen in the stack.
     */
    private void navigateBack() {
        if (getParentFragmentManager() != null && isAdded()) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel active countdown timer and nullify binding to prevent memory leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding = null;
    }
}