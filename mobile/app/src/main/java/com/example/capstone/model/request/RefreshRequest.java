package com.example.capstone.model.request;

public class RefreshRequest {
  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  private String refreshToken;

}
