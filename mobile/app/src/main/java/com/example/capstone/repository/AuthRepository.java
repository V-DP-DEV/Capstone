package com.example.capstone.repository;

import android.content.Context;

import com.example.capstone.model.request.LoginRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.service.AuthService;

public class AuthRepository {
  private final AuthService authService;

  public AuthRepository(AuthService authService){
    this.authService = authService;
  }

  public void login(
                    LoginRequest loginRequest,
                    ApiCallback callback){
    authService.login(loginRequest,callback);
  }
}
