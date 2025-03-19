package com.nhaccuatui.musicapplication.UserActivity.MainFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhaccuatui.musicapplication.Adapter.SongAdapter;
import com.nhaccuatui.musicapplication.DTO.Song;
import com.nhaccuatui.musicapplication.R;
import com.nhaccuatui.musicapplication.Fetcher.SongFetcher;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private SongAdapter songAdapter;
    private final List<Song> allSongs = new ArrayList<>();
    private final List<Song> filteredSongs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        songAdapter = new SongAdapter(getContext(), filteredSongs);
        recyclerView.setAdapter(songAdapter);

        fetchSongs();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable options menu for search
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_btn, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterSongs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void fetchSongs() {
        String apiUrl = "https://nhaccuatui.somee.com/API/MyAPI/GetAllSongs";

        SongFetcher.fetchSongs(apiUrl, new SongFetcher.FetchSongsCallback() {
            @Override
            public void onSuccess(List<Song> songs) {
                if (isAdded()) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        allSongs.clear();
                        allSongs.addAll(songs);

                        // Initially display all songs
                        filteredSongs.clear();
                        filteredSongs.addAll(allSongs);

                        songAdapter.updateSongs(filteredSongs);
                    });
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (isAdded()) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void filterSongs(String query) {
        filteredSongs.clear();

        if (query.isEmpty()) {
            filteredSongs.addAll(allSongs);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Song song : allSongs) {
                if (song.getName().toLowerCase().contains(lowerCaseQuery) ||
                        song.getArtist().toLowerCase().contains(lowerCaseQuery)) {
                    filteredSongs.add(song);
                }
            }
        }

        songAdapter.updateSongs(filteredSongs);
    }
}