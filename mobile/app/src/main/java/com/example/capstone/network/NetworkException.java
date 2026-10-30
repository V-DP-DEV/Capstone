package com.example.capstone.network;

import java.io.IOException;

public class NetworkException extends IOException {
  public NetworkException(Throwable cause) {
    super("Could not connect to the server. Please check your internet connection.", cause);
  }
}
