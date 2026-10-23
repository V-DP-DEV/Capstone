package com.example.capstone.viewmodel;
/**
 * ViewModel responsible for managing completed interview attempt review sessions,
 * question-by-question result navigation, AI analysis triggering, and repository communication in the MVVM architecture.
 */
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.model.request.DeleteAccountRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.repository.IUserRepository;

public class SettingsViewModel extends ViewModel {

    private final IAuthRepository authRepository;
    private final IUserRepository userRepository;

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> generalError =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> logoutSuccess =
            new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> logoutAllDevicesSuccess =
            new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> deleteAccountSuccess =
            new MutableLiveData<>(false);

    public SettingsViewModel(
            IAuthRepository authRepository,
            IUserRepository userRepository
    ) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getGeneralError() {
        return generalError;
    }

    public LiveData<Boolean> getLogoutSuccess() {
        return logoutSuccess;
    }

    public LiveData<Boolean> getLogoutAllDevicesSuccess() {
        return logoutAllDevicesSuccess;
    }

    public LiveData<Boolean> getDeleteAccountSuccess() {
        return deleteAccountSuccess;
    }

    public void logout() {
        if (Boolean.TRUE.equals(loading.getValue())) {
            return;
        }

        generalError.setValue(null);
        logoutSuccess.setValue(false);
        logoutAllDevicesSuccess.setValue(false);
        deleteAccountSuccess.setValue(false);
        loading.setValue(true);

        authRepository.logout(new ApiCallback<Void>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                loading.postValue(false);
                logoutSuccess.postValue(true);
            }

            @Override
            public void onGeneralError(Exception e) {
                loading.postValue(false);
                generalError.postValue(getErrorMessage(
                        e, "Unable to log out. Please try again."
                ));
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                loading.postValue(false);
                generalError.postValue(getHttpErrorMessage(
                        response, "Unable to log out. Please try again."
                ));
            }
        });
    }

    public void logoutAllDevices() {
        if (Boolean.TRUE.equals(loading.getValue())) {
            return;
        }

        generalError.setValue(null);
        logoutSuccess.setValue(false);
        logoutAllDevicesSuccess.setValue(false);
        deleteAccountSuccess.setValue(false);
        loading.setValue(true);

        authRepository.logoutAllDevices(new ApiCallback<Void>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                loading.postValue(false);
                logoutAllDevicesSuccess.postValue(true);
            }

            @Override
            public void onGeneralError(Exception e) {
                loading.postValue(false);
                generalError.postValue(getErrorMessage(
                        e,
                        "Unable to log out from all devices. Please try again."
                ));
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                loading.postValue(false);
                generalError.postValue(getHttpErrorMessage(
                        response,
                        "Unable to log out from all devices. Please try again."
                ));
            }
        });
    }

    public void deleteAccount(String password) {
        if (Boolean.TRUE.equals(loading.getValue())) {
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            generalError.setValue("Please enter your password to delete your account.");
            return;
        }

        generalError.setValue(null);
        deleteAccountSuccess.setValue(false);
        logoutSuccess.setValue(false);
        logoutAllDevicesSuccess.setValue(false);
        loading.setValue(true);

        DeleteAccountRequest request = new DeleteAccountRequest();
        request.setPassword(password);

        userRepository.deleteAccount(request, new ApiCallback<Void>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                loading.postValue(false);
                deleteAccountSuccess.postValue(true);
            }

            @Override
            public void onGeneralError(Exception e) {
                loading.postValue(false);
                generalError.postValue(getErrorMessage(
                        e,
                        "Unable to delete your account. Please try again."
                ));
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                loading.postValue(false);
                generalError.postValue(getHttpErrorMessage(
                        response,
                        "Unable to delete your account. Please try again."
                ));
            }
        });
    }

    private String getErrorMessage(Exception e, String fallback) {
        if (e != null
                && e.getMessage() != null
                && !e.getMessage().trim().isEmpty()) {
            return e.getMessage();
        }

        return fallback;
    }

    private String getHttpErrorMessage(
            ApiResponse<?> response,
            String fallback
    ) {
        if (response != null
                && response.getError() != null
                && response.getError().getMessage() != null
                && !response.getError().getMessage().trim().isEmpty()) {
            return response.getError().getMessage();
        }

        return fallback;
    }
}
