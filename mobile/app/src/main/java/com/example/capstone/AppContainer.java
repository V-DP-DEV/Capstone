package com.example.capstone;

import android.content.Context;

import com.example.capstone.network.ApiClient;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.service.AuthService;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppContainer {
  public final AuthRepository authRepository;
  public final AuthService authService;
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
  }
}
