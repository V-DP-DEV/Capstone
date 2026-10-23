
package com.example.capstone;

import android.content.Context;

import com.example.capstone.network.ApiClient;

import com.example.capstone.repository.AnalyticsRepository;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.IAnalyticsRepository;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.repository.IInterviewAttemptsRepository;
import com.example.capstone.repository.IInterviewRepository;
import com.example.capstone.repository.IUserRepository;
import com.example.capstone.repository.InterviewAttemptsRepository;
import com.example.capstone.repository.InterviewRepository;
import com.example.capstone.repository.UserRepository;
import com.example.capstone.service.AnalyticsService;
import com.example.capstone.service.AuthService;
import com.example.capstone.service.InterviewAttemptsService;
import com.example.capstone.service.InterviewService;
import com.example.capstone.service.UserService;
import com.example.capstone.util.FlexibleBooleanAdapter;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;
import com.example.capstone.viewmodel.ProfileViewModel;
import com.example.capstone.viewmodel.SetupViewModel;
import com.example.capstone.viewmodel.SettingsViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AppContainer {

  public final IAuthRepository authRepository;
  public final AuthService authService;

  public final IUserRepository userRepository;
  public final UserService userService;

  public final InterviewAttemptsService interviewAttemptsService;
  public final IInterviewAttemptsRepository interviewAttemptsRepository;

  public final ApiClient apiClient;
  public final PreferenceManager preferenceManager;
  public final SecureSession secureSession;

  public final IInterviewRepository interviewRepository;
  public final InterviewService interviewService;
  public final AnalyticsService analyticsService;
  public final IAnalyticsRepository analyticsRepository;

  public final Gson gson;

  public AppContainer(Context context) {
    secureSession = new SecureSession(context);
    preferenceManager = new PreferenceManager(context);

    gson = new GsonBuilder()
            .registerTypeAdapter(boolean.class, new FlexibleBooleanAdapter())
            .registerTypeAdapter(Boolean.class, new FlexibleBooleanAdapter())
            .create();

    apiClient = new ApiClient(secureSession, gson);

    authService = new AuthService(
            apiClient,
            secureSession,
            preferenceManager
    );

    authRepository = new AuthRepository(authService);

    apiClient.setRefreshCaller(
            refreshCallback ->
                    authRepository.refreshToken(refreshCallback)
    );

    userService = new UserService(apiClient);
    userRepository = new UserRepository(userService);

    interviewAttemptsService =
            new InterviewAttemptsService(apiClient);

    interviewAttemptsRepository =
            new InterviewAttemptsRepository(
                    interviewAttemptsService
            );

    interviewService = new InterviewService(apiClient);
    interviewRepository = new InterviewRepository(interviewService);

    analyticsService = new AnalyticsService(apiClient);
    analyticsRepository = new AnalyticsRepository(analyticsService);
  }

  public SecureSession getSecureSession() {
    return secureSession;
  }

  public SettingsViewModel createSettingsViewModel() {
    return new SettingsViewModel(authRepository, userRepository);
  }

  public ProfileViewModel createProfileViewModel() {
    return new ProfileViewModel(userRepository);
  }
}
