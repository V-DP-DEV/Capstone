package com.example.capstone.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.capstone.MyApplication;
import com.example.capstone.R;
import com.example.capstone.domainModels.User;
import com.example.capstone.domainModels.UserRole;
import com.example.capstone.repository.UserRepository;
import com.example.capstone.ui.admin.AdminActivity;
import com.example.capstone.ui.admin.AdminDashboardFragment;
import com.example.capstone.ui.auth.AuthActivity;
import com.example.capstone.ui.user.UserActivity;
import com.example.capstone.ui.user.UserDashboardFragment;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;

public abstract class BaseActivity extends AppCompatActivity {

  protected SecureSession secureSession;
  protected PreferenceManager manager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(new android.widget.FrameLayout(this));
    MyApplication app =
        (MyApplication) getApplication();

    secureSession =
        app.getAppContainer().getSecureSession();

    manager = app.getAppContainer().preferenceManager;



    secureSession.getLoggedIn()
        .observe(this, expired -> {

          if (Boolean.FALSE.equals(expired)) {
            redirectToLogin();
          }

        });
  }

  private void redirectToLogin() {

    secureSession.clear();

    Intent intent = new Intent(
        this,
        AuthActivity.class
    );

    intent.setFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK |
            Intent.FLAG_ACTIVITY_CLEAR_TASK
    );

    startActivity(intent);
    //clears the backstack so the user cant go back
    finish();
  }

  // Keep a reference to the loading dialog so we can dismiss it later
  private AlertDialog loadingDialog;

  // No event
  public void showAlertDialog(String title, String message) {
    showAlertDialog(title, message, null); // Pass null as the event
  }

  // with event
  public void showAlertDialog(String title, String message, Runnable onOkClicked) {
    new AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK", (dialog, which) -> {
          dialog.dismiss();
          // Only run the event if something was actually passed in
          if (onOkClicked != null) {
            onOkClicked.run();
          }
        })
        .setCancelable(true)
        .show();
  }
  /**
   * Show a non-dismissible loading popup with a spinner
   */
  public void showLoadingDialog(String message) {
    // Build a simple layout programmatically to avoid needing an extra XML file
    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.HORIZONTAL);
    layout.setPadding(50, 50, 50, 50);
    layout.setGravity(Gravity.CENTER_VERTICAL);

    ProgressBar progressBar = new ProgressBar(this);
    progressBar.setIndeterminate(true);
    layout.addView(progressBar);

    TextView textView = new TextView(this);
    textView.setText(message);
    textView.setPadding(40, 0, 0, 0);
    layout.addView(textView);

    // Initialize and show the dialog
    loadingDialog = new AlertDialog.Builder(this)
        .setView(layout)
        .setCancelable(false) // Prevents the user from tapping away while loading
        .create();

    loadingDialog.show();
  }

  /**
   * Dismiss the loading spinner when the background task finishes
   */
  public void hideLoadingDialog() {
    if (loadingDialog != null && loadingDialog.isShowing()) {
      loadingDialog.dismiss();
    }
  }

  /**
   * Navigates to home screen clearing previous fragments and history
   */
  public void navigateToHome() {
    UserRole userRole = manager.getRole();

    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
      try {
        //Wipe out fragment backstack history cleanly
        getSupportFragmentManager().popBackStack(
            null,
            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
        );

        //Clear any lingering active fragments from memory
        for (androidx.fragment.app.Fragment fragment : getSupportFragmentManager().getFragments()) {
          if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();
          }
        }

        //Process the changes immediately now that the lock is lifted
        getSupportFragmentManager().executePendingTransactions();

      } catch (Exception e) {
        e.printStackTrace(); // Catch any minor state friction silently
      }

      // Cleanly recreate the Activity fresh!
      recreate();
    });
  }
}