
package com.example.capstone.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.service.FeedbackService;
import com.example.capstone.util.Resource;
import com.google.gson.JsonElement;

public class FeedbackRepository {

    private final FeedbackService feedbackService;

    public FeedbackRepository(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    public LiveData<Resource<JsonElement>> getDashboard(
            int candidateId) {

        MutableLiveData<Resource<JsonElement>> result =
                new MutableLiveData<>();

        result.setValue(Resource.loading());

        feedbackService.getDashboard(candidateId,
                new ApiCallback<JsonElement>() {

                    @Override
                    public void onSuccess(
                            ApiResponse<JsonElement> response) {

                        result.setValue(
                                Resource.success(response.getData())
                        );
                    }

                    @Override
                    public void onGeneralError(Exception e) {

                        result.setValue(
                                Resource.error(
                                        e.getMessage() != null
                                                ? e.getMessage()
                                                : "Network error"
                                )
                        );
                    }

                    @Override
                    public void onHttpError(
                            ApiResponse<JsonElement> response) {

                        String message = "Request failed";

                        if (response.getError() != null
                                && response.getError().getMessage() != null) {
                            message = response.getError().getMessage();
                        }

                        result.setValue(Resource.error(message));
                    }
                });

        return result;
    }

    public LiveData<Resource<JsonElement>> getSkills(
            int candidateId) {

        MutableLiveData<Resource<JsonElement>> result =
                new MutableLiveData<>();

        result.setValue(Resource.loading());

        feedbackService.getSkills(candidateId,
                new ApiCallback<JsonElement>() {

                    @Override
                    public void onSuccess(
                            ApiResponse<JsonElement> response) {

                        result.setValue(
                                Resource.success(response.getData())
                        );
                    }

                    @Override
                    public void onGeneralError(Exception e) {

                        result.setValue(
                                Resource.error(
                                        e.getMessage() != null
                                                ? e.getMessage()
                                                : "Network error"
                                )
                        );
                    }

                    @Override
                    public void onHttpError(
                            ApiResponse<JsonElement> response) {

                        String message = "Request failed";

                        if (response.getError() != null
                                && response.getError().getMessage() != null) {
                            message = response.getError().getMessage();
                        }

                        result.setValue(Resource.error(message));
                    }
                });

        return result;
    }
}
