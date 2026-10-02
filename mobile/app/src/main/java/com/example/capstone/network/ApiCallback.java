package com.example.capstone.network;

//defines events/ interface
public interface ApiCallback<T> {

  void onSuccess(ApiResponse<T> response);
  void onGeneralError(Exception e);
  void onHttpError(ApiResponse<T> response);
}
