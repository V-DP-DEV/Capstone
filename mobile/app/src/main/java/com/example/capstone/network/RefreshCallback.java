package com.example.capstone.network;

public interface RefreshCallback {
  void onSuccess();
  void onError(RefreshError error);
}
