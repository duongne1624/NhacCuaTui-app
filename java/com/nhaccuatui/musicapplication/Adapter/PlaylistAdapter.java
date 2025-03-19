package com.nhaccuatui.musicapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nhaccuatui.musicapplication.DTO.Playlist;
import com.nhaccuatui.musicapplication.R;
import com.nhaccuatui.musicapplication.UserActivity.MainFragment.PlaylistSongsFragment;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private final Context context;
    private final List<Playlist> playlists;

    public PlaylistAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.playlistName.setText(playlist.getName());

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) context;

            Fragment fragment = PlaylistSongsFragment.newInstance(playlist.getId());
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();

            View navigationView = activity.findViewById(R.id.bottom_navigation);
            if (navigationView != null) {
                navigationView.setVisibility(View.GONE);
            }
        });

        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog(Integer.parseInt(playlist.getId()), position));
    }

    private void showDeleteConfirmationDialog(int playlistId, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Playlist")
                .setMessage("Are you sure you want to delete this playlist?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deletePlaylist(playlistId, position);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void deletePlaylist(int playlistId, int position) {
        String url = "https://nhaccuatui.somee.com/API/MyAPI/DeletePlaylist?playlistId=" + playlistId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ((AppCompatActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Failed to delete playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ((AppCompatActivity) context).runOnUiThread(() -> {
                        playlists.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Playlist deleted successfully", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    ((AppCompatActivity) context).runOnUiThread(() -> {
                        playlists.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Playlist deleted successfully", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView playlistName;
        ImageButton btnDelete;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistName = itemView.findViewById(R.id.playlistName);
            btnDelete = itemView.findViewById(R.id.btnDeletePlaylist);
        }
    }
}
