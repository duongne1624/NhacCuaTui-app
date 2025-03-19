package com.nhaccuatui.musicapplication.UserActivity.MainFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nhaccuatui.musicapplication.Activity.LoginActivity;
import com.nhaccuatui.musicapplication.R;

public class ProfileFragment extends Fragment {

    private ImageView avatarImageView;
    private TextView usernameTextView, emailTextView, fullNameTextView, phoneTextView, roleTextView;
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Bind UI elements
        avatarImageView = view.findViewById(R.id.avatarImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        fullNameTextView = view.findViewById(R.id.fullNameTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);
        roleTextView = view.findViewById(R.id.roleTextView);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Load user info
        loadUserInfo();

        // Logout button click listener
        logoutButton.setOnClickListener(v -> {
            // Clear user login state
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clear all data
            editor.apply();

            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect to LoginActivity
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String fullName = sharedPreferences.getString("fullName", "N/A");
        String phone = sharedPreferences.getString("phone", "N/A");
        String role = sharedPreferences.getString("role", "N/A");

        // Populate UI elements
        usernameTextView.setText(username);
        emailTextView.setText(email);
        fullNameTextView.setText(fullName);
        phoneTextView.setText(phone);
        roleTextView.setText(role);
    }
}
