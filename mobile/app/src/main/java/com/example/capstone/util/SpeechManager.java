package com.example.capstone.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechManager {

  // Custom interface to pass text back to your Activity/Fragment
  public interface SpeechCallback {
    void onSpeechResult(String textChunk);
    void onError(int errorCode, String errorMessage);
  }

  private final SpeechRecognizer speechRecognizer;
  private final Intent speechIntent;
  private final SpeechCallback callback;
  private boolean isListening = false;
  private final Handler mainHandler = new Handler(Looper.getMainLooper());

  public SpeechManager(Context context, SpeechCallback callback) {
    this.callback = callback;

    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
    speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    speechIntent.putExtra(RecognizerIntent.EXTRA_SEGMENTED_SESSION, true);
    setupListener();
  }

  private void setupListener() {
    speechRecognizer.setRecognitionListener(new RecognitionListener() {
      @Override
      public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        //if something was transcribed send to custom callback
        if (matches != null && !matches.isEmpty() && callback != null) {
          callback.onSpeechResult(matches.get(0));
        }

        //  Keep looping if the user hasn't explicitly stopped it
        if (isListening) {
          speechRecognizer.startListening(speechIntent);
        }
      }

      @Override
      public void onError(int error) {
        // If it's just a silence timeout, silently restart the loop
        if (error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
          safeRestart();
        } else if (callback != null) {
          // Send actual technical errors (like no network or mic blocked) to the UI
          callback.onError(error, "Error code: " + error);
        }
      }

      // Unused mandatory overrides
      @Override public void onReadyForSpeech(Bundle params) {}
      @Override public void onBeginningOfSpeech() {}
      @Override public void onRmsChanged(float rmsdB) {}
      @Override public void onBufferReceived(byte[] buffer) {}
      @Override public void onEndOfSpeech() {}
      @Override public void onPartialResults(Bundle partialResults) {}
      @Override public void onEvent(int eventType, Bundle params) {}
    });
  }

  private void safeRestart() {
    System.out.println("Safely trying to restart");
    if (isListening) {
      mainHandler.postDelayed(() -> {
        // Double check user didn't hit stop during the timeout window
        if (isListening) {
          try {
            speechRecognizer.cancel(); // Clears any frozen mic allocations
            speechRecognizer.startListening(speechIntent);
          } catch (Exception e) {
            // Fail-safe to catch underlying native driver exceptions
          }
        }
      }, 50); // delay to clean up
    }
  }

  public void startListening() {
    isListening = true;
    speechRecognizer.startListening(speechIntent);
  }

  public void stopListening() {
    isListening = false;
    speechRecognizer.stopListening();
    mainHandler.removeCallbacksAndMessages(null);
  }

  public void destroy() {
    isListening = false;
    mainHandler.removeCallbacksAndMessages(null);
    if (speechRecognizer != null) {
      speechRecognizer.cancel();
      speechRecognizer.destroy();
    }
  }
}
