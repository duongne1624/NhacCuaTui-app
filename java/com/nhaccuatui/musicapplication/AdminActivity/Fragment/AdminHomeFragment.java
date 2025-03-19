package com.nhaccuatui.musicapplication.AdminActivity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.nhaccuatui.musicapplication.Adapter.AdminPagerAdapter;
import com.nhaccuatui.musicapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);

        AdminPagerAdapter adapter = new AdminPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Users"); break;
                case 1: tab.setText("Artists"); break;
                case 2: tab.setText("Albums"); break;
                case 3: tab.setText("Songs"); break;
                default: tab.setText("Unknown");
            }
        }).attach();

        return view;
    }
}