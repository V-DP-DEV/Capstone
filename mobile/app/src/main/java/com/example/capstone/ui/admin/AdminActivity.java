package com.example.capstone.ui.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;

import com.example.capstone.databinding.ActivityAdminBinding;

import com.example.capstone.R;

public class AdminActivity extends AppCompatActivity {

  private AppBarConfiguration appBarConfiguration;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);

    ActivityAdminBinding binding = ActivityAdminBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    setSupportActionBar(binding.toolbar.commonToolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_fragment_content_admin);

    if (navHostFragment == null) {
      return;
    }

    NavController navController = navHostFragment.getNavController();

    appBarConfiguration =
        new AppBarConfiguration.Builder(navController.getGraph())
            .build();

    navController.addOnDestinationChangedListener(
        (controller, destination, arguments) -> {
          binding.toolbar.toolbarTitle.setText(destination.getLabel());
        }
    );
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
        .findFragmentById(R.id.nav_host_fragment_content_admin);
    boolean handled = false;
    if (navHostFragment != null) {
      NavController navController = navHostFragment.getNavController();
      handled = NavigationUI.navigateUp(navController, appBarConfiguration);
    }
    return handled || super.onSupportNavigateUp();
  }
}