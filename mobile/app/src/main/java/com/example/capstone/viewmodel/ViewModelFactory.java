package com.example.capstone.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.function.Supplier;

public class ViewModelFactory<T extends ViewModel>
    implements ViewModelProvider.Factory {

  private final Supplier<T> creator;

  public ViewModelFactory(Supplier<T> creator) {
    this.creator = creator;
  }

  @Override
  public <VM extends ViewModel> VM create(Class<VM> modelClass) {
    return modelClass.cast(creator.get());
  }
}