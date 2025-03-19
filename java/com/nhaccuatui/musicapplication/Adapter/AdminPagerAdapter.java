package com.nhaccuatui.musicapplication.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nhaccuatui.musicapplication.AdminActivity.Fragment.DataFragment.*;

public class AdminPagerAdapter extends FragmentStateAdapter {

    public AdminPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new UsersFragment();
            case 1: return new ArtistsFragment();
            case 2: return new AlbumsFragment();
            case 3: return new SongsFragment();
            default: return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Number of tabs
    }
}
