package com.example.capstone.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.capstone.network.ApiClient;
import com.example.capstone.repository.IInterviewAttemptsRepository;
import com.example.capstone.repository.InterviewAttemptsRepository;
import com.example.capstone.service.InterviewAttemptsService;
import com.example.capstone.util.SecureSession;
import com.google.gson.Gson;

/**
 * Factory responsible for constructing the {@link HistoryViewModel} and its
 * underlying data dependencies away from the UI layer.
 */
public class HistoryViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public HistoryViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HistoryViewModel.class)) {
            // Build the data/network layer using ApiClient's actual constructor signature
            SecureSession secureSession = new SecureSession(context);
            Gson gson = new Gson();
            ApiClient apiClient = new ApiClient(secureSession, gson);

            InterviewAttemptsService service = new InterviewAttemptsService(apiClient);
            IInterviewAttemptsRepository repository = new InterviewAttemptsRepository(service);

            return (T) new HistoryViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}