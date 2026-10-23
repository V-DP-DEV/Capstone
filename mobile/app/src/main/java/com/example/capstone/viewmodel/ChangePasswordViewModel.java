package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.model.request.ChangePasswordRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IUserRepository;

/**
 * ViewModel responsible for managing password change state, form validation rules
 * (length, numeric, and special character checks), and secure repository communication in the MVVM architecture.
 */
public class ChangePasswordViewModel extends ViewModel {

    private final IUserRepository userRepository;

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> oldPasswordError =
            new MutableLiveData<>();

    private final MutableLiveData<String> newPasswordError =
            new MutableLiveData<>();

    private final MutableLiveData<String> confirmPasswordError =
            new MutableLiveData<>();

    private final MutableLiveData<String> generalError =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> changePasswordSuccess =
            new MutableLiveData<>(false);

    public ChangePasswordViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getOldPasswordError() {
        return oldPasswordError;
    }

    public LiveData<String> getNewPasswordError() {
        return newPasswordError;
    }

    public LiveData<String> getConfirmPasswordError() {
        return confirmPasswordError;
    }

    public LiveData<String> getGeneralError() {
        return generalError;
    }

    public LiveData<Boolean> getChangePasswordSuccess() {
        return changePasswordSuccess;
    }

    public void changePassword(
            String oldPassword,
            String newPassword,
            String confirmPassword
    ) {
        clearErrors();
        changePasswordSuccess.setValue(false);

        String cleanOldPassword =
                oldPassword == null ? "" : oldPassword;

        String cleanNewPassword =
                newPassword == null ? "" : newPassword;

        String cleanConfirmPassword =
                confirmPassword == null ? "" : confirmPassword;

        boolean valid = true;

        if (cleanOldPassword.trim().isEmpty()) {
            oldPasswordError.setValue(
                    "Please enter your current password."
            );
            valid = false;
        }

        if (cleanNewPassword.isEmpty()) {
            newPasswordError.setValue(
                    "Please enter a new password."
            );
            valid = false;
        } else if (cleanNewPassword.length() < 8) {
            newPasswordError.setValue(
                    "Password must contain at least 8 characters."
            );
            valid = false;
        } else if (!cleanNewPassword.matches(".*\\d.*")) {
            newPasswordError.setValue(
                    "Password must contain at least one number."
            );
            valid = false;
        } else if (!cleanNewPassword.matches(".*[^a-zA-Z0-9].*")) {
            newPasswordError.setValue(
                    "Password must contain at least one special character."
            );
            valid = false;
        }

        if (cleanConfirmPassword.isEmpty()) {
            confirmPasswordError.setValue(
                    "Please confirm your new password."
            );
            valid = false;
        } else if (!cleanNewPassword.equals(cleanConfirmPassword)) {
            confirmPasswordError.setValue(
                    "Passwords do not match."
            );
            valid = false;
        }

        if (!valid) {
            return;
        }

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(cleanOldPassword);
        request.setNewPassword(cleanNewPassword);

        loading.setValue(true);

        userRepository.changePassword(
                request,
                new ApiCallback<Void>() {

                    @Override
                    public void onSuccess(ApiResponse<Void> response) {
                        loading.postValue(false);
                        changePasswordSuccess.postValue(true);
                    }

                    @Override
                    public void onGeneralError(Exception e) {
                        loading.postValue(false);

                        generalError.postValue(
                                getErrorMessage(
                                        e,
                                        "Unable to change your password. Please try again."
                                )
                        );
                    }

                    @Override
                    public void onHttpError(ApiResponse<Void> response) {
                        loading.postValue(false);

                        generalError.postValue(
                                getHttpErrorMessage(
                                        response,
                                        "Unable to change your password. Check your current password and try again."
                                )
                        );
                    }
                }
        );
    }

    private void clearErrors() {
        oldPasswordError.setValue(null);
        newPasswordError.setValue(null);
        confirmPasswordError.setValue(null);
        generalError.setValue(null);
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
