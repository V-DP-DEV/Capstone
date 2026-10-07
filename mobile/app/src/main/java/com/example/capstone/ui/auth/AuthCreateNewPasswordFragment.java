package com.example.capstone.ui.auth;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.capstone.R;
import com.example.capstone.databinding.FragmentAuthCreateNewPasswordBinding;
import com.example.capstone.ui.BaseActivity;

public class AuthCreateNewPasswordFragment extends Fragment {
    private FragmentAuthCreateNewPasswordBinding binding;
    private OnSavePasswordClickListener listener;

    public interface OnSavePasswordClickListener {
        void onSavePasswordClicked(String newPassword, String confirmPassword);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAuthCreateNewPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListeners();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String getNewPassword() {
        return binding != null ? binding.etNewPassword.getText().toString().trim() : "";
    }

    public String getConfirmPassword() {
        return binding != null ? binding.etConfirmPassword.getText().toString().trim() : "";
    }

    public void showNewPasswordError(String message) {
        if (binding == null) return;
        binding.tvNewPasswordError.setText(message);
        binding.tvNewPasswordError.setVisibility(View.VISIBLE);
    }
    public void showConfirmPasswordError(String message) {
        if (binding == null) return;
        binding.tvConfirmPasswordError.setText(message);
        binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
    }
    public void hideAllErrors() {
        if (binding == null) return;
        binding.tvNewPasswordError.setVisibility(View.GONE);
        binding.tvConfirmPasswordError.setVisibility(View.GONE);
    }
    public void setLengthRequirementSatisfied(boolean isSatisfied) {
        if (binding == null) return;
        int color = isSatisfied ? Color.parseColor("#388E3C") : Color.parseColor("#757575");
        binding.tvReqLength.setTextColor(color);
    }

    public void setNumberRequirementSatisfied(boolean isSatisfied) {
        if (binding == null) return;
        int color = isSatisfied ? Color.parseColor("#388E3C") : Color.parseColor("#757575");
        binding.tvReqNumber.setTextColor(color);
    }

    private void setupListeners() {
        // Save Button Click
        binding.btnSavePassword.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSavePasswordClicked(getNewPassword(), getConfirmPassword());
            }
        });
        binding.etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding == null) return;
                binding.tvNewPasswordError.setVisibility(View.GONE);

                String text = s.toString();
                setLengthRequirementSatisfied(text.length() >= 8);
                setNumberRequirementSatisfied(text.matches(".*\\d.*"));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding == null) return;
                binding.tvConfirmPasswordError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void setOnSavePasswordClickListener(OnSavePasswordClickListener listener) {
        this.listener = listener;
    }

}
