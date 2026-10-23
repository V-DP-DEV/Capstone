
package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.domainModels.User;
import com.example.capstone.model.request.UpdateUserRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IUserRepository;

public class ProfileViewModel extends ViewModel {

    private final IUserRepository userRepository;

    private final MutableLiveData<User> profile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> generalError = new MutableLiveData<>();
    private final MutableLiveData<String> firstNameError = new MutableLiveData<>();
    private final MutableLiveData<String> surnameError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>(false);

    public ProfileViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<User> getProfile() {
        return profile;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getGeneralError() {
        return generalError;
    }

    public LiveData<String> getFirstNameError() {
        return firstNameError;
    }

    public LiveData<String> getSurnameError() {
        return surnameError;
    }

    public LiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public void loadProfile() {
        loading.setValue(true);
        generalError.setValue(null);

        userRepository.getProfile(new ApiCallback<User>() {
            @Override
            public void onSuccess(ApiResponse<User> response) {
                loading.postValue(false);

                if (response != null && response.getData() != null) {
                    profile.postValue(response.getData());
                } else {
                    generalError.postValue("Unable to load your profile.");
                }
            }

            @Override
            public void onGeneralError(Exception e) {
                loading.postValue(false);
                generalError.postValue(getErrorMessage(
                        e, "Unable to load your profile. Please try again."
                ));
            }

            @Override
            public void onHttpError(ApiResponse<User> response) {
                loading.postValue(false);
                generalError.postValue(getHttpErrorMessage(
                        response, "Unable to load your profile."
                ));
            }
        });
    }

    public void updateProfile(String firstName, String surname) {
        firstNameError.setValue(null);
        surnameError.setValue(null);
        generalError.setValue(null);
        updateSuccess.setValue(false);

        String cleanFirstName = firstName == null ? "" : firstName.trim();
        String cleanSurname = surname == null ? "" : surname.trim();

        boolean valid = true;

        if (cleanFirstName.isEmpty()) {
            firstNameError.setValue("First name cannot be empty.");
            valid = false;
        }

        if (cleanSurname.isEmpty()) {
            surnameError.setValue("Surname cannot be empty.");
            valid = false;
        }

        if (!valid) {
            return;
        }

        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstname(cleanFirstName);
        request.setSurname(cleanSurname);

        loading.setValue(true);

        userRepository.updateAccount(request, new ApiCallback<Void>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                loading.postValue(false);

                User currentUser = profile.getValue();

                if (currentUser != null) {
                    currentUser.setFirstname(cleanFirstName);
                    currentUser.setSurname(cleanSurname);
                    profile.postValue(currentUser);
                }

                updateSuccess.postValue(true);
            }

            @Override
            public void onGeneralError(Exception e) {
                loading.postValue(false);
                generalError.postValue(getErrorMessage(
                        e, "Unable to update your profile. Please try again."
                ));
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                loading.postValue(false);
                generalError.postValue(getHttpErrorMessage(
                        response, "Unable to update your profile."
                ));
            }
        });
    }

    private String getErrorMessage(Exception e, String fallback) {
        if (e != null && e.getMessage() != null
                && !e.getMessage().trim().isEmpty()) {
            return e.getMessage();
        }
        return fallback;
    }

    private String getHttpErrorMessage(
            ApiResponse<?> response, String fallback) {
        if (response != null && response.getError() != null
                && response.getError().getMessage() != null
                && !response.getError().getMessage().trim().isEmpty()) {
            return response.getError().getMessage();
        }
        return fallback;
    }
}
