package com.nhaccuatui.musicapplication.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhaccuatui.musicapplication.Activity.LoginActivity;
import com.nhaccuatui.musicapplication.UserActivity.MainFragment.HomeFragment;
import com.nhaccuatui.musicapplication.UserActivity.MainFragment.LibraryFragment;
import com.nhaccuatui.musicapplication.UserActivity.MainFragment.ProfileFragment;
import com.nhaccuatui.musicapplication.R;

public class MainActivity extends AppCompatActivity {

    private Fragment homeFragment;
    private Fragment libraryFragment;
    private Fragment profileFragment;
    private Fragment activeFragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Initialize fragments
        homeFragment = new HomeFragment();
        libraryFragment = new LibraryFragment();
        profileFragment = new ProfileFragment();
        activeFragment = homeFragment;

        // Add all fragments initially but only show the home fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, profileFragment, "ProfileFragment").hide(profileFragment)
                .add(R.id.fragment_container, libraryFragment, "LibraryFragment").hide(libraryFragment)
                .add(R.id.fragment_container, homeFragment, "HomeFragment")
                .commit();

        // Set up navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                switchFragment(homeFragment);
            } else if (item.getItemId() == R.id.library) {
                switchFragment(libraryFragment);
            } else if (item.getItemId() == R.id.profile) {
                if (isLoggedIn()) {
                    switchFragment(profileFragment);
                } else {
                    Toast.makeText(this, "Vui lòng đăng nhập để xem thông tin!", Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                }
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }

        // Hiển thị lại BottomNavigationView nếu quay về Fragment chính
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof HomeFragment ||
                currentFragment instanceof LibraryFragment ||
                currentFragment instanceof ProfileFragment) {
            findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        }
    }


    private void switchFragment(Fragment fragment) {
        if (activeFragment != fragment) {
            getSupportFragmentManager().beginTransaction()
                    .hide(activeFragment) // Hide the currently active fragment
                    .show(fragment)      // Show the selected fragment
                    .commit();
            activeFragment = fragment;
        }
    }

    private boolean isLoggedIn() {
        return getSharedPreferences("UserPrefs", MODE_PRIVATE).getBoolean("isLoggedIn", false);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
