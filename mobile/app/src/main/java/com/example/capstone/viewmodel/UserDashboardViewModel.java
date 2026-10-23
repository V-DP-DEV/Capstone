package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.UserDashboardMetrics;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IAnalyticsRepository;
/**
 * ViewModel responsible for managing user dashboard analytics state, including fetching performance metrics,
 * loading indicators, error handling, and repository communication in the MVVM architecture.
 */
public class UserDashboardViewModel extends ViewModel {

    private final IAnalyticsRepository analyticsRepository;

    private final MutableLiveData<UserDashboardMetrics> dashboardMetrics = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public UserDashboardViewModel(IAnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
        fetchDashboardData();
    }

    // Observable state getters
    public LiveData<UserDashboardMetrics> getDashboardMetrics() {
        return dashboardMetrics;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchDashboardData() {
        isLoading.setValue(true);
        error.setValue(null);

        analyticsRepository.getUserDashboard(new ApiCallback<UserDashboardMetrics>() {
            @Override
            public void onSuccess(ApiResponse<UserDashboardMetrics> response) {
                isLoading.setValue(false);
                UserDashboardMetrics metrics = response.getData();

                if (metrics == null) {
                    error.setValue("Dashboard metrics not found.");
                    return;
                }

                dashboardMetrics.setValue(metrics);
            }

            @Override
            public void onGeneralError(Exception e) {
                isLoading.setValue(false);
                error.setValue(
                        e.getMessage() != null
                                ? e.getMessage()
                                : "Failed to load dashboard data."
                );
            }

            @Override
            public void onHttpError(ApiResponse<UserDashboardMetrics> response) {
                isLoading.setValue(false);
                if (response.getError() != null && response.getError().getMessage() != null) {
                    error.setValue(response.getError().getMessage());
                } else {
                    error.setValue("Failed to load dashboard data (HTTP error).");
                }
            }
        });
    }

    public void clearViewModel() {
        dashboardMetrics.setValue(null);
        error.setValue(null);
        isLoading.setValue(false);
    }
}