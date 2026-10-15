
package com.example.capstone.service;

import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;
import com.google.gson.JsonElement;

public class FeedbackService {

    private final ApiClient apiClient;

    public FeedbackService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    // Fetch candidate dashboard analytics
    public void getDashboard(
            int candidateId,
            ApiCallback<JsonElement> callback) {

        ApiRequest request = new ApiRequest(
                "candidates/" + candidateId + "/dashboard"
        );

        request.setMethodGET();
        request.setRequiresAuthentication(true);

        apiClient.execute(
                request,
                JsonElement.class,
                callback
        );
    }

    // Fetch candidate skills analysis
    public void getSkills(
            int candidateId,
            ApiCallback<JsonElement> callback) {

        ApiRequest request = new ApiRequest(
                "candidates/" + candidateId + "/skills"
        );

        request.setMethodGET();
        request.setRequiresAuthentication(true);

        apiClient.execute(
                request,
                JsonElement.class,
                callback
        );
    }
}
