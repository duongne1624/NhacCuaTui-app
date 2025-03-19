package com.nhaccuatui.musicapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nhaccuatui.musicapplication.Activity.LoginActivity;
import com.nhaccuatui.musicapplication.Activity.PlayerActivity;
import com.nhaccuatui.musicapplication.DTO.Playlist;
import com.nhaccuatui.musicapplication.R;
import com.nhaccuatui.musicapplication.DTO.Song;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final Context context;
    private List<Song> songs;

    public SongAdapter(Context context, List<Song> songs) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.titleHolder.setText(song.getName());
        holder.artistHolder.setText(song.getArtist());

        String imageUrl = "https://nhaccuatui.somee.com/source/Musics-Image/" + song.getImageFile();
        String mp3Url = "https://nhaccuatui.somee.com/source/mp3/" + song.getAudioFile();

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.default_music)
                .error(R.drawable.default_music)
                .into(holder.artworkHolder);

        // Navigate to PlayerActivity on item click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("songTitle", song.getName());
            intent.putExtra("songArtist", song.getArtist());
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("mp3Url", mp3Url);
            context.startActivity(intent);
        });

        // Handle "Add to Playlist" button click
        holder.addPlaylistHolder.setOnClickListener(v -> {
            if (isLoggedIn()) {
                fetchAndShowPlaylists(song.getId());
            } else {
                showLoginPrompt();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView artworkHolder;
        TextView titleHolder, artistHolder;
        ImageButton addPlaylistHolder;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            artworkHolder = itemView.findViewById(R.id.artworkView);
            titleHolder = itemView.findViewById(R.id.titleView);
            artistHolder = itemView.findViewById(R.id.artistView);
            addPlaylistHolder = itemView.findViewById(R.id.addPlaylist);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateSongs(List<Song> updatedSongs) {
        this.songs = updatedSongs;
        notifyDataSetChanged();
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    private void fetchAndShowPlaylists(String songId) {
        String userId = getUserId();
        if (userId.isEmpty()) {
            showLoginPrompt();
            return;
        }

        String apiUrl = "https://nhaccuatui.somee.com/API/MyAPI/GetUserPlaylists?songId=" + songId + "&userId=" + userId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Failed to fetch playlists: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    new Handler(Looper.getMainLooper()).post(() -> parseAndShowPlaylists(responseBody, songId));
                } else {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Error fetching playlists: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void parseAndShowPlaylists(String responseBody, String songId) {
        try {
            Gson gson = new Gson();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            if (responseJson.get("success").getAsBoolean()) {
                Type playlistType = new TypeToken<List<Playlist>>() {}.getType();
                List<Playlist> playlists = gson.fromJson(responseJson.get("playlists"), playlistType);

                showPlaylistDialog(playlists, songId);
            } else {
                Toast.makeText(context, "Failed to load playlists", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to parse playlist data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPlaylistDialog(List<Playlist> playlists, String songId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add to Playlist");

        String[] playlistNames = playlists.stream().map(Playlist::getName).toArray(String[]::new);

        builder.setItems(playlistNames, (dialog, which) -> {
            Playlist selectedPlaylist = playlists.get(which);
            String playlistId = selectedPlaylist.getId();

            // Call the API to add the song to the selected playlist
            addSongToPlaylist(songId, playlistId);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void addSongToPlaylist(String songId, String playlistId) {
        String apiUrl = "https://nhaccuatui.somee.com/API/MyAPI/AddSongToPlaylist?songId=" + songId + "&playlistId=" + playlistId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(okhttp3.internal.Util.EMPTY_REQUEST) // Send an empty POST body
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Failed to add song: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Song added successfully!", Toast.LENGTH_SHORT).show());
                } else {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "Error: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void showLoginPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn cần phải đăng nhập để xem playlist. Bạn có muốn đăng nhập ngay?");

        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
