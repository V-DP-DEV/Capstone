package com.capstone.myapplication.network;

public interface ApiCallback {

  void onSuccess(ApiResponse response);
  void onGeneralError(Exception e);
  void onHttpError(ApiResponse response);
}
