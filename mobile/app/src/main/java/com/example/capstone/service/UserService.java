package com.example.capstone.service;

import com.example.capstone.model.request.ChangePasswordRequest;
import com.example.capstone.model.request.DeleteAccountRequest;
import com.example.capstone.model.request.UpdateUserRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.util.SecureSession;

public class UserService {
  private final ApiClient apiClient;
  public UserService(ApiClient apiClient){
    this.apiClient = apiClient;
  }

  public void changePassword(ChangePasswordRequest changePassowrdRequest, ApiCallback<Void> callback){

  }
  public void deleteAccount(DeleteAccountRequest deleteAccountRequest, ApiCallback<Void> callback){

  }
  public void updateAccount(UpdateUserRequest updateUserRequest, ApiCallback<Void> callback){

  }
}
