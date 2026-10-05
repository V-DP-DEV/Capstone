package com.example.capstone.model.response;

public class RefreshResponse {
  private String token;
  private String refreshToken;
  private long tokenExpiresAt;
  private long refreshTokenExpiresAt;
  private String userRole;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public long getTokenExpiresAt() {
    return tokenExpiresAt;
  }

  public void setTokenExpiresAt(long tokenExpiresAt) {
    this.tokenExpiresAt = tokenExpiresAt;
  }


  public long getRefreshTokenExpiresAt() {
    return refreshTokenExpiresAt;
  }

  public void setRefreshTokenExpiresAt(long refreshTokenExpiresAt) {
    this.refreshTokenExpiresAt = refreshTokenExpiresAt;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }
}
