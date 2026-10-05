package com.example.capstone.repository;

import com.example.capstone.model.request.ChangePasswordRequest;
import com.example.capstone.model.request.DeleteAccountRequest;
import com.example.capstone.model.request.UpdateUserRequest;
import com.example.capstone.network.ApiCallback;

public interface IUserRepository {
  void changePassword(ChangePasswordRequest changePasswordRequest, ApiCallback<Void> callback);

  void deleteAccount(DeleteAccountRequest deleteAccountRequest, ApiCallback<Void> callback);

  void updateAccount(UpdateUserRequest updateUserRequest, ApiCallback<Void> callback);
}
