package com.example.capstone.repository;

import com.example.capstone.model.request.LoginRequest;
import com.example.capstone.model.request.SignupRequest;
import com.example.capstone.model.response.LoginResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.RefreshCallback;
import com.example.capstone.service.AuthService;

public interface IAuthRepository {
  void login(LoginRequest loginRequest, ApiCallback<LoginResponse> callback);

  void signup(SignupRequest signupRequest, ApiCallback<Void> callback);
  void logout(ApiCallback<Void> callback);
  void logoutAllDevices(ApiCallback<Void> callback);
  void refreshToken(RefreshCallback callback);
}
