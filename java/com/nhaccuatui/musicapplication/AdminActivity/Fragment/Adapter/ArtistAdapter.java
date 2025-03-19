package com.nhaccuatui.musicapplication.AdminActivity.Fragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;  // Import Glide
import com.nhaccuatui.musicapplication.ADTO.Artist;
import com.nhaccuatui.musicapplication.R;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private Context context;
    private List<Artist> artists;

    public ArtistAdapter(Context context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.artistName.setText(artist.getArtistName());

        String avatarUrl = "https://nhaccuatui.somee.com/source/Artists-Image/" + artist.getAvatarImage();

        // Load the artist avatar image using Glide
        Glide.with(context)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(holder.artistAvatar);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        ImageView artistAvatar;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.text_artist_name);
            artistAvatar = itemView.findViewById(R.id.image_artist_avatar);
        }
    }
}
