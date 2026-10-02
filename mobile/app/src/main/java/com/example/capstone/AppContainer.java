package com.example.capstone;

import android.content.Context;

import com.example.capstone.network.ApiClient;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.service.AuthService;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;

public class AppContainer {
  public final AuthRepository authRepository;
  public final AuthService authService;
  public final ApiClient apiClient;
  public final PreferenceManager preferenceManager;
  public final SecureSession secureSession;

  public AppContainer(Context context){
    secureSession = new SecureSession(context);
    preferenceManager = new PreferenceManager(context);

    apiClient = new ApiClient(secureSession);
    authService = new AuthService(apiClient,secureSession);
    authRepository = new AuthRepository(authService);
  }
}
