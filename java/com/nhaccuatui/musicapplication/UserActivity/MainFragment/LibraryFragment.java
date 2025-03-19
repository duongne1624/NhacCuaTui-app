package com.nhaccuatui.musicapplication.UserActivity.MainFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nhaccuatui.musicapplication.Adapter.PlaylistAdapter;
import com.nhaccuatui.musicapplication.DTO.Playlist;
import com.nhaccuatui.musicapplication.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LibraryFragment extends Fragment {

    private RecyclerView playlistsRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlists = new ArrayList<>();
    private ImageButton btnAddPlaylist;

    private Context context;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        // Bind RecyclerView and Button
        playlistsRecyclerView = view.findViewById(R.id.playlistsRecyclerView);
        btnAddPlaylist = view.findViewById(R.id.btnAddPlaylist);

        playlistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Adapter
        playlistAdapter = new PlaylistAdapter(getContext(), playlists);
        playlistsRecyclerView.setAdapter(playlistAdapter);

        // Get userId from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        if (userId != null) {
            fetchPlaylists(userId); // Fetch playlists for the logged-in user
        } else {
            Toast.makeText(getContext(), "Please log in to view playlists", Toast.LENGTH_SHORT).show();
        }

        // Handle Add Playlist button
        btnAddPlaylist.setOnClickListener(v -> showAddPlaylistDialog());

        return view;
    }

    private void fetchPlaylists(String userId) {
        String apiUrl = "https://nhaccuatui.somee.com/API/MyAPI/GetUserPlaylists?songId=0&userId=" + userId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to fetch playlists: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    requireActivity().runOnUiThread(() -> parseAndShowPlaylists(responseBody));
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error fetching playlists: " + response.code(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private void parseAndShowPlaylists(String responseBody) {
        try {
            Gson gson = new Gson();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            if (responseJson.get("success").getAsBoolean()) {
                Type playlistType = new TypeToken<List<Playlist>>() {}.getType();
                playlists = gson.fromJson(responseJson.get("playlists"), playlistType);

                // Update RecyclerView Adapter
                playlistAdapter = new PlaylistAdapter(getContext(), playlists);
                playlistsRecyclerView.setAdapter(playlistAdapter);
            } else {
                Toast.makeText(getContext(), "Failed to load playlists", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error parsing playlist data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddPlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Playlist");

        // Input field for playlist name
        final android.widget.EditText input = new android.widget.EditText(requireContext());
        input.setHint("Playlist Name");
        builder.setView(input);

        // "Add" button
        builder.setPositiveButton("Add", (dialog, which) -> {
            String playlistName = input.getText().toString().trim();
            if (!playlistName.isEmpty()) {
                addPlaylistToServer(playlistName);
            } else {
                Toast.makeText(getContext(), "Playlist name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // "Cancel" button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addPlaylistToServer(String playlistName) {
        String apiUrl = "https://nhaccuatui.somee.com/API/MyAPI/AddPlaylist?playlistName=" + playlistName + "&userId=" + userId.trim();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Failed to add playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Playlist added successfully", Toast.LENGTH_SHORT).show();
                        fetchPlaylists(userId);
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Playlist added successfully", Toast.LENGTH_SHORT).show();
                            fetchPlaylists(userId);
                    });
                }
            }
        });
    }
}
