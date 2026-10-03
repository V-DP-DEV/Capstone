package com.example.capstone;

import android.content.Context;

import com.example.capstone.network.ApiClient;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.InterviewAttemptsRepository;
import com.example.capstone.repository.UserRepository;
import com.example.capstone.service.AuthService;
import com.example.capstone.service.InterviewAttemptsService;
import com.example.capstone.service.UserService;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppContainer {

  public SecureSession getSecureSession() {
    return secureSession;
  }

  public Gson getGson() {
    return gson;
  }

  public final AuthRepository authRepository;
  public final AuthService authService;
  public final UserRepository userRepository;
  public final UserService userService;
  public final InterviewAttemptsService interviewAttemptsService;
  public final InterviewAttemptsRepository interviewAttemptsRepository;
  public final ApiClient apiClient;
  public final PreferenceManager preferenceManager;
  public final SecureSession secureSession;
  public final Gson gson;

  public AppContainer(Context context){
    secureSession = new SecureSession(context);
    preferenceManager = new PreferenceManager(context);
    gson = new Gson();

    apiClient = new ApiClient(secureSession,gson);
    authService = new AuthService(apiClient,secureSession);
    authRepository = new AuthRepository(authService);

    apiClient.setRefreshCaller(()->authRepository.refreshToken());

    userService = new UserService(apiClient);
    userRepository = new UserRepository(userService);

    interviewAttemptsService = new InterviewAttemptsService(apiClient);
    interviewAttemptsRepository = new InterviewAttemptsRepository(interviewAttemptsService);
  }
}
