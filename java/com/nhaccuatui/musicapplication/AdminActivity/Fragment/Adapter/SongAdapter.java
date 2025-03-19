package com.nhaccuatui.musicapplication.AdminActivity.Fragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhaccuatui.musicapplication.ADTO.Song;
import com.nhaccuatui.musicapplication.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context context;
    private List<Song> songs;

    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getSongName());
        holder.artistName.setText(song.getArtistName());

        String avatarUrl = "https://nhaccuatui.somee.com/source/Musics-Image/" + song.getSongCover();

        // Load song cover image using Glide
        Glide.with(context)
                .load(avatarUrl)  // Load the cover image from songCover URL
                .placeholder(R.drawable.default_song_cover)  // Placeholder image while loading
                .into(holder.songCover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        TextView songName, artistName;
        ImageView songCover, songImage; // Add a new ImageView for songImage

        public SongViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.text_song_name);
            artistName = itemView.findViewById(R.id.text_artist_name);
            songCover = itemView.findViewById(R.id.image_song_cover);
        }
    }
}
