package com.example.capstone.repository;

import com.example.capstone.model.request.ChangePasswordRequest;
import com.example.capstone.model.request.DeleteAccountRequest;
import com.example.capstone.model.request.UpdateUserRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.service.UserService;

public class UserRepository {
  private final UserService userService;
  public UserRepository(UserService userService){
    this.userService = userService;
  }

  public void changePassword(ChangePasswordRequest changePasswordRequest, ApiCallback<Void> callback){
    userService.changePassword(changePasswordRequest,callback);
  }

  public void deleteAccount(DeleteAccountRequest deleteAccountRequest, ApiCallback<Void> callback){
    userService.deleteAccount(deleteAccountRequest,callback);
  }

  public void updateAccount(UpdateUserRequest updateUserRequest, ApiCallback<Void> callback){
    userService.updateAccount(updateUserRequest,callback);
  }
}
