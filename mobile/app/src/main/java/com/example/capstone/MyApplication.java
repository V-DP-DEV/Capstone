package com.example.capstone;

import android.app.Application;

public class MyApplication extends Application {
  private AppContainer appContainer;

  @Override
  public void onCreate() {
    super.onCreate();

    appContainer = new AppContainer(this);
  }

  public AppContainer getAppContainer() {
    return appContainer;
  }
}
