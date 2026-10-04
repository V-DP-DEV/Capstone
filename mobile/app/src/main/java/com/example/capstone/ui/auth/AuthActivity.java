package com.example.capstone.ui.auth;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.model.request.LoginRequest;
import com.example.capstone.model.request.SignupRequest;
import com.example.capstone.model.response.LoginResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.service.AuthService;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;
import com.example.capstone.databinding.ActivityAuthBinding;

import com.example.capstone.R;

public class AuthActivity extends AppCompatActivity {
    private AppContainer appContainer;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

     ActivityAuthBinding binding = ActivityAuthBinding.inflate(getLayoutInflater());
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
                .findFragmentById(R.id.nav_host_fragment_content_auth);

      if (navHostFragment == null) {
        return;
      }

      MyApplication app = (MyApplication) getApplication();
      AppContainer container =
          app.getAppContainer();

      IAuthRepository repo = container.authRepository;
      SignupRequest request = new SignupRequest();
      request.setEmail("vian@gmail.com");
      request.setEmail("Password123");
      request.setFirstname("ge");
      request.setSurname("ge");

      LoginRequest loginRequest = new LoginRequest();
      loginRequest.setEmail("admin@example.com");
      loginRequest.setPassword("Password123");
      loginRequest.setDeviceId();
      loginRequest.setDeviceName();

      repo.signup(request, new ApiCallback<Void>() {
        @Override
        public void onSuccess(ApiResponse<Void> response) {

        }

        @Override
        public void onGeneralError(Exception e) {

        }

        @Override
        public void onHttpError(ApiResponse<Void> response) {

        }
      });

      repo.login(
          loginRequest,
          new ApiCallback<LoginResponse>() {

            @Override
            public void onSuccess(ApiResponse<LoginResponse> response) {
              // Login worked
              // Move to the next Activity
              LoginResponse loginResponse= response.getData();
              System.out.println(loginResponse.getToken());
            }

            @Override
            public void onGeneralError(Exception e) {
            }

            @Override
            public void onHttpError(ApiResponse<LoginResponse> response) {

            }
          }
      );

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
                .findFragmentById(R.id.nav_host_fragment_content_auth);
        boolean handled = false;
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            handled = NavigationUI.navigateUp(navController, appBarConfiguration);
        }
        return handled || super.onSupportNavigateUp();
    }
}