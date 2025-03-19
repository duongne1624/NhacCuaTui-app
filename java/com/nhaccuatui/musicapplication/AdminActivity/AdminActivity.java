package com.nhaccuatui.musicapplication.AdminActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhaccuatui.musicapplication.Activity.LoginActivity;
import com.nhaccuatui.musicapplication.AdminActivity.Fragment.AdminHomeFragment;
import com.nhaccuatui.musicapplication.AdminActivity.Fragment.MoreFragment;
import com.nhaccuatui.musicapplication.R;
import com.nhaccuatui.musicapplication.UserActivity.MainFragment.ProfileFragment;

public class AdminActivity extends AppCompatActivity {

    private Fragment adminHomeFragment;
    private Fragment adminMoreFragment;
    private Fragment profileFragment;
    private Fragment activeFragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Initialize fragments
        adminHomeFragment = new AdminHomeFragment();
        adminMoreFragment = new MoreFragment();
        profileFragment = new ProfileFragment();

        // Add all fragments initially but only show the home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, adminHomeFragment, "HomeFragment")
                    .add(R.id.fragment_container, adminMoreFragment, "MoreFragment").hide(adminMoreFragment)
                    .add(R.id.fragment_container, profileFragment, "ProfileFragment").hide(profileFragment)
                    .commit();
            activeFragment = adminHomeFragment;
        } else {
            activeFragment = fragmentManager.findFragmentByTag("HomeFragment");
        }

        // Set up navigation item selection listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Handle fragment switching with if-else
            if (itemId == R.id.home) {
                if (activeFragment != adminHomeFragment) {
                    switchFragment(adminHomeFragment);
                }
            } else if (itemId == R.id.more) {
                if (activeFragment != adminMoreFragment) {
                    switchFragment(adminMoreFragment);
                }
            } else if (itemId == R.id.profile) {
                if (activeFragment != profileFragment) {
                    switchFragment(profileFragment);
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

        // Update BottomNavigationView visibility
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof AdminHomeFragment ||
                currentFragment instanceof MoreFragment ||
                currentFragment instanceof ProfileFragment) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit();
        activeFragment = fragment;
    }

    private boolean isLoggedIn() {
        return getSharedPreferences("UserPrefs", MODE_PRIVATE).getBoolean("isLoggedIn", false);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
