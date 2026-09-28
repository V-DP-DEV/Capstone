package com.capstone.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.capstone.myapplication.databinding.FragmentFirstBinding;
import com.capstone.myapplication.network.ApiCallback;
import com.capstone.myapplication.network.ApiRequest;
import com.capstone.myapplication.network.ApiResponse;
import com.capstone.myapplication.utils.SecureSession;

import org.json.JSONObject;

import java.util.UUID;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Login();



        binding.buttonFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void Login(){
        ApiRequest request = new ApiRequest("auth/login");
        //only for login
        String deviceId = UUID.randomUUID().toString();
        //only for login
        String deviceName = Build.MANUFACTURER + " " + Build.MODEL;

        request.setBody("{\"email\":\"admin@example.com\",\"password\":\"Password123\",\"deviceId\":\""+ deviceId +"\",\"deviceName\":\""+deviceName+"\"}");

        request.setMethodPost();
        request.execute(new ApiCallback() {
            @Override
            public void onSuccess(ApiResponse response) {
                SecureSession session = new SecureSession(requireContext());
                try{
                    JSONObject o = new JSONObject(response.getData());
                    System.out.println(o);
                    String token = o.getString("token");
                    System.out.println(token);
                    session.saveToken(token);
                    System.out.println(session.getToken());
                }
                catch (Exception e){
                    return;
                }
            }

            @Override
            public void onGeneralError(Exception e) {

            }

            @Override
            public void onHttpError(ApiResponse response) {
                System.out.println(response.getStatusCode());
            }
        });
    }
}