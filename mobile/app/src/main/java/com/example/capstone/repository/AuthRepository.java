package com.example.capstone.repository;

import android.content.Context;

import com.example.capstone.model.request.LoginRequest;
import com.example.capstone.model.request.RefreshRequest;
import com.example.capstone.model.request.SignupRequest;
import com.example.capstone.model.response.LoginResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.service.AuthService;

public class AuthRepository {
  private final AuthService authService;

  public AuthRepository(AuthService authService){
    this.authService = authService;
  }

  public void login(
                    LoginRequest loginRequest,
                    ApiCallback<LoginResponse> callback){
    authService.login(loginRequest,callback);
  }

  public void signup(
      SignupRequest signupRequest,
      ApiCallback<Void> callback){
    authService.signup(signupRequest,callback);
  }

  public void logout(
      ApiCallback<Void> callback){
    authService.logout(callback);
  }

  public void logoutAllDevices(ApiCallback<Void> callback){
    authService.logoutAllDevices(callback);
  }

  public void refreshToken(){
    authService.refresh();
  }
}
