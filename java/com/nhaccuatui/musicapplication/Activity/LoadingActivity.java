package com.nhaccuatui.musicapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.nhaccuatui.musicapplication.AdminActivity.AdminActivity;
import com.nhaccuatui.musicapplication.R;
import com.nhaccuatui.musicapplication.UserActivity.MainActivity;

public class LoadingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String PREF_IS_LOGGED_IN = "isLoggedIn";
    private static final String PREF_ROLE = "role";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean isLoggedIn = preferences.getBoolean(PREF_IS_LOGGED_IN, false);

                if (isLoggedIn) {
                    String role = preferences.getString(PREF_ROLE, "");

                    // Kiểm tra nếu role là "admin"
                    if ("Admin".equals(role)) {
                        Intent intent = new Intent(LoadingActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        }, 2000);
    }
}
