package com.nhaccuatui.musicapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaccuatui.musicapplication.AdminActivity.AdminActivity;
import com.nhaccuatui.musicapplication.UserActivity.MainActivity;
import com.nhaccuatui.musicapplication.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(username, password);
            }
        });
    }

    private void loginUser(String username, String password) {
        String apiUrl = "https://nhaccuatui.somee.com/API/MyAPI/LoginUser?taikhoan=" + username + "&matkhau=" + password;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    try {
                        // Parse the response (assuming it's in JSON array format)
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<List<String>>>() {}.getType();
                        List<List<String>> userData = gson.fromJson(responseBody, type);

                        if (userData != null && !userData.isEmpty()) {
                            // Extract user details from the first row
                            List<String> userDetails = userData.get(0);
                            saveUserInfo(userDetails);
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                boolean b = userDetails.get(5).contains("min");
                                if(b) {
                                    navigateToAdmin(); // Redirect to AdminActivity
                                } else {
                                    navigateToMain(); // Redirect to MainActivity
                                }
                            });
                        } else {
                            runOnUiThread(() ->
                                    Toast.makeText(LoginActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show()
                            );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(LoginActivity.this, "Failed to parse login data", Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void saveUserInfo(List<String> userDetails) {
        // Save user information in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isLoggedIn", true);
        editor.putString("userId", userDetails.get(0).trim());       // ID
        editor.putString("username", userDetails.get(1));    // Username
        editor.putString("email", userDetails.get(2).trim());       // Email
        editor.putString("phone", userDetails.get(3).trim());       // Phone
        editor.putString("fullName", userDetails.get(4));    // Full Name
        editor.putString("role", userDetails.get(5));        // Role
        editor.putString("createdDate", userDetails.get(6)); // Created Date
        editor.putString("lastLogin", userDetails.get(7));   // Last Login

        editor.apply();
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToAdmin() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
