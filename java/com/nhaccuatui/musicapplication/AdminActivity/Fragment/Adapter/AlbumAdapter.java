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
import com.nhaccuatui.musicapplication.ADTO.Album;
import com.nhaccuatui.musicapplication.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private Context context;
    private List<Album> albums;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.albumName.setText(album.getAlbumName());
        holder.artistName.setText(album.getArtistName());

        // Construct the full URL for the album cover
        String albumCoverUrl = "https://nhaccuatui.somee.com/source/Albums-Image/" + album.getAlbumCover();

        Glide.with(context)
                .load(albumCoverUrl)
                .placeholder(R.drawable.default_album_cover)
                .error(R.drawable.default_album_cover)
                .into(holder.albumCover);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView albumName;
        TextView artistName;
        ImageView albumCover;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.album_name);
            artistName = itemView.findViewById(R.id.artist_name);
            albumCover = itemView.findViewById(R.id.album_cover);
        }
    }
}
