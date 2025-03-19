package com.nhaccuatui.musicapplication.UserActivity.MainFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhaccuatui.musicapplication.Adapter.PlaylistSongsAdapter;
import com.nhaccuatui.musicapplication.DTO.Song;
import com.nhaccuatui.musicapplication.R;
import com.nhaccuatui.musicapplication.Fetcher.SongFetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlaylistSongsFragment extends Fragment {

    private static final String ARG_PLAYLIST_ID = "playlist_id";
    private RecyclerView recyclerView;
    private PlaylistSongsAdapter songAdapter;
    private final List<Song> songs = new ArrayList<>();
    private String playlistId;

    private ImageButton btnBack;

    public static PlaylistSongsFragment newInstance(String playlistId) {
        PlaylistSongsFragment fragment = new PlaylistSongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAYLIST_ID, playlistId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_songs, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
            requireActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        });


        songAdapter = new PlaylistSongsAdapter(getContext(), songs, this::removeSongFromPlaylist);
        recyclerView.setAdapter(songAdapter);

        if (getArguments() != null) {
            playlistId = getArguments().getString(ARG_PLAYLIST_ID);
            fetchSongsInPlaylist(playlistId);
        }

        return view;
    }

    private void fetchSongsInPlaylist(String playlistId) {
        String apiUrl = "https://nhaccuatui.somee.com/API/MyAPI/GetSongsInPlaylist?playlistId=" + playlistId;

        SongFetcher.fetchSongs(apiUrl, new SongFetcher.FetchSongsCallback() {
            @Override
            public void onSuccess(List<Song> fetchedSongs) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        songs.clear();
                        songs.addAll(fetchedSongs);
                        songAdapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Error fetching songs: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void removeSongFromPlaylist(String songId) {
        String apiUrl = "https://nhaccuatui.somee.com/API/MyAPI/RemoveSong?playlistId=" + playlistId + "&songId=" + songId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((AppCompatActivity) getContext()).runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to delete playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ((AppCompatActivity) getContext()).runOnUiThread(() -> {
                        fetchSongsInPlaylist(playlistId);
                        Toast.makeText(getContext(), "Playlist deleted successfully", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    ((AppCompatActivity) getContext()).runOnUiThread(() -> {
                        fetchSongsInPlaylist(playlistId);
                        Toast.makeText(getContext(), "Playlist deleted successfully", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
