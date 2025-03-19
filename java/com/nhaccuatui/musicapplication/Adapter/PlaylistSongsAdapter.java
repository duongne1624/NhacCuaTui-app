package com.nhaccuatui.musicapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhaccuatui.musicapplication.Activity.PlayerActivity;
import com.nhaccuatui.musicapplication.R;
import com.nhaccuatui.musicapplication.DTO.Song;

import java.util.List;

public class PlaylistSongsAdapter extends RecyclerView.Adapter<PlaylistSongsAdapter.SongViewHolder> {

    private final Context context;
    private final List<Song> songs;
    private final SongRemoveListener songRemoveListener;

    public PlaylistSongsAdapter(Context context, List<Song> songs, SongRemoveListener songRemoveListener) {
        this.context = context;
        this.songs = songs;
        this.songRemoveListener = songRemoveListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_row_item_playlist, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);

        holder.titleView.setText(song.getName());
        holder.artistView.setText(song.getArtist());

        String imageUrl = "https://nhaccuatui.somee.com/source/Musics-Image/" + song.getImageFile();
        String mp3Url = "https://nhaccuatui.somee.com/source/mp3/" + song.getAudioFile();

        // Navigate to PlayerActivity on item click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("songTitle", song.getName());
            intent.putExtra("songArtist", song.getArtist());
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("mp3Url", mp3Url);
            context.startActivity(intent);
        });

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.default_music)
                .error(R.drawable.default_music)
                .into(holder.artworkView);

        holder.deleteButton.setOnClickListener(v -> songRemoveListener.onSongRemove(song.getId()));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView artworkView;
        TextView titleView, artistView;
        ImageButton deleteButton;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            artworkView = itemView.findViewById(R.id.artworkView);
            titleView = itemView.findViewById(R.id.titleView);
            artistView = itemView.findViewById(R.id.artistView);
            deleteButton = itemView.findViewById(R.id.deleteSong);
        }
    }

    public interface SongRemoveListener {
        void onSongRemove(String songId);
    }
}
