package com.example.capstone.network;

//defines events/ interface
public interface ApiCallback {

  void onSuccess(ApiResponse response);
  void onGeneralError(Exception e);
  void onHttpError(ApiResponse response);
}
